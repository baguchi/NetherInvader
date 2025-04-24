package baguchan.nether_invader;

import baguchan.nether_invader.network.ChainPacket;
import baguchan.nether_invader.registry.ModEntitys;
import baguchan.nether_invader.registry.ModItems;
import baguchan.nether_invader.registry.ModPotions;
import baguchan.nether_invader.registry.ModSensors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static baguchan.nether_invader.registry.ModPotions.STRONG_AWKWARD_POTION;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NetherInvader.MODID)
public class NetherInvader
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "nether_invader";
    // Directly reference a slf4j logger

    public static final String NETWORK_PROTOCOL = "2";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "net"))
            .networkProtocolVersion(() -> NETWORK_PROTOCOL)
            .clientAcceptedVersions(NETWORK_PROTOCOL::equals)
            .serverAcceptedVersions(NETWORK_PROTOCOL::equals)
            .simpleChannel();

    public NetherInvader()
    {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        this.setupMessages();
        modEventBus.addListener(this::spawnEggSetup);
        ModItems.ITEMS.register(modEventBus);
        ModPotions.MOB_EFFECTS.register(modEventBus);
        ModPotions.POTIONS.register(modEventBus);
        ModSensors.SENSOR_TYPE.register(modEventBus);
        ModEntitys.ENTITIES_REGISTRY.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NetherConfigs.COMMON_SPEC);
    }

    private void setupMessages() {
        CHANNEL.messageBuilder(ChainPacket.class, 0)
                .encoder(ChainPacket::serialize).decoder(ChainPacket::deserialize)
                .consumerMainThread(ChainPacket::handle)
                .add();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            PotionBrewing.addMix(Potions.AWKWARD, Items.GLOWSTONE_DUST, STRONG_AWKWARD_POTION.get());

        });
    }

    private void spawnEggSetup(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.CHAINED_GHAST_SPAWN_EGG);
            event.accept(ModItems.AGRESSIVE_PIGLIN_SPAWN_EGG);
        }
    }
}
