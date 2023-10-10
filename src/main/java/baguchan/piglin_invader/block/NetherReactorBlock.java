package baguchan.piglin_invader.block;

import baguchan.piglin_invader.blockentity.NetherReactorBlockEntity;
import baguchan.piglin_invader.registry.ModBlockEntitys;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class NetherReactorBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public NetherReactorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, Boolean.valueOf(false)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_222211_) {
        p_222211_.add(ACTIVE);
    }


    public InteractionResult use(BlockState p_54524_, Level p_54525_, BlockPos p_54526_, Player p_54527_, InteractionHand p_54528_, BlockHitResult p_54529_) {
        if (!p_54524_.getValue(ACTIVE)) {
            p_54525_.setBlock(p_54526_, p_54524_.setValue(ACTIVE, true), 3);
            p_54525_.playSound(null, p_54526_, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 2.0F, 1.0F);
            return InteractionResult.sidedSuccess(p_54525_.isClientSide);
        } else {
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
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
