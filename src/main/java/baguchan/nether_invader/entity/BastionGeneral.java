package baguchan.nether_invader.entity;

import baguchan.nether_invader.entity.ai.BastionGeneralAi;
import baguchan.nether_invader.registry.ModItems;
import baguchan.nether_invader.registry.ModSensors;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class BastionGeneral extends AbstractPiglin {

    public static final Brain.Provider<BastionGeneral> BRAIN_PROVIDER = Brain.provider(
            List.of(MemoryModuleType.UNIVERSAL_ANGER),
            List.of(baguchi.bagus_lib.register.ModSensors.SMART_NEAREST_LIVING_ENTITY_SENSOR.get(), SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, ModSensors.ANGER_PIGLIN_SENSOR.get()),
            BastionGeneralAi::getActivities
    );

    public static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK = SynchedEntityData.defineId(BastionGeneral.class, EntityDataSerializers.LONG);


    public final AnimationState spinAttackStartAnimationState = new AnimationState();
    public final AnimationState spinAttackPoseAnimationState = new AnimationState();
    public final AnimationState spinAttackStopAnimationState = new AnimationState();

    public final AnimationState attackAnimationState = new AnimationState();
    private final int attackAnimationLength = 34;

    private int attackAnimationTick;


    public BastionGeneral(EntityType<? extends AbstractPiglin> p_34683_, Level p_34684_) {
        super(p_34683_, p_34684_);
        this.xpReward = 30;
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LAST_POSE_CHANGE_TICK, 0L);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_34727_) {
        super.onSyncedDataUpdated(p_34727_);
    }


    @Override
    public void handleEntityEvent(byte p_219360_) {
        if (p_219360_ == 4) {
            this.attackAnimationState.start(this.tickCount);
            this.attackAnimationTick = 0;
        } else {
            super.handleEntityEvent(p_219360_);
        }

    }

    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
        return itemStack.is(ModItems.LAVA_INFUSED_SWORD.asItem()) || itemStack.is(Items.NETHERITE_SWORD.asItem()) ? super.wantsToPickUp(level, itemStack) : false;
    }


    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (this.isVisuallySpin()) {
            this.spinAttackStopAnimationState.stop();
            if (this.isVisuallySpinningStart()) {
                this.spinAttackStartAnimationState.startIfStopped(this.tickCount);
                this.spinAttackPoseAnimationState.stop();
            } else {
                this.spinAttackStartAnimationState.stop();
                this.spinAttackPoseAnimationState.startIfStopped(this.tickCount);
            }
        } else {
            this.spinAttackStartAnimationState.stop();
            this.spinAttackPoseAnimationState.stop();
            this.spinAttackStopAnimationState.animateWhen(this.isInPoseTransition() && this.getPoseTime() >= 0L, this.tickCount);
        }
    }

    public boolean isVisuallySpinningStart() {
        return this.isSpinAttack() && this.getPoseTime() < 13L && this.getPoseTime() >= 0L;
    }

    public boolean isInPoseTransition() {
        long poseTime = this.getPoseTime();
        return poseTime < (this.isSpinAttack() ? 13 : 32);
    }

    public boolean isSpinAttack() {
        return this.entityData.get(LAST_POSE_CHANGE_TICK) < 0L;
    }

    public boolean isVisuallySpin() {
        return this.getPoseTime() < 0L != this.isSpinAttack();
    }

    public long getPoseTime() {
        return this.level().getGameTime() - Math.abs(this.entityData.get(LAST_POSE_CHANGE_TICK));
    }

    @VisibleForTesting
    public void resetLastPoseChangeTick(long syncedPoseTickTime) {
        this.entityData.set(LAST_POSE_CHANGE_TICK, syncedPoseTickTime);
    }

    public void startSpin() {
        if (!this.isSpinAttack()) {
            this.gameEvent(GameEvent.ENTITY_ACTION);
            this.resetLastPoseChangeTick(-this.level().getGameTime());
        }
    }

    public void stopSpin() {
        if (this.isSpinAttack()) {
            this.gameEvent(GameEvent.ENTITY_ACTION);
            this.resetLastPoseChangeTick(this.level().getGameTime());
        }
    }

    @Override
    public boolean removeWhenFarAway(double p_34775_) {
        return this instanceof PiglinRaider piglinRaider && !piglinRaider.netherInvader$hasRaid() && super.removeWhenFarAway(p_34775_);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.level().isClientSide()) {
            if (this.attackAnimationTick < this.attackAnimationLength) {
                this.attackAnimationTick++;
            }
            if (this.attackAnimationTick >= this.attackAnimationLength) {
                this.attackAnimationState.stop();
            }

        }

    }

    @Override
    protected void updateWalkAnimation(float p_382793_) {
        float f = Math.min(p_382793_ * 20.0F, 1.0F);
        this.walkAnimation.update(f, 0.4F, 1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 80.0).add(Attributes.ARMOR, 4.0F).add(Attributes.ARMOR_TOUGHNESS, 3.0F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 20);
    }

    @Override
    public @org.jetbrains.annotations.Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, EntitySpawnReason p_363352_, @org.jetbrains.annotations.Nullable SpawnGroupData p_21437_) {
        RandomSource randomsource = p_21434_.getRandom();

        BastionGeneralAi.initMemories(this);
        this.populateDefaultEquipmentSlots(randomsource, p_21435_);
        this.populateDefaultEquipmentEnchantments(p_21434_, randomsource, p_21435_);
        return super.finalizeSpawn(p_21434_, p_21435_, p_363352_, p_21437_);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource p_219189_, DifficultyInstance p_219190_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.LAVA_INFUSED_SWORD.asItem()));
    }

    @Override
    protected Brain<BastionGeneral> makeBrain(Brain.Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    @Override
    public Brain<BastionGeneral> getBrain() {
        return (Brain<BastionGeneral>) super.getBrain();
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected boolean canHunt() {
        return false;
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        ProfilerFiller profilerfiller = Profiler.get();
        profilerfiller.push("piglinBrain");
        this.getBrain().tick(serverLevel, this);
        profilerfiller.pop();
        BastionGeneralAi.updateActivity(this);
        super.customServerAiStep(serverLevel);
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel p_376894_) {
        return this.xpReward;
    }

    @Override
    protected void finishConversion(ServerLevel p_34756_) {
        super.finishConversion(p_34756_);
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
            return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
        } else {
            return this.isAggressive() && this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem) ? PiglinArmPose.CROSSBOW_HOLD : PiglinArmPose.DEFAULT;
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource p_34694_, float p_34695_) {
        boolean flag = super.hurtServer(serverLevel, p_34694_, p_34695_);
        if (this.level().isClientSide()) {
            return false;
        } else {
            if (flag && p_34694_.getEntity() instanceof LivingEntity) {
                BastionGeneralAi.wasHurtBy(serverLevel, this, (LivingEntity) p_34694_.getEntity());
            }

            return flag;
        }
    }

    @Override
    public boolean startRiding(Entity p_21396_, boolean p_21397_, boolean p_433558_) {
        if (this.isBaby() && p_21396_.getType() == EntityType.HOGLIN) {
            p_21396_ = this.getTopPassenger(p_21396_, 3);
        }
        return super.startRiding(p_21396_, p_21397_, p_433558_);
    }

    private Entity getTopPassenger(Entity p_34731_, int p_34732_) {
        List<Entity> list = p_34731_.getPassengers();
        return p_34732_ != 1 && !list.isEmpty() ? this.getTopPassenger(list.get(0), p_34732_ - 1) : p_34731_;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.level().isClientSide() ? null : BastionGeneralAi.getSoundForCurrentActivity(this).orElse(null);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_34767_) {
        return SoundEvents.PIGLIN_BRUTE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_BRUTE_DEATH;
    }

    //sorry. I want to create diversity, but it was difficult for now...
    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    protected void playStepSound(BlockPos p_34748_, BlockState p_34749_) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    @Override
    protected boolean considersEntityAsAlly(Entity p_360600_) {
        if (super.considersEntityAsAlly(p_360600_)) {
            return true;
        } else {
            return p_360600_ instanceof AbstractPiglin && this.getTeam() == null && p_360600_.getTeam() == null;
        }
    }

    @Override
    public float getSecondsToDisableBlocking() {
        if (this.isSpinAttack()) {
            return 10.0F;
        }
        return super.getSecondsToDisableBlocking();
    }

    @Override
    protected void playConvertedSound() {
        this.makeSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED);
    }
}
