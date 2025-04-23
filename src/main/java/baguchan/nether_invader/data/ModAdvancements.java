package baguchan.nether_invader.data;

import baguchan.nether_invader.criterion.PiglinSlayerTrigger;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancements extends AdvancementProvider {
    public ModAdvancements(PackOutput output,
                           CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, List.of(new AdvancementGen()));
    }

    private static final class AdvancementGen implements AdvancementSubProvider {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {
            HolderGetter<EntityType<?>> holdergetter = provider.lookupOrThrow(Registries.ENTITY_TYPE);

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
                    .addCriterion("temp", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(holdergetter, EntityType.PIGLIN_BRUTE)))
                    .save(consumer,
                            ResourceLocation.fromNamespaceAndPath("nether_invader", "root"));

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
                            ResourceLocation.fromNamespaceAndPath("nether_invader", "piglin_slayer"));
        }
    }
}