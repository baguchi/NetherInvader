package baguchan.nether_invader.criterion;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.registry.ModCriterionTriggers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class PiglinSlayerTrigger extends SimpleCriterionTrigger<PiglinSlayerTrigger.Instance> {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(NetherInvader.MODID, "piglin_slayer");

    public void trigger(ServerPlayer player) {
        this.trigger(player, (instance) -> true);
    }

    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public record Instance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<PiglinSlayerTrigger.Instance> CODEC = RecordCodecBuilder.create((p_311988_) -> {
            return p_311988_.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(PiglinSlayerTrigger.Instance::player)).apply(p_311988_, PiglinSlayerTrigger.Instance::new);
        });

        @Override
        public Optional<ContextAwarePredicate> player() {
            return this.player;
        }
    }

    public static Criterion<Instance> get() {
        return ModCriterionTriggers.PIGLIN_SLAYER_TRIGGER.get().createCriterion(new PiglinSlayerTrigger.Instance(Optional.empty()));
    }
}