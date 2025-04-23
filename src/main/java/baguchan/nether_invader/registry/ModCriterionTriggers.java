package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.criterion.PiglinSlayerTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCriterionTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> CRITERIONS_REGISTER = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, NetherInvader.MODID);
    public static final DeferredHolder<CriterionTrigger<?>, PiglinSlayerTrigger> PIGLIN_SLAYER_TRIGGER = CRITERIONS_REGISTER.register(PiglinSlayerTrigger.ID.getPath(), () -> new PiglinSlayerTrigger());


}
