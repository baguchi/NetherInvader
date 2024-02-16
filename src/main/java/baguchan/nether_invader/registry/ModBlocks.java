package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.block.NetherReactorBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, NetherInvader.MODID);

    public static final Supplier<Block> NETHER_REACTOR = register("nether_reactor", () -> new NetherReactorBlock(BlockBehaviour.Properties.of().strength(5.0F, 10.0F).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK)));


    private static <T extends Block> Supplier<T> baseRegister(String name, Supplier<? extends T> block, Function<Supplier<T>, Supplier<? extends Item>> item) {
        Supplier<T> register = BLOCKS.register(name, block);
        Supplier<? extends Item> itemSupplier = item.apply(register);
        ModItems.ITEMS.register(name, itemSupplier);
        return register;
    }

    private static <T extends Block> Supplier<T> noItemRegister(String name, Supplier<? extends T> block) {
        Supplier<T> register = BLOCKS.register(name, block);
        return register;
    }

    private static <B extends Block> Supplier<B> register(String name, Supplier<? extends Block> block) {
        return (Supplier<B>) baseRegister(name, block, (object) -> ModBlocks.registerBlockItem(object));
    }

    private static <T extends Block> Supplier<BlockItem> registerBlockItem(final Supplier<T> block) {
        return () -> {
            return new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties());
        };
    }
}
