package baguchan.nether_invader.entity;

import baguchan.nether_invader.utils.NetherSpreaderUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NetherSpreader extends Mob implements Enemy {
    public static final NetherReactorExplosionCalculator EXPLOSION_DAMAGE_CALCULATOR = new NetherReactorExplosionCalculator();

    private final NetherSpreaderUtil netherSpreaderUtil = NetherSpreaderUtil.createLevelSpreader();

    protected static final EntityDataAccessor<Float> DATA_PROGRESS = SynchedEntityData.defineId(NetherSpreader.class, EntityDataSerializers.FLOAT);


    public float oldSpreaderProgress;

    public NetherSpreader(EntityType<? extends NetherSpreader> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_PROGRESS, 0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0F).add(Attributes.ARMOR, 18.0D).add(Attributes.ARMOR_TOUGHNESS, 5.0D).add(Attributes.MAX_HEALTH, 100D);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_33434_) {
        if (DATA_PROGRESS.equals(p_33434_)) {
            this.setBoundingBox(this.makeBoundingBox());
        }

        super.onSyncedDataUpdated(p_33434_);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_33432_) {
        super.readAdditionalSaveData(p_33432_);
        this.setSpreaderProgress(p_33432_.getFloat("SpreaderProgress"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_33443_) {
        super.addAdditionalSaveData(p_33443_);
        p_33443_.putFloat("SpreaderProgress", (byte) this.getSpreaderProgress());
    }

    @Override
    protected AABB makeBoundingBox() {
        double d0 = (double) this.getX();
        double d1 = (double) this.getY();
        double d2 = (double) this.getZ();
        double d6 = (double) this.getType().getWidth() / 2;
        double d7 = (double) this.getType().getHeight() + this.getSpreaderProgress() * 6F;
        double d8 = (double) this.getType().getWidth() / 2;

        return new AABB(d0 - d6, d1, d2 - d8, d0 + d6, d1 + d7, d2 + d8);
    }

    public void setSpreaderProgress(float spreaderProgress) {
        this.entityData.set(DATA_PROGRESS, spreaderProgress);
    }

    public float getSpreaderProgress() {
        return this.entityData.get(DATA_PROGRESS);
    }

    @Override
    public Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    @Override
    public void setDeltaMovement(Vec3 p_149804_) {
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }


    public int getExperienceReward() {
        return 100;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.oldSpreaderProgress != this.getSpreaderProgress()) {
            this.onPeekAmountChange();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isNoAi()) {
            this.netherSpreaderUtil.updateCursors(this.level(), this.blockPosition().below(), this.random, true);

            this.oldSpreaderProgress = this.getSpreaderProgress();

            if (this.getSpreaderProgress() >= 1F) {
                this.setSpreaderProgress(0F);
                this.netherSpreaderUtil.clear();
                for (int i = 0; i < 5; i++) {
                    this.netherSpreaderUtil.addCursors(this.blockPosition().below(), 10);
                }
                this.level().explode(this, (DamageSource) null, EXPLOSION_DAMAGE_CALCULATOR, this.getX(), this.getY(), this.getZ(), (float) (2), false, Level.ExplosionInteraction.BLOW, ParticleTypes.GUST, ParticleTypes.GUST_EMITTER, SoundEvents.GENERIC_EXPLODE);
            } else {
                this.setSpreaderProgress(this.getSpreaderProgress() + 0.005F);
            }
        }
    }

    private void onPeekAmountChange() {
        this.reapplyPosition();
        float f = this.getSpreaderProgress();
        float f1 = this.oldSpreaderProgress;
        float f2 = f - f1;
        if (!(f2 <= 0.0F)) {
            for (Entity entity : this.level()
                    .getEntities(
                            this,
                            getBoundingBox(),
                            EntitySelector.NO_SPECTATORS.and(p_149771_ -> !p_149771_.isPassengerOfSameVehicle(this))
                    )) {
                if (!(entity instanceof NetherSpreader) && !entity.noPhysics) {
                    entity.move(
                            MoverType.SHULKER,
                            new Vec3(
                                    0, (double) (f2), 0
                            )
                    );
                }
            }
        }
    }

    @Override
    public void push(Entity p_21294_) {
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        if (p_21436_ == MobSpawnType.STRUCTURE) {
            this.setNoAi(false);
        }

        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    public float getSpreaderProgressScale(float p_29570_) {
        return Mth.lerp(p_29570_, this.oldSpreaderProgress, this.getSpreaderProgress());
    }

    @Override
    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        if (this.level().isClientSide()) {
            this.spawnBreakingParticle();
        }
        this.playSound(SoundEvents.GENERIC_EXPLODE, 2.0F, 1.0F);
        this.discard();
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (this.level().isClientSide()) {
            this.spawnBreakingParticle();
        }
        return super.hurt(p_21016_, p_21017_);
    }

    protected void spawnBreakingParticle() {
        BlockState blockstate = Blocks.NETHERITE_BLOCK.defaultBlockState();
        for (int i = 0; i < 15; i++) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5) * (double) this.getDimensions(this.getPose()).width;
            double d1 = this.getY() + (this.random.nextDouble()) * (double) this.getDimensions(this.getPose()).height;
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * (double) this.getDimensions(this.getPose()).width;
            this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0F, 0F, 0F);
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public static final class NetherReactorExplosionCalculator extends ExplosionDamageCalculator {
        public NetherReactorExplosionCalculator() {
        }

        public boolean shouldDamageEntity(Explosion p_314513_, Entity p_314456_) {
            return !(p_314456_ instanceof AbstractPiglin) && !(p_314456_ instanceof Hoglin) && !(p_314456_ instanceof NetherSpreader);
        }
    }
}
