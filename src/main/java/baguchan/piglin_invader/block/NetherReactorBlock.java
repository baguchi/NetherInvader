package baguchan.piglin_invader.block;

import baguchan.piglin_invader.blockentity.NetherReactorBlockEntity;
import baguchan.piglin_invader.registry.ModBlockEntitys;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class NetherReactorBlock extends BaseEntityBlock {
    public NetherReactorBlock(Properties properties) {
        super(properties);
    }

    @javax.annotation.Nullable
    public BlockEntity newBlockEntity(BlockPos p_222117_, BlockState p_222118_) {
        return new NetherReactorBlockEntity(p_222117_, p_222118_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_222100_, BlockState p_222101_, BlockEntityType<T> p_222102_) {
        return p_222100_.isClientSide ? null : createTickerHelper(p_222102_, ModBlockEntitys.NETHER_REACTOR.get(), NetherReactorBlockEntity::serverTick);
    }

    public RenderShape getRenderShape(BlockState p_222120_) {
        return RenderShape.MODEL;
    }

}
