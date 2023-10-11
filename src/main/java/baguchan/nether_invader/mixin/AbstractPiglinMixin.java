package baguchan.nether_invader.mixin;

import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractPiglin.class)
public abstract class AbstractPiglinMixin extends Monster {

    protected AbstractPiglinMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Inject(method = "isConverting", at = @At("HEAD"), cancellable = true)
    public void isConverting(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (level().getBiome(this.blockPosition()).is(BiomeTags.IS_NETHER)) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}
