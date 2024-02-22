package baguchan.nether_invader.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModSurfaceRuleData {
    private static final SurfaceRules.RuleSource NETHERRACK = makeStateRule(Blocks.NETHERRACK);
    private static final SurfaceRules.RuleSource CRIMSON_NYLIUM = makeStateRule(Blocks.CRIMSON_NYLIUM);
    private static final SurfaceRules.RuleSource SOUL_SAND = makeStateRule(Blocks.SOUL_SAND);
    private static final SurfaceRules.RuleSource SOUL_SOIL = makeStateRule(Blocks.SOUL_SOIL);

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.RuleSource surfaceCrimson = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), CRIMSON_NYLIUM), NETHERRACK);

        SurfaceRules.RuleSource crimsonLike = SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.CRIMSON_FOREST), SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, surfaceCrimson),
                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, NETHERRACK), NETHERRACK
        ));
        SurfaceRules.RuleSource netherLike = SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.NETHER_WASTES), SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, NETHERRACK),
                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, NETHERRACK), NETHERRACK
        ));
        return SurfaceRules.sequence(
                crimsonLike,
                netherLike
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}