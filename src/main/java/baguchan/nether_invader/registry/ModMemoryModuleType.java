package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

public class ModMemoryModuleType {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, NetherInvader.MODID);
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<List<LivingEntity>>> NEAREST_VISIBLE_ENEMY = MEMORY_MODULE_TYPES.register("nearest_visible_enemy", () -> new MemoryModuleType<>(Optional.empty()));

}