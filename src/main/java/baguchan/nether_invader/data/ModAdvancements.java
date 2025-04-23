package baguchan.nether_invader.data;

import baguchan.nether_invader.criterion.PiglinSlayerTrigger;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancements extends AdvancementProvider {
    public ModAdvancements(PackOutput output,
                           CompletableFuture<HolderLookup.Provider> lookupProvider,
                           ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new AdvancementGen()));
    }

    private static final class AdvancementGen implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
            AdvancementHolder root = Advancement.Builder.advancement()
                    .display(
                            new ItemStack(Items.PIGLIN_BRUTE_SPAWN_EGG),
                            Component.translatable("advancements.nether_invader.root.title"),
                            Component.translatable("advancements.nether_invader.root.description"),
                            // The background texture. Use null if you don't want a background texture (for non-root advancements).
                            ResourceLocation.withDefaultNamespace("textures/block/chiseled_polished_blackstone.png"),
                            AdvancementType.TASK,// The frame type. Valid values are AdvancementType.TASK, CHALLENGE, or GOAL.
                            true, //Toast
                            true, //chat
                            false //hidden or not
                    )
                    .addCriterion("temp", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.PIGLIN_BRUTE)))
                    .save(consumer,
                            ResourceLocation.fromNamespaceAndPath("nether_invader", "root"),
                            existingFileHelper);

            AdvancementHolder piglinSlayer = Advancement.Builder.advancement()
                    .display(
                            Items.PIGLIN_HEAD,
                            Component.translatable("advancements.nether_invader.piglin_slayer.title"),
                            Component.translatable("advancements.nether_invader.piglin_slayer.desc"),
                            null,
                            AdvancementType.CHALLENGE, true, true, false
                    )
                    .parent(root)
                    .addCriterion("temp", PiglinSlayerTrigger.get())
                    .save(consumer,
                            ResourceLocation.fromNamespaceAndPath("nether_invader", "piglin_slayer"),
                            existingFileHelper);
        }
    }
}