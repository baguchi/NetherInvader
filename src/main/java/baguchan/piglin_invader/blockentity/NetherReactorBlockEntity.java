package baguchan.piglin_invader.blockentity;

import baguchan.piglin_invader.registry.ModBlockEntitys;
import baguchan.piglin_invader.utils.NetherSpeader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NetherReactorBlockEntity extends BlockEntity {
    private final NetherSpeader netherSpreader = NetherSpeader.createLevelSpreader();

    public NetherReactorBlockEntity(BlockPos p_222774_, BlockState p_222775_) {
        super(ModBlockEntitys.NETHER_REACTOR.get(), p_222774_, p_222775_);
    }

    public static void serverTick(Level p_222780_, BlockPos p_222781_, BlockState p_222782_, NetherReactorBlockEntity p_222783_) {
        if (p_222783_.netherSpreader.getCursors().isEmpty() || p_222783_.netherSpreader.getCursors().size() < 3) {
            p_222783_.netherSpreader.addCursors(p_222781_.relative(Direction.UP, 1), 5);
        }

        p_222783_.netherSpreader.updateCursors(p_222780_, p_222781_, p_222780_.getRandom(), true);
    }

    public void load(CompoundTag p_222787_) {
        this.netherSpreader.load(p_222787_);
    }

    protected void saveAdditional(CompoundTag p_222789_) {
        this.netherSpreader.save(p_222789_);
        super.saveAdditional(p_222789_);
    }
}