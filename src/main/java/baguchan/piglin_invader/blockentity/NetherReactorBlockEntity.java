package baguchan.piglin_invader.blockentity;

import baguchan.piglin_invader.block.NetherReactorBlock;
import baguchan.piglin_invader.registry.ModBlockEntitys;
import baguchan.piglin_invader.utils.NetherSpeader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NetherReactorBlockEntity extends BlockEntity {
    private final NetherSpeader netherSpreader = NetherSpeader.createLevelSpreader();

    private int tick;
    private boolean active;

    public NetherReactorBlockEntity(BlockPos p_222774_, BlockState p_222775_) {
        super(ModBlockEntitys.NETHER_REACTOR.get(), p_222774_, p_222775_);
    }


    public static void serverTick(Level p_222780_, BlockPos p_222781_, BlockState p_222782_, NetherReactorBlockEntity p_222783_) {
        if (p_222783_.active && p_222783_.tick > 2400) {
            p_222780_.playSound(null, p_222781_, SoundEvents.RESPAWN_ANCHOR_DEPLETE.get(), SoundSource.BLOCKS, 2.0F, 1.0F);

            p_222780_.removeBlock(p_222781_, true);
        } else if (p_222783_.active) {
            if (p_222783_.netherSpreader.getCursors().isEmpty() || p_222783_.netherSpreader.getCursors().size() < 2) {
                p_222783_.netherSpreader.addCursors(p_222781_, 5);
            }

            if (p_222783_.tick % 4 == 0) {
                p_222783_.netherSpreader.updateCursors(p_222780_, p_222781_, p_222780_.getRandom(), true);
            }
            ++p_222783_.tick;
            p_222780_.destroyBlockProgress(0, p_222783_.getBlockPos(), 1);
        } else {
            p_222783_.active = p_222782_.getValue(NetherReactorBlock.ACTIVE);
        }
    }

    public void load(CompoundTag p_222787_) {
        this.netherSpreader.load(p_222787_);
        this.active = p_222787_.getBoolean("active");
    }

    protected void saveAdditional(CompoundTag p_222789_) {
        this.netherSpreader.save(p_222789_);
        super.saveAdditional(p_222789_);
        p_222789_.putBoolean("active", this.active);
    }
}