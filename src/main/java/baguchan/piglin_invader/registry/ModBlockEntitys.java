package baguchan.piglin_invader.registry;

import baguchan.piglin_invader.PiglinInvader;
import baguchan.piglin_invader.blockentity.NetherReactorBlockEntity;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntitys {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PiglinInvader.MODID);

    public static final RegistryObject<BlockEntityType<NetherReactorBlockEntity>> NETHER_REACTOR = BLOCK_ENTITIES.register("nether_reactor", () -> register("piglin_invader:nether_reactor", BlockEntityType.Builder.of(NetherReactorBlockEntity::new,
            ModBlocks.NETHER_REACTOR.get())));

    private static <T extends BlockEntity> BlockEntityType<T> register(String p_200966_0_, BlockEntityType.Builder<T> p_200966_1_) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, p_200966_0_);
        return p_200966_1_.build(type);
    }
}