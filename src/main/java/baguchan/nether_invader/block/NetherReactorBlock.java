package baguchan.nether_invader.block;

import baguchan.nether_invader.blockentity.NetherReactorBlockEntity;
import baguchan.nether_invader.registry.ModBlockEntitys;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_222211_) {
        p_222211_.add(ACTIVE);
    }


    public InteractionResult use(BlockState p_54524_, Level p_54525_, BlockPos p_54526_, Player p_54527_, InteractionHand p_54528_, BlockHitResult p_54529_) {
        if (!p_54524_.getValue(ACTIVE) && isGoodCondition(p_54525_, p_54526_)) {
            this.changeBlocks(p_54525_, p_54526_);
            p_54525_.setBlock(p_54526_, p_54524_.setValue(ACTIVE, true), 3);
            p_54525_.playSound(null, p_54526_, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 2.0F, 1.0F);
            return InteractionResult.sidedSuccess(p_54525_.isClientSide);
        } else {
            return super.use(p_54524_, p_54525_, p_54526_, p_54527_, p_54528_, p_54529_);
        }
    }

    public void changeBlocks(LevelAccessor world, BlockPos pos) {
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (!world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos).is(this)) {
                world.setBlock(blockPos, Blocks.BLACKSTONE.defaultBlockState(), 3);
                        }
        }
    }

    public boolean isGoodCondition(LevelAccessor world, BlockPos pos) {
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x < 1; x++) {
                for (int z = -1; z < 1; z++) {
                    if (y == 0) {
                        if (x == 0 && z == 0) {
                            if (!world.getBlockState(pos.offset(x, y, z)).is(this)) {
                                return false;
                            }
                        } else {
                            if (x == 0 && z != 0 || z == 0 && x != 0) {
                                if (!world.getBlockState(pos.offset(x, y, z)).isAir()) {
                                    return false;
                                }
                            } else {
                                if (!world.getBlockState(pos.offset(x, y, z)).is(Blocks.GILDED_BLACKSTONE)) {
                                    return false;
                                }
                            }

                        }
                    } else if (y == 1) {
                        if (x == 0 || z == 0) {
                            if (!world.getBlockState(pos.offset(x, y, z)).is(Blocks.GILDED_BLACKSTONE)) {
                                return false;
                            }

                        } else {
                            if (!world.getBlockState(pos.offset(x, y, z)).isAir()) {
                                return false;
                            }
                        }
                    } else {
                        if (x == 0 || z == 0) {
                            if (!world.getBlockState(pos.offset(x, y, z)).is(Blocks.GILDED_BLACKSTONE)) {
                                return false;
                            }
                        } else {
                            if (!world.getBlockState(pos.offset(x, y, z)).is(Blocks.GOLD_BLOCK)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
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
