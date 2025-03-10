package baguchan.nether_invader;

import baguchan.nether_invader.registry.ModEntitys;
import baguchan.nether_invader.registry.ModItems;
import baguchan.nether_invader.registry.ModPotions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(NetherInvader.MODID)
public class NetherInvader
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "nether_invader";
    // Directly reference a slf4j logger

    public NetherInvader(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModItems.ITEMS.register(modEventBus);
        ModPotions.MOB_EFFECTS.register(modEventBus);
        ModPotions.POTIONS.register(modEventBus);
        ModEntitys.ENTITIES_REGISTRY.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, NetherConfigs.COMMON_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
        });
    }
}
