package baguchan.nether_invader;

import baguchan.nether_invader.registry.ModBlockEntitys;
import baguchan.nether_invader.registry.ModBlocks;
import baguchan.nether_invader.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NetherInvader.MODID)
public class NetherInvader
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "nether_invader";
    // Directly reference a slf4j logger

    public NetherInvader()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addPiglinInvaderDatapack);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntitys.BLOCK_ENTITIES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NetherConfigs.COMMON_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    public void addPiglinInvaderDatapack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("nether_raid");
            var supplier = new PathPackResources.PathResourcesSupplier(resourcePath, true);

            var pack = createBuiltinPack("builtin/nether_raid", supplier, Component.literal("Enable Nether Raid(WIP)"));

            event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
        }

        if (event.getPackType() == PackType.SERVER_DATA) {
            var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("nether_reactor");
            var supplier = new PathPackResources.PathResourcesSupplier(resourcePath, true);

            var pack = createBuiltinPack("builtin/nether_reactor", supplier, Component.literal("Enable Nether Reactor Recipe"));

            event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
        }
    }

    protected Pack createBuiltinPack(String p_250596_, Pack.ResourcesSupplier p_249625_, Component p_249043_) {
        return Pack.readMetaAndCreate(p_250596_, p_249043_, NetherConfigs.COMMON.enable_nether_invader_feature_default.get(), p_249625_, PackType.SERVER_DATA, Pack.Position.TOP, PackSource.FEATURE);
    }
}
