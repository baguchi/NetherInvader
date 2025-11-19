package baguchan.nether_invader.entity;

import baguchan.nether_invader.entity.ai.BastionPiglinAi;
import baguchan.nether_invader.registry.ModItems;
import baguchan.nether_invader.registry.ModSensors;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class BastionGeneral extends AbstractPiglin {
    protected static final ImmutableList<SensorType<? extends Sensor<? super BastionGeneral>>> SENSOR_TYPES = ImmutableList.of(
            bagu_chan.bagus_lib.register.ModSensors.SMART_NEAREST_LIVING_ENTITY_SENSOR.get(), SensorType.NEAREST_ITEMS, SensorType.HURT_BY, ModSensors.ANGER_PIGLIN_SENSOR.get()
    );
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.DOORS_TO_CLOSE,
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
            MemoryModuleType.NEARBY_ADULT_PIGLINS,
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS,
            MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.INTERACTION_TARGET,
            MemoryModuleType.PATH,
            MemoryModuleType.ANGRY_AT,
            MemoryModuleType.UNIVERSAL_ANGER,
            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.CELEBRATE_LOCATION,
            MemoryModuleType.DANCING,
            MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED,
            MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT,
            MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT,
            MemoryModuleType.ATE_RECENTLY,
            MemoryModuleType.NEAREST_REPELLENT
    );

    public AnimationState attackAnimationState = new AnimationState();
    private final int attackAnimationLength = 34;

    private int attackAnimationTick;


    public BastionGeneral(EntityType<? extends AbstractPiglin> p_34683_, Level p_34684_) {
        super(p_34683_, p_34684_);
        this.xpReward = 5;
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
    protected void updateWalkAnimation(float p_382793_) {
        float f = Math.min(p_382793_ * 20.0F, 3.0F);
        this.walkAnimation.update(f, 0.4F);
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
    public void addAdditionalSaveData(CompoundTag p_34751_) {
        super.addAdditionalSaveData(p_34751_);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_34725_) {
        super.readAdditionalSaveData(p_34725_);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel p_348503_, DamageSource p_34697_, boolean p_34699_) {
        super.dropCustomDeathLoot(p_348503_, p_34697_, p_34699_);
        if (p_34697_.getEntity() instanceof Creeper creeper && creeper.canDropMobsSkull()) {
            ItemStack itemstack = new ItemStack(Items.PIGLIN_HEAD);
            creeper.increaseDroppedSkulls();
            this.spawnAtLocation(itemstack);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326106_) {
        super.defineSynchedData(p_326106_);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_34727_) {
        super.onSyncedDataUpdated(p_34727_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60.0).add(Attributes.ARMOR, 4.0F).add(Attributes.ARMOR_TOUGHNESS, 3.0F).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 35);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34717_, DifficultyInstance p_34718_, MobSpawnType p_34719_, @Nullable SpawnGroupData p_34720_) {
        RandomSource randomsource = p_34717_.getRandom();

        BastionPiglinAi.initMemories(this, p_34717_.getRandom());
        this.populateDefaultEquipmentSlots(randomsource, p_34718_);
        this.populateDefaultEquipmentEnchantments(p_34717_, randomsource, p_34718_);
        return super.finalizeSpawn(p_34717_, p_34718_, p_34719_, p_34720_);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double p_34775_) {
        return !this.isPersistenceRequired();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource p_219189_, DifficultyInstance p_219190_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.LAVA_INFUSED_SWORD.asItem()));
    }

    @Override
    protected Brain.Provider<BastionGeneral> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> p_34723_) {
        return BastionPiglinAi.makeBrain(this, this.brainProvider().makeBrain(p_34723_));
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
    protected void customServerAiStep() {
        this.level().getProfiler().push("piglinBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        BastionPiglinAi.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    protected int getBaseExperienceReward() {
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
    public boolean hurt(DamageSource p_34694_, float p_34695_) {
        boolean flag = super.hurt(p_34694_, p_34695_);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (flag && p_34694_.getEntity() instanceof LivingEntity) {
                BastionPiglinAi.wasHurtBy(this, (LivingEntity) p_34694_.getEntity());
            }

            return flag;
        }
    }

    @Override
    public boolean isAlliedTo(Entity p_320563_) {
        if (super.isAlliedTo(p_320563_)) {
            return true;
        } else {
            return p_320563_ instanceof AbstractPiglin && this.getTeam() == null && p_320563_.getTeam() == null;
        }
    }

    @Override
    public boolean startRiding(Entity p_34701_, boolean p_34702_) {
        if (this.isBaby() && p_34701_.getType() == EntityType.HOGLIN) {
            p_34701_ = this.getTopPassenger(p_34701_, 3);
        }

        return super.startRiding(p_34701_, p_34702_);
    }

    private Entity getTopPassenger(Entity p_34731_, int p_34732_) {
        List<Entity> list = p_34731_.getPassengers();
        return p_34732_ != 1 && !list.isEmpty() ? this.getTopPassenger(list.get(0), p_34732_ - 1) : p_34731_;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.level().isClientSide ? null : BastionPiglinAi.getSoundForCurrentActivity(this).orElse(null);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_34767_) {
        return SoundEvents.PIGLIN_BRUTE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_BRUTE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos p_34748_, BlockState p_34749_) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void playConvertedSound() {
        this.makeSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED);
    }
}
