package baguchan.piglin_invader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
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
@Mod(PiglinInvader.MODID)
public class PiglinInvader
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "piglin_invader";
    // Directly reference a slf4j logger

    public PiglinInvader()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addPiglinInvaderDatapack);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    public void addPiglinInvaderDatapack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("orbofdominance");
            var supplier = new PathPackResources.PathResourcesSupplier(resourcePath, true);

            var pack = createBuiltinPack("builtin/piglin_portal", supplier, Component.literal("piglin_invader"));

            event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
        }
    }

    protected Pack createBuiltinPack(String p_250596_, Pack.ResourcesSupplier p_249625_, Component p_249043_) {
        return Pack.readMetaAndCreate(p_250596_, p_249043_, false, p_249625_, PackType.SERVER_DATA, Pack.Position.TOP, PackSource.FEATURE);
    }
}
