package baguchan.nether_invader.mixin;

import baguchan.nether_invader.entity.Scaffolding;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BehaviorUtils.class)
public class BehaviorUtilMixin {

    @Inject(method = "isWithinAttackRange", at = @At("HEAD"), cancellable = true)
    private static void isWithinAttackRange(Mob p_22633_, LivingEntity p_22634_, int p_22635_, CallbackInfoReturnable<Boolean> cir) {
        if (p_22633_.getMainHandItem().getItem() instanceof ProjectileWeaponItem projectileweaponitem && p_22633_.canUseNonMeleeWeapon(p_22633_.getMainHandItem())
        ) {
            if (p_22633_.getVehicle() instanceof Scaffolding) {
                int i = 32 - p_22635_;
                cir.setReturnValue(p_22633_.closerThan(p_22634_, (double) i));
            }
        }
    }
}
