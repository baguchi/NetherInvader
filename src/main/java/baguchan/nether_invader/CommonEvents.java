package baguchan.nether_invader;

import baguchan.nether_invader.api.IPiglinImmunite;
import baguchan.nether_invader.registry.ModPotions;
import baguchan.nether_invader.world.savedata.PiglinRaidData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = NetherInvader.MODID)
public class CommonEvents {
    @SubscribeEvent
    public static void onServerTick(LevelTickEvent.Post tick) {
        if (!tick.getLevel().isClientSide && tick.getLevel() instanceof ServerLevel serverWorld) {
            PiglinRaidData.get(serverWorld).tick(serverWorld);
        }
    }

    @SubscribeEvent
    public static void onKilled(LivingDeathEvent event) {
        if (event.getEntity() instanceof PiglinBrute piglinBrute) {
            if (event.getSource().getEntity() instanceof Player player) {
                if (player.hasEffect(ModPotions.HORDE_OMEN)) {
                    player.addEffect(new MobEffectInstance(ModPotions.HORDE_OMEN, 120000, player.getEffect(ModPotions.HORDE_OMEN).getAmplifier() + 1));

                } else {
                    player.addEffect(new MobEffectInstance(ModPotions.HORDE_OMEN, 120000));
                }
            }
        }
    }

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
