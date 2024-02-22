package baguchan.nether_invader.region;

import baguchan.nether_invader.registry.ModBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

public class OverworldNetherRegion extends Region {
    public OverworldNetherRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        // Overlap Vanilla's parameters with our own for our COLD_BLUE biome.
        // The parameters for this biome are chosen arbitrarily.
        new ParameterUtils.ParameterPointListBuilder()
                .temperature(Climate.Parameter.point(0.4F))
                .humidity(Climate.Parameter.point(0.0F))
                .continentalness(ParameterUtils.Continentalness.NEAR_INLAND, ParameterUtils.Continentalness.INLAND)
                .erosion(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_3)
                .depth(ParameterUtils.Depth.SURFACE, ParameterUtils.Depth.FLOOR)
                .weirdness(ParameterUtils.Weirdness.LOW_SLICE_VARIANT_ASCENDING, ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING).build().forEach(point -> builder.add(point, ModBiomes.NETHER_WASTES));
        new ParameterUtils.ParameterPointListBuilder()
                .temperature(Climate.Parameter.point(0.0F))
                .humidity(Climate.Parameter.point(0.0F))
                .continentalness(ParameterUtils.Continentalness.NEAR_INLAND, ParameterUtils.Continentalness.INLAND)
                .erosion(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_3)
                .depth(ParameterUtils.Depth.SURFACE, ParameterUtils.Depth.FLOOR)
                .weirdness(ParameterUtils.Weirdness.LOW_SLICE_VARIANT_ASCENDING, ParameterUtils.Weirdness.MID_SLICE_VARIANT_DESCENDING)
                .build().forEach(point -> builder.add(point, ModBiomes.CRIMSON_FOREST));
        // Add our points to the mapper
        builder.build().forEach(mapper);
    }
}