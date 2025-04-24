package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.entity.AgressivePiglin;
import baguchan.nether_invader.entity.ChainedGhast;
import baguchan.nether_invader.entity.Scaffolding;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = NetherInvader.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntitys {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, NetherInvader.MODID);


    public static final RegistryObject<EntityType<ChainedGhast>> CHAINED_GHAST = ENTITIES_REGISTRY.register("chained_ghast", () -> EntityType.Builder.of(ChainedGhast::new, MobCategory.MONSTER).fireImmune()
            .sized(4.0F, 4.0F)
            .clientTrackingRange(10).build(prefix("chained_ghast")));
    public static final RegistryObject<EntityType<Scaffolding>> SCAFFOLDING = ENTITIES_REGISTRY.register("scaffolding", () -> EntityType.Builder.of(Scaffolding::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10).build(prefix("scaffolding")));
    public static final RegistryObject<EntityType<AgressivePiglin>> AGRESSIVE_PIGLIN = ENTITIES_REGISTRY.register("agressive_piglin", () -> EntityType.Builder.of(AgressivePiglin::new, MobCategory.MONSTER)
            .sized(0.6F, 1.95F)
            .clientTrackingRange(8).build(prefix("agressive_piglin")));


    @SubscribeEvent
    public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
        event.put(CHAINED_GHAST.get(), ChainedGhast.createAttributes().build());
        event.put(SCAFFOLDING.get(), Scaffolding.createAttributes().build());
        event.put(AGRESSIVE_PIGLIN.get(), AgressivePiglin.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerEntityPlace(SpawnPlacementRegisterEvent event) {
        event.register(CHAINED_GHAST.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ChainedGhast::checkChainGhastSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(AGRESSIVE_PIGLIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractPiglin::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }

    private static String prefix(String path) {
        return NetherInvader.MODID + path;
    }
}