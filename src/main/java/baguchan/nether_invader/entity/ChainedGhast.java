package baguchan.nether_invader.entity;

import baguchan.nether_invader.registry.ModEntities;
import baguchan.nether_invader.registry.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ChainedGhast extends Ghast {
    private boolean hasLeash;
    @Nullable
    public BlockPos targetPos;

    public ChainedGhast(EntityType<? extends ChainedGhast> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new GhastMoveControl(this);
        this.lookControl = new ChainedGhastLookControl();
    }

    @Override
    public void addAdditionalSaveData(ValueOutput p_32744_) {
        super.addAdditionalSaveData(p_32744_);
        p_32744_.putBoolean("hasLeash", this.hasLeash);
        if (this.targetPos != null) {
            p_32744_.store("TargetPos", BlockPos.CODEC, this.targetPos);
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput p_32733_) {
        super.readAdditionalSaveData(p_32733_);
        this.hasLeash = p_32733_.getBooleanOr("hasLeash", false);
        this.targetPos = p_32733_.read("TargetPos", BlockPos.CODEC).orElse(null);

    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TargetFloatAroundGoal(this, 16));
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this, 16));
        this.goalSelector.addGoal(7, new GhastLookGoal(this));
        this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
        this.targetSelector
                .addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (p_352811_, level) -> Math.abs(p_352811_.getY() - this.getY()) <= 8.0));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 100.0);
    }

    @Override
    protected PathNavigation createNavigation(Level p_218342_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_218342_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        return flyingpathnavigation;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34297_, DifficultyInstance p_34298_, EntitySpawnReason p_34299_, @Nullable SpawnGroupData p_34300_) {
        RandomSource randomsource = p_34297_.getRandom();
        p_34300_ = super.finalizeSpawn(p_34297_, p_34298_, p_34299_, p_34300_);


        if ((double) randomsource.nextFloat() < 1F && p_34299_ == EntitySpawnReason.SPAWN_ITEM_USE) {
            Scaffolding scaffolding = ModEntities.SCAFFOLDING.get().create(this.level(), EntitySpawnReason.EVENT);
            AgressivePiglin piglin = ModEntities.AGRESSIVE_PIGLIN.get().create(this.level(), EntitySpawnReason.EVENT);
            if (scaffolding != null && piglin != null) {
                scaffolding.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                piglin.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                piglin.finalizeSpawn(p_34297_, p_34298_, EntitySpawnReason.JOCKEY, null);
                piglin.setBaby(false);
                piglin.setItemSlot(EquipmentSlot.MAINHAND, Items.CROSSBOW.getDefaultInstance());

                piglin.addEffect(new MobEffectInstance(ModPotions.AWKWARD, 120000));

                piglin.startRiding(scaffolding);
                p_34297_.addFreshEntityWithPassengers(scaffolding);
                scaffolding.setChainedTo(this, true);

                this.hasLeash = true;
            }
        }
        return p_34300_;
    }

    @Override
    public boolean supportQuadLeashAsHolder() {
        return true;
    }

    @Override
    public double leashElasticDistance() {
        return (double) 10.0F;
    }

    @Override
    public double leashSnapDistance() {
        return (double) 16.0F;
    }


    public static boolean checkChainGhastSpawnRules(
            EntityType<ChainedGhast> p_218985_, LevelAccessor p_218986_, EntitySpawnReason p_218987_, BlockPos p_218988_, RandomSource p_218989_
    ) {
        return p_218986_.getDifficulty() != Difficulty.PEACEFUL
                && p_218989_.nextInt(20) == 0
                && checkMobSpawnRules(p_218985_, p_218986_, p_218987_, p_218988_, p_218989_);
    }

    static class GhastMoveControl extends MoveControl {
        private final ChainedGhast ghast;
        private int floatDuration;

        public GhastMoveControl(ChainedGhast p_32768_) {
            super(p_32768_);
            this.ghast = p_32768_;
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration = this.floatDuration + this.ghast.getRandom().nextInt(5) + 2;
                    Vec3 vec3 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                    double d0 = vec3.length();
                    vec3 = vec3.normalize();
                    if (this.canReach(vec3, Mth.ceil(d0))) {
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3.scale(0.1 * this.speedModifier)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 p_32771_, int p_32772_) {
            AABB aabb = this.ghast.getBoundingBox();

            //need riding entity space
            for (int i = 1; i < p_32772_; i++) {
                aabb = aabb.move(p_32771_);
                if (!this.ghast.level().noCollision(this.ghast, aabb) || this.ghast.level().containsAnyLiquid(aabb)) {
                    return false;
                }
            }

            return true;
        }

        private boolean canReachWithPassenger(Vec3 p_32771_, int p_32772_) {
            AABB aabb = this.ghast.getBoundingBox().expandTowards(0, -6, 0);

            //need riding entity space
            for (int i = 1; i < p_32772_; i++) {
                aabb = aabb.move(p_32771_);
                if (!this.ghast.level().noCollision(this.ghast, aabb) || this.ghast.level().containsAnyLiquid(aabb)) {
                    return false;
                }
            }

            return true;
        }
    }

    static class GhastLookGoal extends Goal {
        private final Ghast ghast;

        public GhastLookGoal(Ghast p_32762_) {
            this.ghast = p_32762_;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vec3 vec3 = this.ghast.getDeltaMovement();
                this.ghast.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180.0F / (float) Math.PI));
                this.ghast.yBodyRot = this.ghast.getYRot();
            } else {
                LivingEntity livingentity = this.ghast.getTarget();
                double d0 = 64.0;
                if (livingentity.distanceToSqr(this.ghast) < 4096.0) {
                    double d1 = livingentity.getX() - this.ghast.getX();
                    double d2 = livingentity.getZ() - this.ghast.getZ();
                    this.ghast.setYRot(-((float) Mth.atan2(d1, d2)) * (180.0F / (float) Math.PI));
                    this.ghast.yBodyRot = this.ghast.getYRot();
                }
            }
        }
    }

    static class GhastShootFireballGoal extends Goal {
        private final Ghast ghast;
        public int chargeTime;

        public GhastShootFireballGoal(Ghast p_32776_) {
            this.ghast = p_32776_;
        }

        @Override
        public boolean canUse() {
            return this.ghast.getTarget() != null;
        }

        @Override
        public void start() {
            this.chargeTime = 0;
        }

        @Override
        public void stop() {
            this.ghast.setCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.ghast.getTarget();
            if (livingentity != null) {
                double d0 = 64.0;
                if (livingentity.distanceToSqr(this.ghast) < 4096.0 && this.ghast.hasLineOfSight(livingentity)) {
                    Level level = this.ghast.level();
                    this.chargeTime++;
                    if (this.chargeTime == 10 && !this.ghast.isSilent()) {
                        level.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
                    }

                    if (this.chargeTime == 20) {
                        double d1 = 4.0;
                        Vec3 vec3 = this.ghast.getLookAngle();
                        double d2 = livingentity.getX() - (this.ghast.getX() + vec3.x * 4.0);
                        double d3 = livingentity.getY(0.5) - (0.5 + this.ghast.getY(0.5));
                        double d4 = livingentity.getZ() - (this.ghast.getZ() + vec3.z * 4.0);
                        Vec3 vec31 = new Vec3(d2, d3, d4);
                        if (!this.ghast.isSilent()) {
                            level.levelEvent(null, 1016, this.ghast.blockPosition(), 0);
                        }

                        LargeFireball largefireball = new LargeFireball(level, this.ghast, vec31.normalize(), this.ghast.getExplosionPower());
                        largefireball.setPos(this.ghast.getX() + vec3.x * 4.0, this.ghast.getY(0.5) + 0.5, largefireball.getZ() + vec3.z * 4.0);
                        level.addFreshEntity(largefireball);
                        this.chargeTime = -40;
                    }
                } else if (this.chargeTime > 0) {
                    this.chargeTime--;
                }

                this.ghast.setCharging(this.chargeTime > 10);
            }
        }
    }

    public static class TargetFloatAroundGoal extends Goal {
        private static final int MAX_ATTEMPTS = 64;
        private final ChainedGhast ghast;
        private final int distanceToBlocks;

        public TargetFloatAroundGoal(ChainedGhast mob) {
            this(mob, 0);
        }

        public TargetFloatAroundGoal(ChainedGhast mob, int i) {
            this.ghast = mob;
            this.distanceToBlocks = i;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (this.ghast.targetPos == null) {
                return false;
            }

            MoveControl moveControl = this.ghast.getMoveControl();
            if (!moveControl.hasWanted()) {
                return true;
            } else {
                double d = moveControl.getWantedX() - this.ghast.getX();
                double e = moveControl.getWantedY() - this.ghast.getY();
                double f = moveControl.getWantedZ() - this.ghast.getZ();
                double g = d * d + e * e + f * f;
                return g < (double) 1.0F || g > (double) 3600.0F;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            Vec3 vec3 = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
            this.ghast.getMoveControl().setWantedPosition(vec3.x(), vec3.y(), vec3.z(), (double) 0.5F);
        }

        public static Vec3 getSuitableFlyToPosition(ChainedGhast mob, int i) {
            Level level = mob.level();
            RandomSource randomSource = mob.getRandom();
            Vec3 vec3 = mob.position();
            Vec3 vec32 = null;

            for (int j = 0; j < 64; ++j) {
                vec32 = chooseRandomPositionWithRestriction(mob, vec3, randomSource);
                if (vec32 != null && isGoodTarget(level, vec32, i)) {
                    return vec32;
                }
            }

            if (vec32 == null) {
                vec32 = chooseRandomPosition(mob, vec3, randomSource);
            }

            BlockPos blockPos = BlockPos.containing(vec32);
            int k = level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ());
            if (k < blockPos.getY() && k > level.getMinY()) {
                vec32 = new Vec3(vec32.x(), mob.getY() - Math.abs(mob.getY() - vec32.y()), vec32.z());
            }

            return vec32;
        }

        private static boolean isGoodTarget(Level level, Vec3 vec3, int i) {
            if (i <= 0) {
                return true;
            } else {
                BlockPos blockPos = BlockPos.containing(vec3);
                if (!level.getBlockState(blockPos).isAir()) {
                    return false;
                } else {
                    for (Direction direction : Direction.values()) {
                        for (int j = 1; j < i; ++j) {
                            BlockPos blockPos2 = blockPos.relative(direction, j);
                            if (!level.getBlockState(blockPos2).isAir()) {
                                return true;
                            }
                        }
                    }

                    return false;
                }
            }
        }

        private static Vec3 chooseRandomPosition(ChainedGhast mob, Vec3 vec3, RandomSource randomSource) {
            double d = mob.targetPos.getX() + (double) ((randomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double e = mob.targetPos.getY() + (double) ((randomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double f = mob.targetPos.getZ() + (double) ((randomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            return new Vec3(d, e, f);
        }

        @org.jetbrains.annotations.Nullable
        private static Vec3 chooseRandomPositionWithRestriction(ChainedGhast mob, Vec3 vec3, RandomSource randomSource) {
            Vec3 vec32 = chooseRandomPosition(mob, vec3, randomSource);
            return mob.hasHome() && !mob.isWithinHome(BlockPos.containing(vec32)) ? null : vec32;
        }
    }

    public static class RandomFloatAroundGoal extends Goal {
        private static final int MAX_ATTEMPTS = 64;
        private final Mob ghast;
        private final int distanceToBlocks;

        public RandomFloatAroundGoal(Mob mob) {
            this(mob, 0);
        }

        public RandomFloatAroundGoal(Mob mob, int i) {
            this.ghast = mob;
            this.distanceToBlocks = i;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl moveControl = this.ghast.getMoveControl();
            if (!moveControl.hasWanted()) {
                return true;
            } else {
                double d = moveControl.getWantedX() - this.ghast.getX();
                double e = moveControl.getWantedY() - this.ghast.getY();
                double f = moveControl.getWantedZ() - this.ghast.getZ();
                double g = d * d + e * e + f * f;
                return g < (double) 1.0F || g > (double) 3600.0F;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            Vec3 vec3 = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
            this.ghast.getMoveControl().setWantedPosition(vec3.x(), vec3.y(), vec3.z(), (double) 1.0F);
        }

        public static Vec3 getSuitableFlyToPosition(Mob mob, int i) {
            Level level = mob.level();
            RandomSource randomSource = mob.getRandom();
            Vec3 vec3 = mob.position();
            Vec3 vec32 = null;

            for (int j = 0; j < 64; ++j) {
                vec32 = chooseRandomPositionWithRestriction(mob, vec3, randomSource);
                if (vec32 != null && isGoodTarget(level, vec32, i)) {
                    return vec32;
                }
            }

            if (vec32 == null) {
                vec32 = chooseRandomPosition(vec3, randomSource);
            }

            BlockPos blockPos = BlockPos.containing(vec32);
            int k = level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ());
            if (k < blockPos.getY() && k > level.getMinY()) {
                vec32 = new Vec3(vec32.x(), mob.getY() - Math.abs(mob.getY() - vec32.y()), vec32.z());
            }

            return vec32;
        }

        private static boolean isGoodTarget(Level level, Vec3 vec3, int i) {
            if (i <= 0) {
                return true;
            } else {
                BlockPos blockPos = BlockPos.containing(vec3);
                if (!level.getBlockState(blockPos).isAir()) {
                    return false;
                } else {
                    for (Direction direction : Direction.values()) {
                        for (int j = 1; j < i; ++j) {
                            BlockPos blockPos2 = blockPos.relative(direction, j);
                            if (!level.getBlockState(blockPos2).isAir()) {
                                return true;
                            }
                        }
                    }

                    return false;
                }
            }
        }

        private static Vec3 chooseRandomPosition(Vec3 vec3, RandomSource randomSource) {
            double d = vec3.x() + (double) ((randomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double e = vec3.y() + (double) ((randomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double f = vec3.z() + (double) ((randomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            return new Vec3(d, e, f);
        }

        @org.jetbrains.annotations.Nullable
        private static Vec3 chooseRandomPositionWithRestriction(Mob mob, Vec3 vec3, RandomSource randomSource) {
            Vec3 vec32 = chooseRandomPosition(vec3, randomSource);
            return mob.hasHome() && !mob.isWithinHome(BlockPos.containing(vec32)) ? null : vec32;
        }
    }

    class ChainedGhastLookControl extends LookControl {
        ChainedGhastLookControl() {
            super(ChainedGhast.this);
        }

        public void tick() {
            if (this.lookAtCooldown > 0) {
                --this.lookAtCooldown;
                double d = this.wantedX - ChainedGhast.this.getX();
                double e = this.wantedZ - ChainedGhast.this.getZ();
                ChainedGhast.this.setYRot(-((float) Mth.atan2(d, e)) * (180F / (float) Math.PI));
                ChainedGhast.this.yBodyRot = ChainedGhast.this.getYRot();
                ChainedGhast.this.yHeadRot = ChainedGhast.this.yBodyRot;
            } else {
                ChainedGhast.faceMovementDirection(this.mob);
            }
        }

        public static float wrapDegrees90(float f) {
            float g = f % 90.0F;
            if (g >= 45.0F) {
                g -= 90.0F;
            }

            if (g < -45.0F) {
                g += 90.0F;
            }

            return g;
        }
    }

    public static void faceMovementDirection(Mob mob) {
        if (mob.getTarget() == null) {
            Vec3 vec3 = mob.getDeltaMovement();
            mob.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float) Math.PI));
            mob.yBodyRot = mob.getYRot();
        } else {
            LivingEntity livingEntity = mob.getTarget();
            double d = (double) 64.0F;
            if (livingEntity.distanceToSqr(mob) < (double) 4096.0F) {
                double e = livingEntity.getX() - mob.getX();
                double f = livingEntity.getZ() - mob.getZ();
                mob.setYRot(-((float) Mth.atan2(e, f)) * (180F / (float) Math.PI));
                mob.yBodyRot = mob.getYRot();
            }
        }

    }
}
