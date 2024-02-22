package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModBiomes {
    public static final ResourceKey<Biome> CRIMSON_FOREST = register("crimson_forest");
    public static final ResourceKey<Biome> NETHER_WASTES = register("nether_wastes");

    public static Biome netherWastes(HolderGetter<PlacedFeature> p_255840_, HolderGetter<ConfiguredWorldCarver<?>> p_255956_) {
        MobSpawnSettings mobspawnsettings = new MobSpawnSettings.Builder()
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 2, 4, 4))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 15, 4, 4))
                .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2))
                .build();
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder(p_255840_, p_255956_)
                .addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA);
        BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
        biomegenerationsettings$builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NETHER)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.RED_MUSHROOM_NETHER)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED);
        BiomeDefaultFeatures.addNetherDefaultOres(biomegenerationsettings$builder);
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(2.0F)
                .downfall(0.0F)
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(4159204)
                                .waterFogColor(329011)
                                .fogColor(3344392)
                                .skyColor(calculateSkyColor(2.0F))
                                .ambientLoopSound(SoundEvents.AMBIENT_NETHER_WASTES_LOOP)
                                .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_NETHER_WASTES_MOOD, 6000, 8, 2.0))
                                .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111))
                                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_NETHER_WASTES))
                                .build()
                )
                .mobSpawnSettings(mobspawnsettings)
                .generationSettings(biomegenerationsettings$builder.build())
                .build();
    }

    public static Biome crimsonForest(HolderGetter<PlacedFeature> p_256350_, HolderGetter<ConfiguredWorldCarver<?>> p_256386_) {
        MobSpawnSettings mobspawnsettings = new MobSpawnSettings.Builder()
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 1, 2, 4))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HOGLIN, 9, 3, 4))
                .addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 5, 3, 4))
                .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2))
                .build();
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder(p_256350_, p_256386_)
                .addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA);
        BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
        biomegenerationsettings$builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.WEEPING_VINES)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.CRIMSON_FUNGI)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.CRIMSON_FOREST_VEGETATION);
        BiomeDefaultFeatures.addNetherDefaultOres(biomegenerationsettings$builder);
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(2.0F)
                .downfall(0.0F)
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(4159204)
                                .waterFogColor(329011)
                                .fogColor(3343107)
                                .skyColor(calculateSkyColor(2.0F))
                                .ambientParticle(new AmbientParticleSettings(ParticleTypes.CRIMSON_SPORE, 0.025F))
                                .ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
                                .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0))
                                .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111))
                                .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST))
                                .build()
                )
                .mobSpawnSettings(mobspawnsettings)
                .generationSettings(biomegenerationsettings$builder.build())
                .build();
    }

    protected static int calculateSkyColor(float p_194844_) {
        float $$1 = p_194844_ / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

    public static void bootstrap(BootstapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> vanillaConfiguredCarvers = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(CRIMSON_FOREST, crimsonForest(placedFeatures, vanillaConfiguredCarvers));
        context.register(NETHER_WASTES, netherWastes(placedFeatures, vanillaConfiguredCarvers));
    }

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(NetherInvader.MODID, name));
    }
}