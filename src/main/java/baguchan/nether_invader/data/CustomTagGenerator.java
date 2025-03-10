package baguchan.nether_invader.data;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.registry.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CustomTagGenerator {
    public static class BiomeTagGenerator extends BiomeTagsProvider {

        public BiomeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, future, NetherInvader.MODID, existingFileHelper);
        }

        private static TagKey<Biome> create(String p_207631_) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(NetherInvader.MODID, p_207631_));
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BiomeTags.IS_NETHER).add(ModBiomes.CRIMSON_FOREST, ModBiomes.NETHER_WASTES, ModBiomes.SOUL_SAND_VALLEYS);
            this.tag(BiomeTags.HAS_BASTION_REMNANT).remove(ModBiomes.CRIMSON_FOREST).remove(ModBiomes.NETHER_WASTES).remove(ModBiomes.SOUL_SAND_VALLEYS);
            this.tag(BiomeTags.HAS_NETHER_FORTRESS).remove(ModBiomes.CRIMSON_FOREST).remove(ModBiomes.NETHER_WASTES).remove(ModBiomes.SOUL_SAND_VALLEYS);
            //vanilla
            tag(BiomeTags.WITHOUT_ZOMBIE_SIEGES).add(ModBiomes.CRIMSON_FOREST, ModBiomes.NETHER_WASTES, ModBiomes.SOUL_SAND_VALLEYS);
            tag(BiomeTags.WITHOUT_PATROL_SPAWNS).add(ModBiomes.CRIMSON_FOREST, ModBiomes.NETHER_WASTES, ModBiomes.SOUL_SAND_VALLEYS);
            tag(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS).add(ModBiomes.CRIMSON_FOREST, ModBiomes.NETHER_WASTES, ModBiomes.SOUL_SAND_VALLEYS);
        }

        @Override
        public String getName() {
            return "NetherInvader Biome Tags";
        }
    }
}