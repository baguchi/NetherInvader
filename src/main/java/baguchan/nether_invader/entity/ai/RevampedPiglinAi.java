package baguchan.nether_invader.entity.ai;

import baguchan.nether_invader.entity.AgressivePiglin;
import baguchan.nether_invader.entity.behavior.PiglinRaiding;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class RevampedPiglinAi {
    public static final int PLAYER_ANGER_RANGE = 35;
    protected static final UniformInt TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);

    private static final UniformInt RIDE_START_INTERVAL = TimeUtil.rangeOfSeconds(10, 40);
    private static final UniformInt RIDE_DURATION = TimeUtil.rangeOfSeconds(10, 30);
    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
    private static final UniformInt AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
    private static final UniformInt BABY_AVOID_NEMESIS_DURATION = TimeUtil.rangeOfSeconds(5, 7);

    public static Brain<?> makeBrain(AgressivePiglin p_34841_, Brain<AgressivePiglin> p_34842_) {
        initCoreActivity(p_34842_);
        initIdleActivity(p_34842_);
        initFightActivity(p_34841_, p_34842_);
        initCelebrateActivity(p_34842_);
        initRetreatActivity(p_34842_);
        p_34842_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_34842_.setDefaultActivity(Activity.IDLE);
        p_34842_.useDefaultActivity();
        return p_34842_;
    }

    public static void initMemories(AgressivePiglin p_219206_, RandomSource p_219207_) {
    }

    private static void initCoreActivity(Brain<AgressivePiglin> p_34821_) {
        p_34821_.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        InteractWithDoor.create(),
                        babyAvoidNemesis(),
                        StartCelebratingIfTargetDead.create(300, RevampedPiglinAi::wantsToDance),
                        StopBeingAngryIfTargetDead.create()
                )
        );
    }

    private static void initIdleActivity(Brain<AgressivePiglin> p_34892_) {
        p_34892_.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        SetEntityLookTarget.create(RevampedPiglinAi::isPlayerHoldingLovedItem, 14.0F),
                        StartAttacking.<AgressivePiglin>create(RevampedPiglinAi::findNearestValidAttackTarget),
                        createIdleLookBehaviors(),
                        createIdleMovementBehaviors(),
                        SetLookAndInteract.create(EntityType.PLAYER, 4)
                )
        );
    }

    private static void initFightActivity(AgressivePiglin p_34904_, Brain<AgressivePiglin> p_34905_) {
        p_34905_.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.<net.minecraft.world.entity.ai.behavior.BehaviorControl<? super AgressivePiglin>>of(
                        StopAttackingIfTargetInvalid.<AgressivePiglin>create((p_375910_, p_375911_) -> !isNearestValidAttackTarget(p_375910_, p_34904_, p_375911_)),
                        BehaviorBuilder.triggerIf(RevampedPiglinAi::hasCrossbow, BackUpIfTooClose.create(5, 0.75F)),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        new SpearApproach((double) 1.0F, 10.0F), new SpearAttack((double) 1.0F, (double) 1.0F, 10.0F, 2.0F), new SpearRetreat((double) 1.0F),
                        MeleeAttack.create(20),
                        new CrossbowAttack<>(),
                        EraseMemoryIf.create(RevampedPiglinAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static void initCelebrateActivity(Brain<AgressivePiglin> p_34921_) {
        p_34921_.addActivityAndRemoveMemoryWhenStopped(
                Activity.CELEBRATE,
                10,
                ImmutableList.<net.minecraft.world.entity.ai.behavior.BehaviorControl<? super AgressivePiglin>>of(
                        avoidRepellent(),
                        SetEntityLookTarget.create(RevampedPiglinAi::isPlayerHoldingLovedItem, 14.0F),
                        StartAttacking.<AgressivePiglin>create((p_412930_, p_412931_) -> p_412931_.isAdult(), RevampedPiglinAi::findNearestValidAttackTarget),
                        //BehaviorBuilder.triggerIf(p_34804_ -> !p_34804_.isDancing(), GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 2, 1.0F)),
                        BehaviorBuilder.triggerIf(AgressivePiglin::isDancing, GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 4, 0.6F)),
                        new RunOne<AgressivePiglin>(
                                ImmutableList.of(
                                        Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), 1),
                                        Pair.of(RandomStroll.stroll(0.6F, 2, 1), 1),
                                        Pair.of(new DoNothing(10, 20), 1)
                                )
                        )
                ),
                MemoryModuleType.CELEBRATE_LOCATION
        );
    }

    private static void initRetreatActivity(Brain<AgressivePiglin> p_34959_) {
        p_34959_.addActivityAndRemoveMemoryWhenStopped(
                Activity.AVOID,
                10,
                ImmutableList.of(
                        SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true),
                        createIdleLookBehaviors(),
                        createIdleMovementBehaviors(),
                        EraseMemoryIf.create(RevampedPiglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)
                ),
                MemoryModuleType.AVOID_TARGET
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

    private static RunOne<AgressivePiglin> createIdleMovementBehaviors() {
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

    private static BehaviorControl<PathfinderMob> avoidRepellent() {
        return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false);
    }

    private static BehaviorControl<AgressivePiglin> babyAvoidNemesis() {
        return CopyMemoryWithExpiry.create(AgressivePiglin::isBaby, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, BABY_AVOID_NEMESIS_DURATION);
    }

    public static void updateActivity(AgressivePiglin p_34899_) {
        Brain<AgressivePiglin> brain = p_34899_.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse(null);
        brain.setActiveActivityToFirstValid(
                ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.IDLE)
        );
        Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
        if (activity != activity1) {
            getSoundForCurrentActivity(p_34899_).ifPresent(p_34899_::makeSound);
        }

        p_34899_.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
        if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION)) {
            brain.eraseMemory(MemoryModuleType.DANCING);
        }

        p_34899_.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
    }

    private static boolean isBabyRidingBaby(AgressivePiglin p_34993_) {
        if (!p_34993_.isBaby()) {
            return false;
        } else {
            Entity entity = p_34993_.getVehicle();
            return entity instanceof Piglin && ((Piglin) entity).isBaby() || entity instanceof Hoglin && ((Hoglin) entity).isBaby();
        }
    }

    private static ItemStack removeOneItemFromItemEntity(ItemEntity p_34823_) {
        ItemStack itemstack = p_34823_.getItem();
        ItemStack itemstack1 = itemstack.split(1);
        if (itemstack.isEmpty()) {
            p_34823_.discard();
        } else {
            p_34823_.setItem(itemstack);
        }

        return itemstack1;
    }


    private static boolean wantsToDance(LivingEntity p_34811_, LivingEntity p_34812_) {
        return RandomSource.create(p_34811_.level().getGameTime()).nextBoolean();
    }

    protected static boolean isLovedItem(ItemStack p_149966_) {
        return p_149966_.is(ItemTags.PIGLIN_LOVED);
    }

    private static boolean wantsToStopRiding(AgressivePiglin p_34835_, Entity p_34836_) {
        return p_34836_ instanceof Mob mob && (!mob.isBaby() || !mob.isAlive() || wasHurtRecently(p_34835_) || wasHurtRecently(mob) || mob instanceof Piglin && mob.getVehicle() == null);
    }

    private static boolean isNearestValidAttackTarget(ServerLevel serverLevel, AgressivePiglin p_34901_, LivingEntity p_34902_) {
        return findNearestValidAttackTarget(serverLevel, p_34901_).filter(p_34887_ -> p_34887_ == p_34902_).isPresent();
    }

    private static boolean isNearZombified(AgressivePiglin p_34999_) {
        Brain<AgressivePiglin> brain = p_34999_.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
            LivingEntity livingentity = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
            return p_34999_.closerThan(livingentity, 6.0);
        } else {
            return false;
        }
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel serverLevel, AgressivePiglin p_35001_) {
        Brain<AgressivePiglin> brain = p_35001_.getBrain();
        if (isNearZombified(p_35001_)) {
            return Optional.empty();
        } else {
            Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(p_35001_, MemoryModuleType.ANGRY_AT);
            if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(serverLevel, p_35001_, optional.get())) {
                return optional;
            } else {
                Optional<Player> optional1 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
                if (optional1.isPresent()) {
                    return optional1;
                }


                Optional<Mob> optional3 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
                if (optional3.isPresent()) {
                    return optional3;
                }
            }
        }
        return Optional.empty();
    }

    public static void angerNearbyPiglins(ServerLevel serverLevel, Player p_34874_, boolean p_34875_) {
        List<AgressivePiglin> list = p_34874_.level().getEntitiesOfClass(AgressivePiglin.class, p_34874_.getBoundingBox().inflate(16.0));
        list.stream().filter(RevampedPiglinAi::isIdle).filter(p_34881_ -> !p_34875_ || BehaviorUtils.canSee(p_34881_, p_34874_)).forEach(p_352819_ -> {
            if (serverLevel.getGameRules().get(GameRules.UNIVERSAL_ANGER)) {
                setAngerTargetToNearestTargetablePlayerIfFound(serverLevel, p_352819_, p_34874_);
            } else {
                setAngerTarget(serverLevel, p_352819_, p_34874_);
            }
        });
    }

    public static void wasHurtBy(ServerLevel serverLevel, AgressivePiglin p_34838_, LivingEntity p_34839_) {
        if (!(p_34839_ instanceof AgressivePiglin)) {

            Brain<AgressivePiglin> brain = p_34838_.getBrain();
            brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
            brain.eraseMemory(MemoryModuleType.DANCING);

            getAvoidTarget(p_34838_).ifPresent(p_348319_ -> {
                if (p_348319_.getType() != p_34839_.getType()) {
                    brain.eraseMemory(MemoryModuleType.AVOID_TARGET);
                }
            });
            if (p_34838_.isBaby()) {
                brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, p_34839_, 100L);
                if (Sensor.isEntityAttackableIgnoringLineOfSight(serverLevel, p_34838_, p_34839_)) {
                    broadcastAngerTarget(serverLevel, p_34838_, p_34839_);
                }
            } else {
                maybeRetaliate(serverLevel, p_34838_, p_34839_);
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

    public static Optional<SoundEvent> getSoundForCurrentActivity(AgressivePiglin p_34948_) {
        return p_34948_.getBrain().getActiveNonCoreActivity().map(p_34908_ -> getSoundForActivity(p_34948_, p_34908_));
    }

    private static SoundEvent getSoundForActivity(AgressivePiglin p_34855_, Activity p_34856_) {
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

    private static boolean isNearAvoidTarget(AgressivePiglin p_35003_) {
        Brain<AgressivePiglin> brain = p_35003_.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET) && brain.getMemory(MemoryModuleType.AVOID_TARGET).get().closerThan(p_35003_, 12.0);
    }

    protected static List<AbstractPiglin> getVisibleAdultPiglins(AgressivePiglin p_35005_) {
        return p_35005_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    private static List<AbstractPiglin> getAdultPiglins(AbstractPiglin p_34961_) {
        return p_34961_.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    private static void stopWalking(AgressivePiglin p_35007_) {
        p_35007_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_35007_.getNavigation().stop();
    }

    protected static void broadcastAngerTarget(ServerLevel serverLevel, AbstractPiglin p_34896_, LivingEntity p_34897_) {
        getAdultPiglins(p_34896_).forEach(p_348314_ -> {
            if (p_34897_.getType() != EntityType.HOGLIN) {
                setAngerTargetIfCloserThanCurrent(serverLevel, p_348314_, p_34897_);
            }
        });
    }

    protected static void broadcastUniversalAnger(ServerLevel serverLevel, AbstractPiglin p_34825_) {
        getAdultPiglins(p_34825_).forEach(p_34991_ -> getNearestVisibleTargetablePlayer(p_34991_).ifPresent(p_149964_ -> setAngerTarget(serverLevel, p_34991_, p_149964_)));
    }

    protected static void setAngerTarget(ServerLevel serverLevel, AbstractPiglin p_34925_, LivingEntity p_34926_) {
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

    public static Optional<LivingEntity> getAvoidTarget(AgressivePiglin p_34987_) {
        return p_34987_.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)
                ? p_34987_.getBrain().getMemory(MemoryModuleType.AVOID_TARGET)
                : Optional.empty();
    }

    public static Optional<Player> getNearestVisibleTargetablePlayer(AbstractPiglin p_34894_) {
        return p_34894_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)
                ? p_34894_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)
                : Optional.empty();
    }

    private static void broadcastRetreat(AgressivePiglin p_34930_, LivingEntity p_34931_) {
        getVisibleAdultPiglins(p_34930_)
                .stream()
                .filter(p_34985_ -> p_34985_ instanceof Piglin)
                .forEach(p_34819_ -> retreatFromNearestTarget((AgressivePiglin) p_34819_, p_34931_));
    }

    private static void retreatFromNearestTarget(AgressivePiglin p_34950_, LivingEntity p_34951_) {
        Brain<AgressivePiglin> brain = p_34950_.getBrain();
        LivingEntity $$3 = BehaviorUtils.getNearestTarget(p_34950_, brain.getMemory(MemoryModuleType.AVOID_TARGET), p_34951_);
        $$3 = BehaviorUtils.getNearestTarget(p_34950_, brain.getMemory(MemoryModuleType.ATTACK_TARGET), $$3);
        setAvoidTargetAndDontHuntForAWhile(p_34950_, $$3);
    }

    public static boolean wantsToStopFleeing(AgressivePiglin p_35009_) {
        Brain<AgressivePiglin> brain = p_35009_.getBrain();
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

    private static boolean piglinsEqualOrOutnumberHoglins(AgressivePiglin p_35011_) {
        return !hoglinsOutnumberPiglins(p_35011_);
    }

    private static boolean hoglinsOutnumberPiglins(AgressivePiglin p_35013_) {
        int i = p_35013_.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(0) + 1;
        int j = p_35013_.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(0);
        return j > i;
    }

    private static void setAvoidTargetAndDontHuntForAWhile(AgressivePiglin p_34968_, LivingEntity p_34969_) {
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_34968_.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, p_34969_, RETREAT_DURATION.sample(p_34968_.level().random));
    }


    private static Vec3 getRandomNearbyPos(AgressivePiglin p_35017_) {
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

    private static boolean isNearRepellent(AgressivePiglin p_35023_) {
        return p_35023_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
    }


    public static boolean isPlayerHoldingLovedItem(LivingEntity p_34884_) {
        return p_34884_.getType() == EntityType.PLAYER && p_34884_.isHolding(RevampedPiglinAi::isLovedItem);
    }

    private static boolean isAdmiringDisabled(AgressivePiglin p_35025_) {
        return p_35025_.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }

    private static boolean wasHurtRecently(LivingEntity p_34989_) {
        return p_34989_.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }

    private static boolean isHoldingItemInOffHand(AgressivePiglin p_35027_) {
        return !p_35027_.getOffhandItem().isEmpty();
    }

    private static boolean isNotHoldingLovedItemInOffHand(AgressivePiglin p_35029_) {
        return p_35029_.getOffhandItem().isEmpty() || !isLovedItem(p_35029_.getOffhandItem());
    }

    public static boolean isZombified(EntityType<?> p_34807_) {
        return p_34807_ == EntityType.ZOMBIFIED_PIGLIN || p_34807_ == EntityType.ZOGLIN;
    }
}
