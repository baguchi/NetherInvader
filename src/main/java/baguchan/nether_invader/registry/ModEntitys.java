package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = NetherInvader.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntitys {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, NetherInvader.MODID);


    public static final DeferredHolder<EntityType<?>, EntityType<NetherSpreader>> NETHER_SPREADER = ENTITIES_REGISTRY.register("nether_spreader", () -> EntityType.Builder.of(NetherSpreader::new, MobCategory.MISC).sized(1.5F, 2.5F).clientTrackingRange(12).fireImmune().build(prefix("nether_spreader")));
    public static final DeferredHolder<EntityType<?>, EntityType<ChainedGhast>> CHAINED_GHAST = ENTITIES_REGISTRY.register("chained_ghast", () -> EntityType.Builder.of(ChainedGhast::new, MobCategory.MONSTER).fireImmune()
            .sized(4.0F, 4.0F)
            .eyeHeight(2.6F)
            .passengerAttachments(4.0625F)
            .ridingOffset(0.5F)
            .clientTrackingRange(10).build(prefix("chained_ghast")));
    public static final DeferredHolder<EntityType<?>, EntityType<Scaffolding>> SCAFFOLDING = ENTITIES_REGISTRY.register("scaffolding", () -> EntityType.Builder.of(Scaffolding::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .eyeHeight(0.5625F)
            .clientTrackingRange(10).build(prefix("scaffolding")));
    public static final DeferredHolder<EntityType<?>, EntityType<AgressivePiglin>> AGRESSIVE_PIGLIN = ENTITIES_REGISTRY.register("agressive_piglin", () -> EntityType.Builder.of(AgressivePiglin::new, MobCategory.MONSTER)
            .sized(0.6F, 1.95F)
            .eyeHeight(1.79F)
            .passengerAttachments(2.0125F)
            .ridingOffset(-0.7F)
            .clientTrackingRange(8).build(prefix("agressive_piglin")));
    public static final DeferredHolder<EntityType<?>, EntityType<BastionGeneral>> BASTION_GENERAL = ENTITIES_REGISTRY.register("bastion_general", () -> EntityType.Builder.of(BastionGeneral::new, MobCategory.MONSTER)
            .sized(0.9F, 2.15F)
            .eyeHeight(1.85F)
            .passengerAttachments(2.0125F)
            .ridingOffset(-0.7F)
            .clientTrackingRange(8).build(prefix("bastion_general")));


    @SubscribeEvent
    public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
        event.put(NETHER_SPREADER.get(), NetherSpreader.createAttributes().build());
        event.put(CHAINED_GHAST.get(), ChainedGhast.createAttributes().build());
        event.put(SCAFFOLDING.get(), Scaffolding.createAttributes().build());
        event.put(AGRESSIVE_PIGLIN.get(), AgressivePiglin.createAttributes().build());
        event.put(BASTION_GENERAL.get(), BastionGeneral.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerEntityPlace(RegisterSpawnPlacementsEvent event) {
        event.register(CHAINED_GHAST.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChainedGhast::checkChainGhastSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AGRESSIVE_PIGLIN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractPiglin::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(BASTION_GENERAL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractPiglin::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }

    private static String prefix(String path) {
        return NetherInvader.MODID + path;
    }
}