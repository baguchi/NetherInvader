package baguchan.nether_invader.mixin;

import baguchan.nether_invader.api.IPiglinImmunite;
import baguchan.nether_invader.entity.PiglinRaider;
import baguchan.nether_invader.world.raid.PiglinRaid;
import baguchan.nether_invader.world.savedata.PiglinRaidData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(AbstractPiglin.class)
public abstract class AbstractPiglinMixin extends Monster implements IPiglinImmunite, PiglinRaider {

    @Unique
    private boolean netherInvader$immuniteByPotion;

    @Nullable
    @Unique
    protected PiglinRaid netherInvader$raid;
    @Unique
    private int netherInvader$wave;
    @Unique
    private boolean netherInvader$canJoinRaid;
    @Unique
    private int netherInvader$ticksOutsideRaid;

    @Unique
    private boolean netherInvader$patrolLeader;
    @Unique
    private boolean netherInvader$patrolling;


    protected AbstractPiglinMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    protected void customServerAiStep(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel && this.isAlive()) {
            PiglinRaid raid = this.netherInvader$getCurrentRaid();
            if (this.netherInvader$canJoinRaid()) {
                if (raid == null) {
                    if (this.level().getGameTime() % 20L == 0L) {
                        PiglinRaid raid1 = PiglinRaidData.get(this.level()).getNearbyRaid(this.blockPosition(), 9216);
                        if (raid1 != null && PiglinRaidData.canJoinRaid((AbstractPiglin) (Object) this, raid1)) {
                            raid1.joinRaid(raid1.getGroupsSpawned(), (AbstractPiglin) (Object) this, null, true);
                        }
                    }
                } else {
                    LivingEntity livingentity = this.getTarget();
                    if (livingentity != null && (livingentity.getType() == EntityType.PLAYER || livingentity.getType() == EntityType.IRON_GOLEM)) {
                        this.noActionTime = 0;
                    }
                }
            }
        }
    }

    @Inject(method = "isConverting", at = @At("HEAD"), cancellable = true)
    public void isConverting(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (level().getBiome(this.blockPosition()).is(BiomeTags.IS_NETHER)) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag p_34661_, CallbackInfo ci) {
        p_34661_.putBoolean("immunite_by_potion", netherInvader$immuniteByPotion);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag p_34661_, CallbackInfo ci) {
        netherInvader$immuniteByPotion = p_34661_.getBoolean("immunite_by_potion");
    }

    @Unique
    public void setNetherInvader$immuniteByPotion(boolean netherInvader$immuniteByPotion) {
        this.netherInvader$immuniteByPotion = netherInvader$immuniteByPotion;
    }

    public int netherInvader$getTicksOutsideRaid() {
        return this.netherInvader$ticksOutsideRaid;
    }

    public void netherInvader$setTicksOutsideRaid(int p_37864_) {
        this.netherInvader$ticksOutsideRaid = p_37864_;
    }


    @Unique
    public boolean isNetherInvader$immuniteByPotion() {
        return netherInvader$immuniteByPotion;
    }

    @Unique
    public boolean netherInvader$canJoinRaid() {
        return this.netherInvader$canJoinRaid;
    }

    @Unique
    public void netherInvader$setCanJoinRaid(boolean p_37898_) {
        this.netherInvader$canJoinRaid = p_37898_;
    }


    @Override
    public boolean netherInvader$canJoinPatrol() {
        return !this.netherInvader$hasActiveRaid();
    }

    @Unique
    public void netherInvader$setCurrentRaid(@Nullable PiglinRaid p_37852_) {
        this.netherInvader$raid = p_37852_;
    }

    @Nullable
    public PiglinRaid netherInvader$getCurrentRaid() {
        return this.netherInvader$raid;
    }

    @Unique
    public boolean netherInvader$isCaptain() {
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
        boolean flag = !itemstack.isEmpty()
                && ItemStack.matches(itemstack, PiglinRaid.getLeaderBannerInstance());
        boolean flag1 = this.netherInvader$isPatrolLeader();
        return flag && flag1;
    }

    @Unique
    public void netherInvader$setPatrolLeader(boolean p_33076_) {
        this.netherInvader$patrolLeader = p_33076_;
        this.netherInvader$patrolling = true;
    }

    @Unique
    public boolean netherInvader$isPatrolLeader() {
        return this.netherInvader$patrolLeader;
    }

    @Unique
    public boolean netherInvader$hasRaid() {
        return !(this.level() instanceof ServerLevel serverlevel)
                ? false
                : this.netherInvader$getCurrentRaid() != null || PiglinRaidData.get(serverlevel).getRaidAt(this.blockPosition()) != null;
    }

    @Unique
    public void netherInvader$setWave(int p_37843_) {
        this.netherInvader$wave = p_37843_;
    }

    @Unique
    public int netherInvader$getWave() {
        return this.netherInvader$wave;
    }

    @Override
    public void netherInvader$applyRaidBuffs(ServerLevel level, int p37714, boolean b) {
    }

    @Override
    public void die(DamageSource p_37847_) {
        if (this.level() instanceof ServerLevel) {
            Entity entity = p_37847_.getEntity();
            PiglinRaid raid = this.netherInvader$getCurrentRaid();
            if (raid != null) {
                if (this.netherInvader$isPatrolLeader()) {
                    raid.removeLeader(this.netherInvader$getWave());
                }

                if (entity != null && entity.getType() == EntityType.PLAYER) {
                    raid.addHeroOfTheVillage(entity);
                }

                raid.removeFromRaid((AbstractPiglin) (Object) this, false);
            }
        }

        super.die(p_37847_);
    }
}
