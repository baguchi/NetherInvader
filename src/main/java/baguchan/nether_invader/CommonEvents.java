package baguchan.nether_invader;

import baguchan.nether_invader.api.IPiglinImmunite;
import baguchan.nether_invader.registry.ModPotions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = NetherInvader.MODID)
public class CommonEvents {
    @SubscribeEvent
    public static void tickEvent(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity living) {
            if (living instanceof IPiglinImmunite piglinImmunite) {
                if (piglinImmunite.isNetherInvader$immuniteByPotion() && !living.hasEffect(ModPotions.AWKWARD)) {
                    if (living instanceof AbstractPiglin abstractPiglin && abstractPiglin.isImmuneToZombification()) {
                        piglinImmunite.setNetherInvader$immuniteByPotion(false);
                        abstractPiglin.setImmuneToZombification(false);
                    }

                    if (living instanceof Hoglin hoglin && hoglin.isImmuneToZombification()) {
                        piglinImmunite.setNetherInvader$immuniteByPotion(false);
                        hoglin.setImmuneToZombification(false);
                    }
                }

                if (!piglinImmunite.isNetherInvader$immuniteByPotion() && living.hasEffect(ModPotions.AWKWARD)) {
                    if (living instanceof AbstractPiglin abstractPiglin && !abstractPiglin.isImmuneToZombification()) {
                        piglinImmunite.setNetherInvader$immuniteByPotion(true);
                        abstractPiglin.setImmuneToZombification(true);
                    }

                    if (living instanceof Hoglin hoglin && !hoglin.isImmuneToZombification()) {
                        piglinImmunite.setNetherInvader$immuniteByPotion(true);
                        hoglin.setImmuneToZombification(true);
                    }
                }
            }
        }
    }
}
