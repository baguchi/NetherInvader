package baguchan.nether_invader;

import baguchan.nether_invader.region.OverworldNetherRegion;
import baguchan.nether_invader.registry.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NetherInvader.MODID)
public class NetherInvader
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "nether_invader";
    // Directly reference a slf4j logger

    public NetherInvader(IEventBus modEventBus)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addPiglinInvaderDatapack);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntitys.ENTITIES_REGISTRY.register(modEventBus);
        ModBlockEntitys.BLOCK_ENTITIES.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NetherConfigs.COMMON_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            if (NetherConfigs.COMMON.nether_spawn_in_overworld.get()) {
                // Weights are kept intentionally low as we add minimal biomes
                Regions.register(new OverworldNetherRegion(new ResourceLocation(MODID, "overworld_nether"), 3));

                // Register our surface rules
                SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, ModSurfaceRuleData.makeRules());
            }
        });
    }

    public void addPiglinInvaderDatapack(AddPackFindersEvent event) {
        /*if (event.getPackType() == PackType.SERVER_DATA) {
            var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("nether_raid");
            var supplier = new PathPackResources.PathResourcesSupplier(resourcePath, true);

            var pack = createBuiltinPack("builtin/nether_raid", supplier, Component.literal("Enable Nether Raid(WIP)"));

            event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
        }*/

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
