package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.entity.NetherSpreader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = NetherInvader.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntitys {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, NetherInvader.MODID);


    public static final Supplier<EntityType<NetherSpreader>> NETHER_SPREADER = ENTITIES_REGISTRY.register("nether_spreader", () -> EntityType.Builder.of(NetherSpreader::new, MobCategory.MISC).sized(1.5F, 2.5F).clientTrackingRange(12).fireImmune().build(prefix("nether_spreader")));

    @SubscribeEvent
    public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
        event.put(NETHER_SPREADER.get(), NetherSpreader.createAttributes().build());
    }

    private static String prefix(String path) {
        return NetherInvader.MODID + path;
    }
}