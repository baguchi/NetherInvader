package baguchan.nether_invader.data;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.registry.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.NOISE, (context) -> {
            })
            .add(Registries.DENSITY_FUNCTION, (context) -> {
            })
            .add(Registries.CONFIGURED_FEATURE, (context) -> {
            })
            .add(Registries.PLACED_FEATURE, (context) -> {
            })
            .add(Registries.CONFIGURED_CARVER, (context) -> {
            })
            .add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, (context) -> {
            })
            .add(Registries.NOISE_SETTINGS, (context) -> {
            })
            .add(Registries.DIMENSION_TYPE, (context) -> {
            })
            .add(Registries.BIOME, ModBiomes::bootstrap);


    public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", NetherInvader.MODID));
    }
}