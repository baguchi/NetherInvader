package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.effect.DefMobEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = NetherInvader.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModPotions {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, NetherInvader.MODID);

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, NetherInvader.MODID);
    public static final DeferredHolder<MobEffect, MobEffect> AWKWARD = MOB_EFFECTS.register(
            "awkward",
            () -> new DefMobEffect(MobEffectCategory.NEUTRAL, 0xBD665C)
    );
    public static final DeferredHolder<Potion, Potion> STRONG_AWKWARD_POTION = POTIONS.register("strong_awkward", () -> new Potion("strong_awkward", new MobEffectInstance(AWKWARD, 9600)));

    @SubscribeEvent
    public static void init(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addMix(Potions.AWKWARD, Items.GLOWSTONE_DUST, STRONG_AWKWARD_POTION);
    }
}
