package baguchan.nether_invader.entity;

import baguchan.nether_invader.entity.ai.RevampedPiglinAi;
import baguchan.nether_invader.registry.ModEntities;
import baguchan.nether_invader.registry.ModSensors;
import baguchan.nether_invader.world.raid.PiglinRaid;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.List;

public class AgressivePiglin extends AbstractPiglin implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(AgressivePiglin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(AgressivePiglin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(AgressivePiglin.class, EntityDataSerializers.BOOLEAN);
    private static final Identifier SPEED_MODIFIER_BABY_ID = Identifier.withDefaultNamespace("baby");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(
            SPEED_MODIFIER_BABY_ID, 0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    );
    private static final int MAX_HEALTH = 16;
    private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
    private static final int ATTACK_DAMAGE = 5;
    private static final float CHANCE_OF_WEARING_EACH_ARMOUR_ITEM = 0.1F;
    private static final int MAX_PASSENGERS_ON_ONE_HOGLIN = 3;
    private static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;
    private static final EntityDimensions BABY_DIMENSIONS = EntityType.PIGLIN.getDimensions().scale(0.5F).withEyeHeight(0.97F);
    private static final double PROBABILITY_OF_SPAWNING_WITH_CROSSBOW_INSTEAD_OF_SWORD = 0.5;
    private boolean cannotHunt;
    protected static final ImmutableList<SensorType<? extends Sensor<? super AgressivePiglin>>> SENSOR_TYPES = ImmutableList.of(
            baguchi.bagus_lib.register.ModSensors.SMART_NEAREST_LIVING_ENTITY_SENSOR.get(), SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, ModSensors.ANGER_PIGLIN_SENSOR.get()
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
            MemoryModuleType.NEAREST_REPELLENT,
            MemoryModuleType.SPEAR_FLEEING_TIME,
            MemoryModuleType.SPEAR_FLEEING_POSITION,
            MemoryModuleType.SPEAR_CHARGE_POSITION,
            MemoryModuleType.SPEAR_ENGAGE_TIME,
            MemoryModuleType.SPEAR_STATUS
    );

    public AgressivePiglin(EntityType<? extends AbstractPiglin> p_34683_, Level p_34684_) {
        super(p_34683_, p_34684_);
        this.xpReward = 5;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput p_421634_) {
        super.addAdditionalSaveData(p_421634_);
        if (this.isBaby()) {
            p_421634_.putBoolean("IsBaby", true);
        }

        if (this.cannotHunt) {
            p_421634_.putBoolean("CannotHunt", true);
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput p_422019_) {
        super.readAdditionalSaveData(p_422019_);

        this.setBaby(p_422019_.getBooleanOr("IsBaby", false));
        this.setCannotHunt(p_422019_.getBooleanOr("CannotHunt", false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326106_) {
        super.defineSynchedData(p_326106_);
        p_326106_.define(DATA_BABY_ID, false);
        p_326106_.define(DATA_IS_CHARGING_CROSSBOW, false);
        p_326106_.define(DATA_IS_DANCING, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_34727_) {
        super.onSyncedDataUpdated(p_34727_);
        if (DATA_BABY_ID.equals(p_34727_)) {
            this.refreshDimensions();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.FOLLOW_RANGE, 35);
    }

    public static boolean checkPiglinSpawnRules(
            EntityType<Piglin> p_219198_, LevelAccessor p_219199_, EntitySpawnReason p_219200_, BlockPos p_219201_, RandomSource p_219202_
    ) {
        return !p_219199_.getBlockState(p_219201_.below()).is(Blocks.NETHER_WART_BLOCK);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34717_, DifficultyInstance p_34718_, EntitySpawnReason p_34719_, @Nullable SpawnGroupData p_34720_) {
        RandomSource randomsource = p_34717_.getRandom();

        RevampedPiglinAi.initMemories(this, p_34717_.getRandom());
        this.populateDefaultEquipmentSlots(randomsource, p_34718_);
        this.populateDefaultEquipmentEnchantments(p_34717_, randomsource, p_34718_);
        return super.finalizeSpawn(p_34717_, p_34718_, p_34719_, p_34720_);
    }

    @Override
    public boolean removeWhenFarAway(double p_34775_) {
        return this instanceof PiglinRaider piglinRaider && !piglinRaider.netherInvader$hasRaid() ? super.removeWhenFarAway(p_34775_) : false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource p_219189_, DifficultyInstance p_219190_) {
        if (this.isAdult()) {
            this.maybeWearArmor(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET), p_219189_);
            this.maybeWearArmor(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE), p_219189_);
            this.maybeWearArmor(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS), p_219189_);
            this.maybeWearArmor(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS), p_219189_);
        } else {
            this.maybeWearArmor(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS), p_219189_);

        }

        if (this instanceof PiglinRaider piglinRaider) {
            if (piglinRaider.netherInvader$isPatrolLeader()) {
                this.setItemSlotAndDropWhenKilled(EquipmentSlot.HEAD, PiglinRaid.getLeaderBannerInstance(this.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
                this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
                this.setDropChance(EquipmentSlot.CHEST, 0.0F);
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
                this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
            } else if (this.getControlledVehicle() != null) {
                if (this.getType() == ModEntities.AGRESSIVE_PIGLIN.get()) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, Items.CROSSBOW.getDefaultInstance());
                }
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
            }

        }
    }

    private void maybeWearArmor(EquipmentSlot p_219192_, ItemStack p_219193_, RandomSource p_219194_) {
        if (p_219194_.nextFloat() < 0.1F) {
            this.setItemSlot(p_219192_, p_219193_);
        }
    }

    @Override
    protected Brain.Provider<AgressivePiglin> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> p_34723_) {
        return RevampedPiglinAi.makeBrain(this, this.brainProvider().makeBrain(p_34723_));
    }

    @Override
    public Brain<AgressivePiglin> getBrain() {
        return (Brain<AgressivePiglin>) super.getBrain();
    }


    @Override
    public EntityDimensions getDefaultDimensions(Pose p_316426_) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(p_316426_);
    }

    @Override
    public void setBaby(boolean p_34729_) {
        this.getEntityData().set(DATA_BABY_ID, p_34729_);
        if (!this.level().isClientSide()) {
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_BABY.id());
            if (p_34729_) {
                attributeinstance.addTransientModifier(SPEED_MODIFIER_BABY);
            }
        }
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    private void setCannotHunt(boolean p_34792_) {
        this.cannotHunt = p_34792_;
    }

    @Override
    protected boolean canHunt() {
        return !this.cannotHunt;
    }


    @Override
    protected void customServerAiStep(ServerLevel p_376586_) {

        ProfilerFiller profilerfiller = Profiler.get();
        profilerfiller.push("piglinBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        profilerfiller.pop();
        RevampedPiglinAi.updateActivity(this);
        super.customServerAiStep(p_376586_);
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel p_376894_) {
        return this.xpReward;
    }

    @Override
    protected void finishConversion(ServerLevel p_34756_) {
        super.finishConversion(p_34756_);
    }

    private ItemStack createSpawnWeapon() {
        return (double) this.random.nextFloat() < 0.5 && !this.isBaby() ? new ItemStack(Items.CROSSBOW) : new ItemStack(Items.GOLDEN_SWORD);
    }

    private boolean isChargingCrossbow() {
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean p_34753_) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, p_34753_);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (this.isDancing()) {
            return PiglinArmPose.DANCING;
        } else if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
            return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
        } else if (this.isChargingCrossbow()) {
            return PiglinArmPose.CROSSBOW_CHARGE;
        } else {
            return this.isAggressive() && this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem) ? PiglinArmPose.CROSSBOW_HOLD : PiglinArmPose.DEFAULT;
        }
    }

    public boolean isDancing() {
        return this.entityData.get(DATA_IS_DANCING);
    }

    public void setDancing(boolean p_34790_) {
        this.entityData.set(DATA_IS_DANCING, p_34790_);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource p_34694_, float p_34695_) {
        boolean flag = super.hurtServer(serverLevel, p_34694_, p_34695_);
        if (this.level().isClientSide()) {
            return false;
        } else {
            if (flag && p_34694_.getEntity() instanceof LivingEntity) {
                RevampedPiglinAi.wasHurtBy(serverLevel, this, (LivingEntity) p_34694_.getEntity());
            }

            return flag;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity p_34704_, float p_34705_) {
        this.performCrossbowAttack(this, 1.6F);
    }

    @Override
    public boolean canUseNonMeleeWeapon(ItemStack p_482058_) {
        return p_482058_.is(net.neoforged.neoforge.common.Tags.Items.PIGLIN_USABLE_CROSSBOWS) || p_482058_.has(DataComponents.KINETIC_WEAPON);
    }


    @Override
    public boolean startRiding(Entity p_34701_, boolean p_34702_, boolean b2) {
        if (this.isBaby() && p_34701_.getType() == EntityType.HOGLIN) {
            p_34701_ = this.getTopPassenger(p_34701_, 3);
        }

        return super.startRiding(p_34701_, p_34702_, b2);
    }

    private Entity getTopPassenger(Entity p_34731_, int p_34732_) {
        List<Entity> list = p_34731_.getPassengers();
        return p_34732_ != 1 && !list.isEmpty() ? this.getTopPassenger(list.get(0), p_34732_ - 1) : p_34731_;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.level().isClientSide() ? null : RevampedPiglinAi.getSoundForCurrentActivity(this).orElse(null);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_34767_) {
        return SoundEvents.PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos p_34748_, BlockState p_34749_) {
        this.playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void playConvertedSound() {
        this.makeSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }
}
