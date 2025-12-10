package baguchan.nether_invader.registry;

import baguchan.nether_invader.NetherInvader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> MOVE_SPREAD = create("move_spread");
    public static final TagKey<Block> REPLACEABLE_FOR_REACTOR = create("replaceable_for_reactor");

    private static TagKey<Block> create(String p_203847_) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NetherInvader.MODID, p_203847_));
    }
}
