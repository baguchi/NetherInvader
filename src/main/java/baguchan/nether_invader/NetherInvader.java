package baguchan.nether_invader;

import baguchan.nether_invader.network.ChainPacket;
import baguchan.nether_invader.registry.*;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

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
        modEventBus.addListener(this::setupPackets);
        modEventBus.addListener(this::spawnEggSetup);
        ModItems.ITEMS.register(modEventBus);
        ModPotions.MOB_EFFECTS.register(modEventBus);
        ModPotions.POTIONS.register(modEventBus);
        ModMemoryModuleType.MEMORY_MODULE_TYPES.register(modEventBus);
        ModSensors.SENSOR_TYPE.register(modEventBus);
        ModEntitys.ENTITIES_REGISTRY.register(modEventBus);
        ModCriterionTriggers.CRITERIONS_REGISTER.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, NetherConfigs.COMMON_SPEC);
    }

    public void setupPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("1.0.0").optional();
        registrar.playBidirectional(ChainPacket.TYPE, ChainPacket.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
        });
    }

    private void spawnEggSetup(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.CHAINED_GHAST_SPAWN_EGG);
            event.accept(ModItems.AGRESSIVE_PIGLIN_SPAWN_EGG);
            event.accept(ModItems.BASTION_GENERAL_SPAWN_EGG);
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.LAVA_INFUSED_SWORD);
        }
    }
}
