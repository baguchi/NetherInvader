package baguchan.nether_invader.entity.ai;

import baguchan.nether_invader.entity.PiglinWarrior;
import baguchan.nether_invader.entity.behavior.PiglinRaiding;
import baguchan.nether_invader.entity.behavior.PiglinWarriorAttack;
import baguchan.nether_invader.entity.behavior.StartHuntingHoglinUniversal;
import baguchan.nether_invader.entity.behavior.StopHoldingItemIfNoLongerAdmiringUniversal;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class PiglinWarriorAi {
    protected static final UniformInt TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);


    public static List<ActivityData<PiglinWarrior>> getActivities(PiglinWarrior piglin) {
        return List.of(initCoreActivity(), initIdleActivity(), initFightActivity(piglin), initAdmireItemActivity());
    }

    public static void initMemories(PiglinWarrior body, RandomSource random) {
        int delayUntilFirstHunt = TIME_BETWEEN_HUNTS.sample(random);
        body.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, true, delayUntilFirstHunt);
    }

    private static ActivityData<PiglinWarrior> initCoreActivity() {
        return ActivityData.create(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        InteractWithDoor.create(),
                        StopHoldingItemIfNoLongerAdmiringUniversal.create(),
                        StartAdmiringItemIfSeen.create(119),
                        StopBeingAngryIfTargetDead.create()
                )
        );
    }

    private static ActivityData<PiglinWarrior> initIdleActivity() {
        return ActivityData.create(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        SetEntityLookTarget.create(PiglinWarriorAi::isPlayerHoldingLovedItem, 14.0F),
                        StartAttacking.create((level, piglin) -> true, PiglinWarriorAi::findNearestValidAttackTarget),
                        BehaviorBuilder.triggerIf(PiglinWarrior::canHunt, StartHuntingHoglinUniversal.create()),
                        createIdleLookBehaviors(),
                        createIdleMovementBehaviors(),
                        SetLookAndInteract.create(EntityType.PLAYER, 4)
                )
        );
    }

    private static ActivityData<PiglinWarrior> initFightActivity(PiglinWarrior body) {
        return ActivityData.create(
                Activity.FIGHT,
                10,
                ImmutableList.<net.minecraft.world.entity.ai.behavior.BehaviorControl<? super PiglinWarrior>>of(
                        StopAttackingIfTargetInvalid.create((level, target) -> !isNearestValidAttackTarget(level, body, target)),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        new PiglinWarriorAttack<>(13, 32, 32, 0.8F),
                        RememberIfHoglinWasKilled.create()),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static ActivityData<PiglinWarrior> initAdmireItemActivity() {
        return ActivityData.create(
                Activity.ADMIRE_ITEM,
                10,
                ImmutableList.of(
                        GoToWantedItem.create(PiglinWarriorAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9),
                        StopAdmiringIfItemTooFarAway.create(9),
                        StopAdmiringIfTiredOfTryingToReachItem.create(200, 200)
                ),
                MemoryModuleType.ADMIRING_ITEM
        );
    }


    private static ImmutableList<Pair<OneShot<LivingEntity>, Integer>> createLookBehaviors() {
        return ImmutableList.of(
                Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 1),
                Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), 1),
                Pair.of(SetEntityLookTarget.create(8.0F), 1)
        );
    }

    private static RunOne<LivingEntity> createIdleLookBehaviors() {
        return new RunOne<>(
                ImmutableList.<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>builder()
                        .addAll(createLookBehaviors())
                        .add(Pair.of(new DoNothing(30, 60), 1))
                        .build()
        );
    }

    private static RunOne<PiglinWarrior> createIdleMovementBehaviors() {
        return new RunOne<>(
                ImmutableList.of(
                        Pair.of(PiglinRaiding.create(1.0F), 2),
                        Pair.of(RandomStroll.stroll(0.6F), 2),
                        Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2),
                        Pair.of(SetWalkTargetFromLookTarget.create(0.6F, 3), 2),
                        Pair.of(new DoNothing(30, 60), 1)
                )
        );
    }

    public static void updateActivity(PiglinWarrior p_34899_) {
        Brain<PiglinWarrior> brain = p_34899_.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse(null);
        brain.setActiveActivityToFirstValid(
                ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.ADMIRE_ITEM, Activity.IDLE)
        );
        Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
        if (activity != activity1) {
            getSoundForCurrentActivity(p_34899_).ifPresent(p_34899_::makeSound);
        }

        p_34899_.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    public static void pickUpItem(ServerLevel level, PiglinWarrior body, ItemEntity itemEntity) {
        stopWalking(body);
        ItemStack taken;
        if (itemEntity.getItem().is(Items.GOLD_NUGGET)) {
            body.take(itemEntity, itemEntity.getItem().getCount());
            taken = itemEntity.getItem();
            itemEntity.discard();
        } else {
            body.take(itemEntity, 1);
            taken = removeOneItemFromItemEntity(itemEntity);
        }

        if (isLovedItem(taken)) {
            body.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(level, body, taken);
            admireGoldItem(body);
        } else if (isFood(taken) && !hasEatenRecently(body)) {
            eat(body);
        }
    }

    private static boolean hasEatenRecently(AbstractPiglin body) {
        return body.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
    }


    private static void eat(AbstractPiglin body) {
        body.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, true, 200L);
    }

    private static void holdInOffhand(ServerLevel level, PiglinWarrior body, ItemStack itemStack) {
        if (isHoldingItemInOffHand(body)) {
            body.spawnAtLocation(level, body.getItemInHand(InteractionHand.OFF_HAND));
        }

        body.holdInOffHand(itemStack);
    }

    private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
        ItemStack sourceStack = itemEntity.getItem();
        ItemStack removedStack = sourceStack.split(1);
        if (sourceStack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(sourceStack);
        }

        return removedStack;
    }

    public static void stopHoldingOffHandItem(ServerLevel level, AbstractPiglin body, boolean barteringEnabled) {
        ItemStack itemStack = body.getItemInHand(InteractionHand.OFF_HAND);
        body.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        boolean barterCurrency = itemStack.isPiglinCurrency();
        if (barteringEnabled && barterCurrency) {
            throwItems(body, getBarterResponseItems(body));
        }
    }

    public static InteractionResult mobInteract(ServerLevel level, PiglinWarrior body, Player player, InteractionHand hand) {
        ItemStack playerHeldItemStack = player.getItemInHand(hand);
        if (canAdmire(body, playerHeldItemStack)) {
            ItemStack taken = playerHeldItemStack.consumeAndReturn(1, player);
            holdInOffhand(level, body, taken);
            admireGoldItem(body);
            stopWalking(body);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    protected static void cancelAdmiring(ServerLevel level, Piglin body) {
        if (isAdmiringItem(body) && !body.getOffhandItem().isEmpty()) {
            body.spawnAtLocation(level, body.getOffhandItem());
            body.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }


    private static void throwItems(AbstractPiglin body, List<ItemStack> itemStacks) {
        Optional<Player> player = body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if (player.isPresent()) {
            throwItemsTowardPlayer(body, player.get(), itemStacks);
        } else {
            throwItemsTowardRandomPos(body, itemStacks);
        }
    }

    private static void throwItemsTowardRandomPos(AbstractPiglin body, List<ItemStack> itemStacks) {
        throwItemsTowardPos(body, itemStacks, getRandomNearbyPos(body));
    }

    private static void throwItemsTowardPlayer(AbstractPiglin body, Player player, List<ItemStack> itemStacks) {
        throwItemsTowardPos(body, itemStacks, player.position());
    }

    private static void throwItemsTowardPos(AbstractPiglin body, List<ItemStack> itemStacks, Vec3 targetPos) {
        if (!itemStacks.isEmpty()) {
            body.swing(InteractionHand.OFF_HAND);

            for (ItemStack itemStack : itemStacks) {
                BehaviorUtils.throwItem(body, itemStack, targetPos.add(0.0, 1.0, 0.0));
            }
        }
    }

    private static List<ItemStack> getBarterResponseItems(AbstractPiglin body) {
        LootTable lootTable = body.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);
        List<ItemStack> items = lootTable.getRandomItems(
                new LootParams.Builder((ServerLevel) body.level()).withParameter(LootContextParams.THIS_ENTITY, body).create(LootContextParamSets.PIGLIN_BARTER)
        );
        return items;
    }

    private static void admireGoldItem(LivingEntity body) {
        body.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 119L);
    }

    public static boolean canAdmire(AbstractPiglin body, ItemStack playerHeldItemStack) {
        return !isAdmiringDisabled(body) && !isAdmiringItem(body) && body.isAdult() && playerHeldItemStack.isPiglinCurrency();
    }

    private static boolean isAdmiringItem(AbstractPiglin body) {
        return body.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }

    public static boolean isLovedItem(ItemStack p_149966_) {
        return p_149966_.is(ItemTags.PIGLIN_LOVED);
    }

    public static boolean wantsToPickup(AbstractPiglin body, ItemStack itemStack) {
        if (body.isBaby() && itemStack.is(ItemTags.IGNORED_BY_PIGLIN_BABIES)) {
            return false;
        } else if (itemStack.is(ItemTags.PIGLIN_REPELLENTS)) {
            return false;
        } else if (isAdmiringDisabled(body) && body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        } else if (itemStack.isPiglinCurrency()) {
            return isNotHoldingLovedItemInOffHand(body);
        } else {
            return false;
        }
    }

    private static boolean isNearestValidAttackTarget(ServerLevel serverLevel, PiglinWarrior piglin, LivingEntity target) {
        return findNearestValidAttackTarget(serverLevel, piglin).filter(p_34887_ -> p_34887_ == target).isPresent();
    }

    private static boolean isNearZombified(PiglinWarrior p_34999_) {
        Brain<PiglinWarrior> brain = p_34999_.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
            LivingEntity livingentity = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
            return p_34999_.closerThan(livingentity, 6.0);
        } else {
            return false;
        }
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, PiglinWarrior body) {
        Brain<PiglinWarrior> brain = body.getBrain();

        Optional<LivingEntity> angryAt = BehaviorUtils.getLivingEntityFromUUIDMemory(body, MemoryModuleType.ANGRY_AT);
        if (angryAt.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(level, body, angryAt.get())) {
            return angryAt;
        } else {
            if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
                Optional<Player> player = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
                if (player.isPresent()) {
                    return player;
                }
            }

            Optional<Mob> nemesis = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            if (nemesis.isPresent()) {
                return nemesis;
            } else {
                Optional<Player> playerNotWearingGold = brain.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
                return playerNotWearingGold.isPresent() && Sensor.isEntityAttackable(level, body, playerNotWearingGold.get())
                        ? playerNotWearingGold
                        : Optional.empty();
            }
        }
    }

    public static void wasHurtBy(ServerLevel serverLevel, PiglinWarrior piglin, LivingEntity target) {
        if (!(target instanceof PiglinWarrior)) {

            Brain<PiglinWarrior> brain = piglin.getBrain();
            brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
            brain.eraseMemory(MemoryModuleType.DANCING);

            getAvoidTarget(piglin).ifPresent(p_348319_ -> {
                if (p_348319_.getType() != target.getType()) {
                    brain.eraseMemory(MemoryModuleType.AVOID_TARGET);
                }
            });
            if (piglin.isBaby()) {
                brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, 100L);
                if (Sensor.isEntityAttackableIgnoringLineOfSight(serverLevel, piglin, target)) {
                    broadcastAngerTarget(serverLevel, piglin, target);
                }
            } else {
                maybeRetaliate(serverLevel, piglin, target);
            }
        }
    }

    protected static void maybeRetaliate(ServerLevel serverLevel, AbstractPiglin p_34827_, LivingEntity p_34828_) {
        if (!p_34827_.getBrain().isActive(Activity.AVOID)) {
            if (Sensor.isEntityAttackableIgnoringLineOfSight(serverLevel, p_34827_, p_34828_)) {
                if (!BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(p_34827_, p_34828_, 4.0)) {
                    if (p_34828_.getType() == EntityType.PLAYER && serverLevel.getGameRules().get(GameRules.UNIVERSAL_ANGER)) {
                        setAngerTargetToNearestTargetablePlayerIfFound(serverLevel, p_34827_, p_34828_);
                        broadcastUniversalAnger(serverLevel, p_34827_);
                    } else {
                        setAngerTarget(serverLevel, p_34827_, p_34828_);
                        broadcastAngerTarget(serverLevel, p_34827_, p_34828_);
                    }
                }
            }
        }
    }

    public static Optional<SoundEvent> getSoundForCurrentActivity(PiglinWarrior p_34948_) {
        return p_34948_.getBrain().getActiveNonCoreActivity().map(p_34908_ -> getSoundForActivity(p_34948_, p_34908_));
    }

    private static SoundEvent getSoundForActivity(PiglinWarrior p_34855_, Activity p_34856_) {
        if (p_34856_ == Activity.FIGHT) {
            return SoundEvents.PIGLIN_ANGRY;
        } else if (p_34855_.isConverting()) {
            return SoundEvents.PIGLIN_RETREAT;
        } else if (p_34856_ == Activity.AVOID && isNearAvoidTarget(p_34855_)) {
            return SoundEvents.PIGLIN_RETREAT;
        } else if (p_34856_ == Activity.ADMIRE_ITEM) {
            return SoundEvents.PIGLIN_ADMIRING_ITEM;
        } else if (p_34856_ == Activity.CELEBRATE) {
            return SoundEvents.PIGLIN_CELEBRATE;
        } else {
            return isNearRepellent(p_34855_) ? SoundEvents.PIGLIN_RETREAT : SoundEvents.PIGLIN_AMBIENT;
        }
    }

    private static boolean isNearAvoidTarget(PiglinWarrior p_35003_) {
        Brain<PiglinWarrior> brain = p_35003_.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET) && brain.getMemory(MemoryModuleType.AVOID_TARGET).get().closerThan(p_35003_, 12.0);
    }

    protected static List<AbstractPiglin> getVisibleAdultPiglins(PiglinWarrior p_35005_) {
        return p_35005_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    private static List<AbstractPiglin> getAdultPiglins(AbstractPiglin p_34961_) {
        return p_34961_.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    private static void stopWalking(PiglinWarrior p_35007_) {
        p_35007_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_35007_.getNavigation().stop();
    }

    protected static void dontKillAnyMoreHoglinsForAWhile(AbstractPiglin body) {
        body.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, true, TIME_BETWEEN_HUNTS.sample(body.level().getRandom()));
    }

    public static void broadcastAngerTarget(ServerLevel serverLevel, AbstractPiglin p_34896_, LivingEntity p_34897_) {
        getAdultPiglins(p_34896_).forEach(p_348314_ -> {
            if (p_34897_.getType() != EntityType.HOGLIN) {
                setAngerTargetIfCloserThanCurrent(serverLevel, p_348314_, p_34897_);
            }
        });
    }

    protected static void broadcastUniversalAnger(ServerLevel serverLevel, AbstractPiglin p_34825_) {
        getAdultPiglins(p_34825_).forEach(p_34991_ -> getNearestVisibleTargetablePlayer(p_34991_).ifPresent(p_149964_ -> setAngerTarget(serverLevel, p_34991_, p_149964_)));
    }

    public static void setAngerTarget(ServerLevel serverLevel, AbstractPiglin p_34925_, LivingEntity p_34926_) {
        if (Sensor.isEntityAttackableIgnoringLineOfSight(serverLevel, p_34925_, p_34926_)) {
            p_34925_.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            p_34925_.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, p_34926_.getUUID(), 600L);

            if (p_34926_.getType() == EntityType.PLAYER && serverLevel.getGameRules().get(GameRules.UNIVERSAL_ANGER)) {
                p_34925_.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
            }
        }
    }

    private static void setAngerTargetToNearestTargetablePlayerIfFound(ServerLevel serverLevel, AbstractPiglin p_34945_, LivingEntity p_34946_) {
        Optional<Player> optional = getNearestVisibleTargetablePlayer(p_34945_);
        if (optional.isPresent()) {
            setAngerTarget(serverLevel, p_34945_, optional.get());
        } else {
            setAngerTarget(serverLevel, p_34945_, p_34946_);
        }
    }

    private static void setAngerTargetIfCloserThanCurrent(ServerLevel serverLevel, AbstractPiglin p_34963_, LivingEntity p_34964_) {
        Optional<LivingEntity> optional = getAngerTarget(p_34963_);
        LivingEntity livingentity = BehaviorUtils.getNearestTarget(p_34963_, optional, p_34964_);
        if (!optional.isPresent() || optional.get() != livingentity) {
            setAngerTarget(serverLevel, p_34963_, livingentity);
        }
    }

    private static Optional<LivingEntity> getAngerTarget(AbstractPiglin p_34976_) {
        return BehaviorUtils.getLivingEntityFromUUIDMemory(p_34976_, MemoryModuleType.ANGRY_AT);
    }

    public static Optional<LivingEntity> getAvoidTarget(PiglinWarrior p_34987_) {
        return p_34987_.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)
                ? p_34987_.getBrain().getMemory(MemoryModuleType.AVOID_TARGET)
                : Optional.empty();
    }

    public static Optional<Player> getNearestVisibleTargetablePlayer(AbstractPiglin p_34894_) {
        return p_34894_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)
                ? p_34894_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)
                : Optional.empty();
    }

    private static void broadcastRetreat(PiglinWarrior p_34930_, LivingEntity p_34931_) {
        getVisibleAdultPiglins(p_34930_)
                .stream()
                .filter(p_34985_ -> p_34985_ instanceof Piglin)
                .forEach(p_34819_ -> retreatFromNearestTarget((PiglinWarrior) p_34819_, p_34931_));
    }

    private static void retreatFromNearestTarget(PiglinWarrior p_34950_, LivingEntity p_34951_) {
        Brain<PiglinWarrior> brain = p_34950_.getBrain();
        LivingEntity $$3 = BehaviorUtils.getNearestTarget(p_34950_, brain.getMemory(MemoryModuleType.AVOID_TARGET), p_34951_);
        $$3 = BehaviorUtils.getNearestTarget(p_34950_, brain.getMemory(MemoryModuleType.ATTACK_TARGET), $$3);
        setAvoidTargetAndDontHuntForAWhile(p_34950_, $$3);
    }

    public static boolean wantsToStopFleeing(PiglinWarrior p_35009_) {
        Brain<PiglinWarrior> brain = p_35009_.getBrain();
        if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            return true;
        } else {
            LivingEntity livingentity = brain.getMemory(MemoryModuleType.AVOID_TARGET).get();
            EntityType<?> entitytype = livingentity.getType();
            if (entitytype == EntityType.HOGLIN) {
                return piglinsEqualOrOutnumberHoglins(p_35009_);
            } else {
                return isZombified(entitytype) && !brain.isMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, livingentity);
            }
        }
    }

    private static boolean piglinsEqualOrOutnumberHoglins(PiglinWarrior p_35011_) {
        return !hoglinsOutnumberPiglins(p_35011_);
    }

    private static boolean hoglinsOutnumberPiglins(PiglinWarrior p_35013_) {
        int i = p_35013_.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(0) + 1;
        int j = p_35013_.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(0);
        return j > i;
    }

    private static void setAvoidTargetAndDontHuntForAWhile(PiglinWarrior p_34968_, LivingEntity p_34969_) {
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }


    private static Vec3 getRandomNearbyPos(AbstractPiglin p_35017_) {
        Vec3 vec3 = LandRandomPos.getPos(p_35017_, 4, 2);
        return vec3 == null ? p_35017_.position() : vec3;
    }


    protected static boolean isIdle(AbstractPiglin p_34943_) {
        return p_34943_.getBrain().isActive(Activity.IDLE);
    }

    private static boolean hasCrossbow(LivingEntity p_34919_) {
        return p_34919_.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem);
    }

    private static boolean isFood(ItemStack p_149970_) {
        return p_149970_.is(ItemTags.PIGLIN_FOOD);
    }

    private static boolean isNearRepellent(PiglinWarrior p_35023_) {
        return p_35023_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
    }


    public static boolean isPlayerHoldingLovedItem(LivingEntity p_34884_) {
        return p_34884_.getType() == EntityType.PLAYER && p_34884_.isHolding(PiglinWarriorAi::isLovedItem);
    }

    private static boolean isAdmiringDisabled(AbstractPiglin p_35025_) {
        return p_35025_.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }

    private static boolean wasHurtRecently(LivingEntity p_34989_) {
        return p_34989_.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }

    private static boolean isHoldingItemInOffHand(AbstractPiglin p_35027_) {
        return !p_35027_.getOffhandItem().isEmpty();
    }

    private static boolean isNotHoldingLovedItemInOffHand(AbstractPiglin p_35029_) {
        return p_35029_.getOffhandItem().isEmpty() || !isLovedItem(p_35029_.getOffhandItem());
    }

    public static boolean isZombified(EntityType<?> p_34807_) {
        return p_34807_ == EntityType.ZOMBIFIED_PIGLIN || p_34807_ == EntityType.ZOGLIN;
    }
}
