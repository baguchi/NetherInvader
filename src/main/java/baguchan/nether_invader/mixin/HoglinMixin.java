package baguchan.nether_invader.mixin;

import baguchan.nether_invader.api.IPiglinImmunite;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Hoglin.class)
public abstract class HoglinMixin extends Animal implements IPiglinImmunite {

    @Unique
    private boolean netherInvader$immuniteByPotion;

    protected HoglinMixin(EntityType<? extends Animal> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }


    @Inject(method = "isConverting", at = @At("HEAD"), cancellable = true)
    public void isConverting(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (level().getBiome(this.blockPosition()).is(BiomeTags.IS_NETHER)) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(ValueOutput p_422353_, CallbackInfo ci) {
        p_422353_.putBoolean("immunite_by_potion", netherInvader$immuniteByPotion);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(ValueInput p_421619_, CallbackInfo ci) {
        netherInvader$immuniteByPotion = p_421619_.getBooleanOr("immunite_by_potion", false);
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
