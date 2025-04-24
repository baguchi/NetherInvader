package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.effect.DefMobEffect;
import baguchan.nether_invader.effect.PiglinRaidEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = NetherInvader.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModPotions {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, NetherInvader.MODID);

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, NetherInvader.MODID);
    public static final RegistryObject<MobEffect> AWKWARD = MOB_EFFECTS.register(
            "awkward",
            () -> new DefMobEffect(MobEffectCategory.NEUTRAL, 0xBD665C)
    );
    public static final RegistryObject<MobEffect> HORDE_OMEN = MOB_EFFECTS.register(
            "horde_omen",
            () -> new PiglinRaidEffect(MobEffectCategory.NEUTRAL, 0xBD665C)
    );

    public static final RegistryObject<Potion> STRONG_AWKWARD_POTION = POTIONS.register("strong_awkward", () -> new Potion("strong_awkward", new MobEffectInstance(AWKWARD.get(), 9600)));

    @SubscribeEvent
    public static void init() {
    }
}
