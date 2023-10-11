package baguchan.nether_invader.blockentity;

import baguchan.nether_invader.NetherConfigs;
import baguchan.nether_invader.block.NetherReactorBlock;
import baguchan.nether_invader.registry.ModBlockEntitys;
import baguchan.nether_invader.utils.NetherSpeader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class NetherReactorBlockEntity extends BlockEntity {
    private final NetherSpeader netherSpreader = NetherSpeader.createLevelSpreader();

    private int tick;
    private boolean active;
    private int summonCooldown = 200;

    public NetherReactorBlockEntity(BlockPos p_222774_, BlockState p_222775_) {
        super(ModBlockEntitys.NETHER_REACTOR.get(), p_222774_, p_222775_);
    }


    public static void serverTick(Level p_222780_, BlockPos p_222781_, BlockState p_222782_, NetherReactorBlockEntity p_222783_) {
        if (p_222783_.active && p_222783_.tick > 3600) {
            p_222783_.active = false;
            p_222780_.playSound(null, p_222781_, SoundEvents.RESPAWN_ANCHOR_DEPLETE.get(), SoundSource.BLOCKS, 2.0F, 1.0F);

            p_222780_.setBlock(p_222781_, p_222782_.setValue(NetherReactorBlock.ACTIVE, false), 3);
        } else if (p_222783_.active) {
            if (p_222783_.summonCooldown > 0) {
                --p_222783_.summonCooldown;
            } else {
                p_222783_.summon(p_222780_, p_222781_);
            }
            if (p_222783_.netherSpreader.getCursors().isEmpty() || p_222783_.netherSpreader.getCursors().size() < 4) {
                p_222783_.netherSpreader.addCursors(p_222781_, 3);
            }

            if (p_222783_.tick % 2 == 0) {
                p_222783_.netherSpreader.updateCursors(p_222780_, p_222781_, p_222780_.getRandom(), true);
            }
            ++p_222783_.tick;
            p_222780_.destroyBlockProgress(0, p_222783_.getBlockPos(), (p_222783_.tick * 10 / 3600));
        } else {
            p_222783_.active = p_222782_.getValue(NetherReactorBlock.ACTIVE);
        }
    }

    public void summon(Level p_222780_, BlockPos pos) {
        if (p_222780_ instanceof ServerLevel serverLevel) {
            int count = 0;
            for (int i = 0; i < 16; ++i) {

                BlockPos blockPos = pos.offset(-10 + p_222780_.random.nextInt(20), -10 + p_222780_.random.nextInt(20), -10 + p_222780_.random.nextInt(20));
                if (serverLevel.isEmptyBlock(blockPos.above()) && serverLevel.isEmptyBlock(blockPos.above(2)) && !serverLevel.isEmptyBlock(blockPos.below())) {
                    Entity piglin = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(NetherConfigs.COMMON.nether_reactor_spawn_whitelist.get().get(p_222780_.random.nextInt(NetherConfigs.COMMON.nether_reactor_spawn_whitelist.get().size())))).create(serverLevel);
                    if (level.random.nextInt(16) == 0) {
                        piglin = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(NetherConfigs.COMMON.nether_reactor_spawn_whitelist.get().get(p_222780_.random.nextInt(NetherConfigs.COMMON.nether_reactor_spawn_whitelist.get().size())))).create(serverLevel);
                    }

                    if (!(piglin instanceof Mob mob)) continue;

                    piglin.moveTo(blockPos, 0.0f, 0.0f);
                    mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);

                    List<Player> player = level.getNearbyPlayers(TargetingConditions.DEFAULT, mob, mob.getBoundingBox().inflate(32));
                    if (!player.isEmpty()) {
                        Player target = player.get(level.random.nextInt(player.size()));
                        mob.setTarget(target);
                        if (mob.getBrain().getMemories().containsKey(MemoryModuleType.ANGRY_AT)) {
                            mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 600L);
                        } else if (mob.getBrain().getMemories().containsKey(MemoryModuleType.ATTACK_TARGET)) {
                            mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 600L);
                        }
                    }
                    serverLevel.addFreshEntityWithPassengers(piglin);
                    count += 1;
                    if (count >= 3) {
                        break;
                    }
                }
            }
        }
        this.summonCooldown = 200;
    }

    public void load(CompoundTag p_222787_) {
        this.netherSpreader.load(p_222787_);
        this.active = p_222787_.getBoolean("active");
        this.summonCooldown = p_222787_.getInt("summonCooldown");
    }

    protected void saveAdditional(CompoundTag p_222789_) {
        this.netherSpreader.save(p_222789_);
        super.saveAdditional(p_222789_);
        p_222789_.putBoolean("active", this.active);
        p_222789_.putInt("summonCooldown", this.summonCooldown);
    }
}