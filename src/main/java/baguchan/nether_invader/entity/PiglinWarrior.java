package baguchan.nether_invader.entity;

import baguchan.nether_invader.entity.ai.PiglinWarriorAi;
import baguchan.nether_invader.registry.ModSensors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.List;

public class PiglinWarrior extends AbstractPiglin {

    private static final int MAX_HEALTH = 16;
    private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
    private static final int ATTACK_DAMAGE = 5;
    private static final float CHANCE_OF_WEARING_EACH_ARMOUR_ITEM = 0.1F;
    private static final int MAX_PASSENGERS_ON_ONE_HOGLIN = 3;
    private static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;
    private static final double PROBABILITY_OF_SPAWNING_WITH_CROSSBOW_INSTEAD_OF_SWORD = 0.5;
    private boolean cannotHunt;
    public static final Brain.Provider<PiglinWarrior> BRAIN_PROVIDER = Brain.provider(
            List.of(MemoryModuleType.UNIVERSAL_ANGER,
                    MemoryModuleType.ATE_RECENTLY),
            List.of(
                    baguchi.bagus_lib.register.ModSensors.SMART_NEAREST_LIVING_ENTITY_SENSOR.get(), SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, ModSensors.PIGLIN_WARRIOR_SENSOR.get()
            ),
            PiglinWarriorAi::getActivities
    );

    public final AnimationState attackAnimationState = new AnimationState();
    private final int attackAnimationLength = 34;

    private int attackAnimationTick;


    public PiglinWarrior(EntityType<? extends AbstractPiglin> p_34683_, Level p_34684_) {
        super(p_34683_, p_34684_);
        this.xpReward = 6;
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult interactionResult = super.mobInteract(player, hand);
        if (interactionResult.consumesAction()) {
            return interactionResult;
        } else if (this.level() instanceof ServerLevel level) {
            return PiglinWarriorAi.mobInteract(level, this, player, hand);
        } else {
            boolean canAdmire = PiglinWarriorAi.canAdmire(this, player.getItemInHand(hand)) && this.getArmPose() != PiglinArmPose.ADMIRING_ITEM;
            return (InteractionResult) (canAdmire ? InteractionResult.SUCCESS : InteractionResult.PASS);
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
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.FOLLOW_RANGE, 20);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34717_, DifficultyInstance p_34718_, EntitySpawnReason p_34719_, @Nullable SpawnGroupData p_34720_) {
        RandomSource randomsource = p_34717_.getRandom();

        PiglinWarriorAi.initMemories(this, p_34717_.getRandom());
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
        this.setItemSlot(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
    }

    @Override
    protected Brain<PiglinWarrior> makeBrain(Brain.Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    @Override
    public Brain<PiglinWarrior> getBrain() {
        return (Brain<PiglinWarrior>) super.getBrain();
    }


    private void setCannotHunt(boolean p_34792_) {
        this.cannotHunt = p_34792_;
    }

    @Override
    public boolean canHunt() {
        return !this.cannotHunt;
    }


    @Override
    protected void customServerAiStep(ServerLevel p_376586_) {

        ProfilerFiller profilerfiller = Profiler.get();
        profilerfiller.push("piglinBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        profilerfiller.pop();
        PiglinWarriorAi.updateActivity(this);
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
        return new ItemStack(Items.GOLDEN_SWORD);
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
            return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
        } else if (PiglinWarriorAi.isLovedItem(this.getOffhandItem())) {
            return PiglinArmPose.ADMIRING_ITEM;
        } else {
            return PiglinArmPose.DEFAULT;
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource p_34694_, float p_34695_) {
        boolean flag = super.hurtServer(serverLevel, p_34694_, p_34695_);
        if (this.level().isClientSide()) {
            return false;
        } else {
            if (flag && p_34694_.getEntity() instanceof LivingEntity) {
                PiglinWarriorAi.wasHurtBy(serverLevel, this, (LivingEntity) p_34694_.getEntity());
            }

            return flag;
        }
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
        return this.level().isClientSide() ? null : PiglinWarriorAi.getSoundForCurrentActivity(this).orElse(null);
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

    public void holdInOffHand(ItemStack itemStack) {
        if (itemStack.isPiglinCurrency()) {
            this.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
            this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        } else {
            this.setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, itemStack);
        }
    }

    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
        return net.neoforged.neoforge.event.EventHooks.canEntityGrief(level, this) && this.canPickUpLoot() && PiglinWarriorAi.wantsToPickup(this, itemStack);
    }

    protected boolean canReplaceCurrentItem(ItemStack newItemStack) {
        EquipmentSlot slot = this.getEquipmentSlotForItem(newItemStack);
        ItemStack currentItemStackInCorrespondingSlot = this.getItemBySlot(slot);
        return this.canReplaceCurrentItem(newItemStack, currentItemStackInCorrespondingSlot, slot);
    }

    @Override
    protected boolean canReplaceCurrentItem(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
        if (EnchantmentHelper.has(currentItemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
            return false;
        } else {
            TagKey<Item> preferredWeaponType = this.getPreferredWeaponType();
            boolean newItemWanted = PiglinWarriorAi.isLovedItem(newItemStack) || preferredWeaponType != null && newItemStack.is(preferredWeaponType);
            boolean currentItemWanted = PiglinWarriorAi.isLovedItem(currentItemStack) || preferredWeaponType != null && currentItemStack.is(preferredWeaponType);
            if (newItemWanted && !currentItemWanted) {
                return true;
            } else {
                return !newItemWanted && currentItemWanted ? false : super.canReplaceCurrentItem(newItemStack, currentItemStack, slot);
            }
        }
    }

    @Override
    protected void pickUpItem(ServerLevel level, ItemEntity entity) {
        this.onItemPickup(entity);
        PiglinWarriorAi.pickUpItem(level, this, entity);
    }

}
