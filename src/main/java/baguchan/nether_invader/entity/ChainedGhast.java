package baguchan.nether_invader.entity;

import baguchan.nether_invader.registry.ModEntitys;
import baguchan.nether_invader.registry.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ChainedGhast extends Ghast {
    private boolean hasLeash;

    public ChainedGhast(EntityType<? extends ChainedGhast> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new GhastMoveControl(this);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_32744_) {
        super.addAdditionalSaveData(p_32744_);
        p_32744_.putBoolean("hasLeash", this.hasLeash);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_32733_) {
        super.readAdditionalSaveData(p_32733_);
        this.hasLeash = p_32733_.getBoolean("hasLeash");
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new GhastLookGoal(this));
        this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
        this.targetSelector
                .addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, p_352811_ -> Math.abs(p_352811_.getY() - this.getY()) <= 4.0));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 100.0);
    }

    @Override
    protected PathNavigation createNavigation(Level p_218342_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_218342_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34297_, DifficultyInstance p_34298_, MobSpawnType p_34299_, @Nullable SpawnGroupData p_34300_) {
        RandomSource randomsource = p_34297_.getRandom();
        p_34300_ = super.finalizeSpawn(p_34297_, p_34298_, p_34299_, p_34300_);


        if ((double) randomsource.nextFloat() < 1F) {
            Scaffolding scaffolding = ModEntitys.SCAFFOLDING.get().create(this.level());
            AgressivePiglin piglin = ModEntitys.AGRESSIVE_PIGLIN.get().create(this.level());
            if (scaffolding != null && piglin != null) {
                scaffolding.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                piglin.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                piglin.finalizeSpawn(p_34297_, p_34298_, MobSpawnType.JOCKEY, null);
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

    public static boolean checkChainGhastSpawnRules(
            EntityType<ChainedGhast> p_218985_, LevelAccessor p_218986_, MobSpawnType p_218987_, BlockPos p_218988_, RandomSource p_218989_
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
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3.scale(0.1)));
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
                        Vec3 vec3 = this.ghast.getViewVector(1.0F);
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

    static class RandomFloatAroundGoal extends Goal {
        private final ChainedGhast ghast;

        public RandomFloatAroundGoal(ChainedGhast p_32783_) {
            this.ghast = p_32783_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movecontrol = this.ghast.getMoveControl();
            if (!movecontrol.hasWanted()) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.ghast.getX();
                double d1 = movecontrol.getWantedY() - this.ghast.getY();
                double d2 = movecontrol.getWantedZ() - this.ghast.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource randomsource = this.ghast.getRandom();
            double d0 = this.ghast.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.ghast.getY() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.ghast.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);

            this.ghast.moveControl.setWantedPosition(d0, d1, d2, 1.0);
        }
    }
}
