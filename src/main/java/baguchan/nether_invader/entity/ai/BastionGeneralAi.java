package baguchan.nether_invader.entity.ai;

import baguchan.nether_invader.entity.BastionGeneral;
import baguchan.nether_invader.entity.behavior.GeneralAttack;
import baguchan.nether_invader.entity.behavior.PiglinRaiding;
import baguchan.nether_invader.registry.ModMemoryModuleType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.ActivityData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class BastionGeneralAi {
    public static final int PLAYER_ANGER_RANGE = 35;
    protected static final UniformInt TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);

    private static final UniformInt RIDE_START_INTERVAL = TimeUtil.rangeOfSeconds(10, 40);
    private static final UniformInt RIDE_DURATION = TimeUtil.rangeOfSeconds(10, 30);
    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
    private static final UniformInt AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
    private static final UniformInt BABY_AVOID_NEMESIS_DURATION = TimeUtil.rangeOfSeconds(5, 7);

    public static List<ActivityData<BastionGeneral>> getActivities(BastionGeneral piglin) {
        return List.of(initCoreActivity(), initIdleActivity(), initFightActivity(piglin));
    }

    public static void initMemories(BastionGeneral body) {
    }


    private static ActivityData<BastionGeneral> initCoreActivity() {
        return ActivityData.create(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink(),
                        InteractWithDoor.create(),
                        StopBeingAngryIfTargetDead.create()
                )
        );
    }

    private static ActivityData<BastionGeneral> initIdleActivity() {
        return ActivityData.create(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        SetEntityLookTarget.create(BastionGeneralAi::isPlayerHoldingLovedItem, 14.0F),
                        StartAttacking.<BastionGeneral>create(BastionGeneralAi::findNearestValidAttackTarget),
                        createIdleLookBehaviors(),
                        createIdleMovementBehaviors(),
                        SetLookAndInteract.create(EntityType.PLAYER, 4)
                )
        );
    }

    private static ActivityData<BastionGeneral> initFightActivity(BastionGeneral bastionGeneral) {
        return ActivityData.create(
                Activity.FIGHT,
                10,
                ImmutableList.<BehaviorControl<? super BastionGeneral>>of(
                        StopAttackingIfTargetInvalid.create((serverLevel, livingEntity) -> !isNearestValidAttackTarget(serverLevel, bastionGeneral, livingEntity)),
                        BehaviorBuilder.triggerIf(BastionGeneralAi::hasCrossbow, BackUpIfTooClose.create(5, 0.75F)),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                        new BastionGeneralAi.RandomSpin(),
                        new GeneralAttack<>(13, 30, 30, 0.8F),
                        EraseMemoryIf.create(BastionGeneralAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)
                ),
                MemoryModuleType.ATTACK_TARGET
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

    private static RunOne<BastionGeneral> createIdleMovementBehaviors() {
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
    public static void updateActivity(BastionGeneral p_34899_) {
        Brain<BastionGeneral> brain = p_34899_.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse(null);
        brain.setActiveActivityToFirstValid(
                ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.IDLE)
        );
        Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
        if (activity != activity1) {
            getSoundForCurrentActivity(p_34899_).ifPresent(p_34899_::makeSound);
        }

        p_34899_.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    protected static boolean isLovedItem(ItemStack p_149966_) {
        return p_149966_.is(ItemTags.PIGLIN_LOVED);
    }


    private static boolean isNearestValidAttackTarget(ServerLevel serverLevel, BastionGeneral p_34901_, LivingEntity p_34902_) {
        return findNearestValidAttackTarget(serverLevel, p_34901_).filter(p_34887_ -> p_34887_ == p_34902_).isPresent();
    }


    private static boolean isNearZombified(BastionGeneral p_34999_) {
        Brain<BastionGeneral> brain = p_34999_.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
            LivingEntity livingentity = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
            return p_34999_.closerThan(livingentity, 6.0);
        } else {
            return false;
        }
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel serverLevel, BastionGeneral p_35001_) {
        Brain<BastionGeneral> brain = p_35001_.getBrain();
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
        return Optional.empty();
    }

    public static void wasHurtBy(ServerLevel serverLevel, BastionGeneral p_34838_, LivingEntity p_34839_) {
        if (!(p_34839_ instanceof AbstractPiglin)) {
            maybeRetaliate(serverLevel, p_34838_, p_34839_);
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

    public static Optional<SoundEvent> getSoundForCurrentActivity(BastionGeneral p_34948_) {
        return p_34948_.getBrain().getActiveNonCoreActivity().map(p_34908_ -> getSoundForActivity(p_34948_, p_34908_));
    }

    private static SoundEvent getSoundForActivity(BastionGeneral p_34855_, Activity p_34856_) {
        if (p_34856_ == Activity.FIGHT) {
            return SoundEvents.PIGLIN_BRUTE_ANGRY;
        } else {
            return SoundEvents.PIGLIN_BRUTE_AMBIENT;
        }
    }

    protected static List<AbstractPiglin> getVisibleAdultPiglins(BastionGeneral p_35005_) {
        return p_35005_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
    }

    private static List<AbstractPiglin> getAdultPiglins(AbstractPiglin p_34961_) {
        return p_34961_.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
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

    public static Optional<LivingEntity> getAvoidTarget(BastionGeneral p_34987_) {
        return p_34987_.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)
                ? p_34987_.getBrain().getMemory(MemoryModuleType.AVOID_TARGET)
                : Optional.empty();
    }

    public static Optional<Player> getNearestVisibleTargetablePlayer(AbstractPiglin p_34894_) {
        return p_34894_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)
                ? p_34894_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)
                : Optional.empty();
    }

    private static void broadcastRetreat(BastionGeneral p_34930_, LivingEntity p_34931_) {
        getVisibleAdultPiglins(p_34930_)
                .stream()
                .filter(p_34985_ -> p_34985_ instanceof Piglin)
                .forEach(p_34819_ -> retreatFromNearestTarget((BastionGeneral) p_34819_, p_34931_));
    }

    private static void retreatFromNearestTarget(BastionGeneral p_34950_, LivingEntity p_34951_) {
        Brain<BastionGeneral> brain = p_34950_.getBrain();
        LivingEntity $$3 = BehaviorUtils.getNearestTarget(p_34950_, brain.getMemory(MemoryModuleType.AVOID_TARGET), p_34951_);
        $$3 = BehaviorUtils.getNearestTarget(p_34950_, brain.getMemory(MemoryModuleType.ATTACK_TARGET), $$3);
        setAvoidTargetAndDontHuntForAWhile(p_34950_, $$3);
    }

    public static boolean wantsToStopFleeing(BastionGeneral p_35009_) {
        Brain<BastionGeneral> brain = p_35009_.getBrain();
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

    private static boolean piglinsEqualOrOutnumberHoglins(BastionGeneral p_35011_) {
        return !hoglinsOutnumberPiglins(p_35011_);
    }

    private static boolean hoglinsOutnumberPiglins(BastionGeneral p_35013_) {
        int i = p_35013_.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(0) + 1;
        int j = p_35013_.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(0);
        return j > i;
    }

    private static void setAvoidTargetAndDontHuntForAWhile(BastionGeneral p_34968_, LivingEntity p_34969_) {
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_34968_.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, p_34969_, RETREAT_DURATION.sample(p_34968_.level().getRandom()));
    }


    private static Vec3 getRandomNearbyPos(BastionGeneral p_35017_) {
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

    private static boolean isNearRepellent(BastionGeneral p_35023_) {
        return p_35023_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
    }


    public static boolean isPlayerHoldingLovedItem(LivingEntity p_34884_) {
        return p_34884_.getType() == EntityType.PLAYER && p_34884_.isHolding(BastionGeneralAi::isLovedItem);
    }


    public static boolean isZombified(EntityType<?> p_34807_) {
        return p_34807_ == EntityType.ZOMBIFIED_PIGLIN || p_34807_ == EntityType.ZOGLIN;
    }

    public static class RandomSpin extends Behavior<BastionGeneral> {
        private long lastSpinTick;

        public RandomSpin() {
            super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, ModMemoryModuleType.NEAREST_VISIBLE_ENEMY.get(), MemoryStatus.VALUE_PRESENT));
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, BastionGeneral body) {
            return body.isSpinAttack() || !BastionGeneralAi.hasCrossbow(body);
        }

        @Override
        protected void start(ServerLevel level, BastionGeneral body, long timestamp) {
            Optional<List<LivingEntity>> list = body.getBrain().getMemory(ModMemoryModuleType.NEAREST_VISIBLE_ENEMY.get());


            if (body.isSpinAttack() && !body.isInWater() && level.getGameTime() - 200 >= this.lastSpinTick) {
                this.lastSpinTick = level.getGameTime();
                body.stopSpin();
            } else if (list.isPresent() && list.get().size() > 2 && !body.hasControllingPassenger() && level.getGameTime() - 600 >= this.lastSpinTick) {
                this.lastSpinTick = level.getGameTime();
                body.startSpin();
            }
        }
    }
}
