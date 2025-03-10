package baguchan.nether_invader.mixin;

import baguchan.nether_invader.api.IPiglinImmunite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractPiglin.class)
public abstract class AbstractPiglinMixin extends Monster implements IPiglinImmunite {

    @Unique
    private boolean netherInvader$immuniteByPotion;

    protected AbstractPiglinMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
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

    @Unique
    public boolean isNetherInvader$immuniteByPotion() {
        return netherInvader$immuniteByPotion;
    }
}
