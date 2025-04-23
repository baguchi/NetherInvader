package baguchan.nether_invader.data;

import baguchan.nether_invader.NetherInvader;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = NetherInvader.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        event.getGenerator().addProvider(true, new RegistryDataGenerator(packOutput, lookupProvider));

        DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(packOutput, lookupProvider);
        generator.addProvider(true, new ModAdvancements(packOutput, lookupProvider));
    }
}