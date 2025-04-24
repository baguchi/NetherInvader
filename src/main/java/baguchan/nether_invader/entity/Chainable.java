package baguchan.nether_invader.entity;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.network.ChainPacket;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;

public interface Chainable {
    Vec3 ELASTICITY_MULTIPLIER = new Vec3(0.8, 0.2, 0.8);
    float field_59997 = 0.7F;
    double field_59998 = 10.0F;
    double field_59999 = 0.11;
    public List<Vec3> HELD_ENTITY_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0F, 0.5F, 0.5F));
    public List<Vec3> LEASH_HOLDER_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0F, 0.5F, 0.0F));
    public List<Vec3> QUAD_LEASH_ATTACHMENT_POINTS = ImmutableList.of(new Vec3(-0.5, 0.5F, 0.5), new Vec3(-0.5, 0.5F, -0.5), new Vec3(0.5, 0.5F, -0.5), new Vec3(0.5, 0.5F, 0.5));


    @Nullable
    default ChainData readChainData(CompoundTag p_352410_) {
        if (p_352410_.contains("chain", 10)) {
            return new Chainable.ChainData(Either.left(p_352410_.getCompound("chain").getUUID("UUID")));
        } else {
            /*if (p_352410_.contains("chain", 11)) {
                Either<UUID, BlockPos> either = NbtUtils.readBlockPos(p_352410_.getCompound("chain")).<Either<UUID, BlockPos>>map(chain ->{
                    return Either.right(chain)
                }).orElse(null);
                if (either != null) {
                    return new Chainable.ChainData(either);
                }
            }*/

            return null;
        }
    }

    default void writeChainData(CompoundTag p_352349_, @Nullable Chainable.ChainData p_352363_) {
        if (p_352363_ != null) {
            Either<UUID, BlockPos> either = p_352363_.delayedChainInfo;
            /*if (p_352363_.chainHolder instanceof ChainFenceKnotEntity chainfenceknotentity) {
                either = Either.right(chainfenceknotentity.getPos());
            } else */
            if (p_352363_.chainHolder != null) {
                either = Either.left(p_352363_.chainHolder.getUUID());
            }

            if (either != null) {
                p_352349_.put("chain", either.map(p_352326_ -> {
                    CompoundTag compoundtag = new CompoundTag();
                    compoundtag.putUUID("UUID", p_352326_);
                    return compoundtag;
                }, NbtUtils::writeBlockPos));
            }
        }
    }

    default <E extends Entity & Chainable> void restoreChainFromSave(E p_352354_, Chainable.ChainData p_352106_) {
        if (p_352106_.delayedChainInfo != null && p_352354_.level() instanceof ServerLevel serverlevel) {
            Optional<UUID> optional1 = p_352106_.delayedChainInfo.left();
            Optional<BlockPos> optional = p_352106_.delayedChainInfo.right();
            if (optional1.isPresent()) {
                Entity entity = serverlevel.getEntity(optional1.get());
                if (entity != null) {
                    setChainedTo(p_352354_, entity, true);
                    return;
                }
            }

            if (p_352354_.tickCount > 100) {
                p_352354_.spawnAtLocation(Items.CHAIN);
                p_352354_.setChainData(null);
            }
        }
    }

    default <E extends Entity & Chainable> void tickChain(E p_352082_) {
        Chainable.ChainData leashable$leashdata = getChainData();
        if (leashable$leashdata != null && leashable$leashdata.delayedChainInfo != null) {
            restoreChainFromSave(p_352082_, leashable$leashdata);
        }

        if (leashable$leashdata != null && leashable$leashdata.chainHolder != null) {
            if (!p_352082_.isAlive() || !leashable$leashdata.chainHolder.isAlive()) {
                dropChain(p_352082_, true, true);
            }

            Entity entity = p_352082_.getChainHolder();
            if (entity != null && entity.level() == p_352082_.level()) {
                float f = p_352082_.distanceTo(entity);
                if (!handleChainAtDistance(entity, f)) {
                    return;
                }

                if ((double) f > 12.0) {
                    p_352082_.chainTooFarBehaviour();
                } else if ((double) f > 6.0) {
                    elasticRangeChainBehaviour((Entity & Chainable) this, entity);
                    p_352082_.checkSlowFallDistance();
                } else {
                    p_352082_.closeRangeChainBehaviour(entity);
                }
            }
        }
    }

    void closeRangeChainBehaviour(Entity entity);

    default void chainTooFarBehaviour() {
        this.dropChain(true, true);
    }

    boolean handleChainAtDistance(Entity entity, float distance);

    default void dropChain(boolean p_352294_, boolean p_352456_) {
        dropChain((Entity & Chainable) this, p_352294_, p_352456_);
    }

    default <E extends Entity & Chainable> void dropChain(E p_352163_, boolean p_352286_, boolean p_352272_) {
        Chainable.ChainData leashable$leashdata = p_352163_.getChainData();
        if (leashable$leashdata != null && leashable$leashdata.chainHolder != null) {
            p_352163_.setChainData(null);
            if (!p_352163_.level().isClientSide && p_352272_) {
                p_352163_.spawnAtLocation(Items.CHAIN);
            }

            if (p_352286_ && p_352163_.level() instanceof ServerLevel serverlevel) {
                NetherInvader.CHANNEL.send(PacketDistributor.ALL.noArg(), new ChainPacket(p_352163_, null));

            }
        }
    }

    default void setChainedTo(Entity p_352411_, boolean p_352183_) {
        setChainedTo((Entity & Chainable) this, p_352411_, p_352183_);
    }

    default <E extends Entity & Chainable> void setChainedTo(E p_352280_, Entity p_352109_, boolean p_352239_) {
        ChainData leashable$leashdata = p_352280_.getChainData();
        if (leashable$leashdata == null) {
            leashable$leashdata = new ChainData(p_352109_);
            p_352280_.setChainData(leashable$leashdata);
        } else {
            leashable$leashdata.setChainHolder(p_352109_);
        }

        if (p_352239_ && p_352280_.level() instanceof ServerLevel serverlevel) {
            NetherInvader.CHANNEL.send(PacketDistributor.ALL.noArg(), new ChainPacket(p_352280_, p_352109_));
        }

        if (p_352280_.isPassenger()) {
            p_352280_.stopRiding();
        }
    }

    @Nullable
    default Entity getChainHolder() {
        return getChainHolder((Entity & Chainable) this);
    }

    @Nullable
    default <E extends Entity & Chainable> Entity getChainHolder(E p_352466_) {
        Chainable.ChainData chainData = getChainData();
        if (chainData == null) {
            return null;
        } else {
            if (chainData.delayedChainHolderId != 0 && p_352466_.level().isClientSide) {
                Entity entity = p_352466_.level().getEntity(chainData.delayedChainHolderId);
                if (entity instanceof Entity) {
                    chainData.setChainHolder(entity);
                }
            }

            return chainData.chainHolder;
        }
    }

    default void elasticRangeChainBehaviour(Entity leashedEntity, Entity leashHolder) {
        applyElasticity(leashedEntity, leashHolder);
    }

    private boolean applyElasticity(Entity leashedEntity, Entity leashHolder) {
        boolean bl = true;
        List<Elasticity> list = calculateChainElasticities(leashedEntity, leashHolder, bl ? QUAD_LEASH_ATTACHMENT_POINTS : HELD_ENTITY_ATTACHMENT_POINT, bl ? QUAD_LEASH_ATTACHMENT_POINTS : LEASH_HOLDER_ATTACHMENT_POINT);
        if (list.isEmpty()) {
            return false;
        } else {
            Elasticity elasticity = Elasticity.sumOf(list).scale(bl ? (double) 0.25F : (double) 1.0F);
            //leashData.momentum += (double)10.0F * elasticity.torque();
            Vec3 vec3d = leashHolder.getDeltaMovement().subtract(leashedEntity.getDeltaMovement());
            leashedEntity.addDeltaMovement(elasticity.force().multiply(ELASTICITY_MULTIPLIER).add(vec3d.scale(0.11)));
            leashedEntity.setXRot(leashHolder.getXRot());
            leashedEntity.setYRot(leashHolder.getYRot());
            leashedEntity.resetFallDistance();
            return true;
        }
    }

    private static <E extends Entity & Chainable> List<Elasticity> calculateChainElasticities(Entity heldEntity, Entity leashHolder, List<Vec3> heldEntityAttachmentPoints, List<Vec3> leashHolderAttachmentPoints) {
        double d = 10F;
        Vec3 vec3d = heldEntity.getDeltaMovement();
        float f = heldEntity.getYRot() * ((float) Math.PI / 180F);
        Vec3 vec3d2 = new Vec3(heldEntity.getBbWidth(), heldEntity.getBbHeight(), heldEntity.getBbWidth());
        float g = leashHolder.getYRot() * ((float) Math.PI / 180F);
        Vec3 vec3d3 = new Vec3(leashHolder.getBbWidth(), leashHolder.getBbHeight(), leashHolder.getBbWidth());
        List<Elasticity> list = new ArrayList();

        for (int i = 0; i < heldEntityAttachmentPoints.size(); ++i) {
            Vec3 vec3d4 = heldEntityAttachmentPoints.get(i).multiply(vec3d2).yRot(-f);
            Vec3 vec3d5 = heldEntity.position().add(vec3d4);
            Vec3 vec3d6 = leashHolderAttachmentPoints.get(i).multiply(vec3d3).yRot(-g);
            Vec3 vec3d7 = leashHolder.position().add(vec3d6);
            Optional<Elasticity> var10000 = calculateChainElasticity(vec3d7, vec3d5, d, vec3d, vec3d4);
            Objects.requireNonNull(list);
            var10000.ifPresent(list::add);
        }

        return list;
    }

    private static Optional<Elasticity> calculateChainElasticity(Vec3 leashHolderAttachmentPos, Vec3 heldEntityAttachmentPos, double elasticDistance, Vec3 heldEntityMovement, Vec3 heldEntityAttachmentPoint) {
        double d = heldEntityAttachmentPos.distanceTo(leashHolderAttachmentPos);
        if (d < elasticDistance) {
            return Optional.empty();
        } else {
            Vec3 vec3d = leashHolderAttachmentPos.subtract(heldEntityAttachmentPos).normalize().scale(d - elasticDistance);
            double e = Elasticity.calculateTorque(heldEntityAttachmentPoint, vec3d);
            boolean bl = heldEntityMovement.dot(vec3d) >= (double) 0.0F;
            if (bl) {
                vec3d = vec3d.scale(0.3F);
            }

            return Optional.of(new Elasticity(vec3d, e));
        }
    }

    public record Elasticity(Vec3 force, double torque) {
        static Elasticity ZERO;

        static double calculateTorque(Vec3 force, Vec3 force2) {
            return force.z * force2.x - force.x * force2.z;
        }

        static Elasticity sumOf(List<Elasticity> elasticities) {
            if (elasticities.isEmpty()) {
                return ZERO;
            } else {
                double d = 0.0F;
                double e = 0.0F;
                double f = 0.0F;
                double g = 0.0F;

                for (Elasticity elasticity : elasticities) {
                    Vec3 vec3d = elasticity.force;
                    d += vec3d.x;
                    e += vec3d.y;
                    f += vec3d.z;
                    g += elasticity.torque;
                }

                return new Elasticity(new Vec3(d, e, f), g);
            }
        }

        public Elasticity scale(double value) {
            return new Elasticity(this.force.scale(value), this.torque * value);
        }

        public Vec3 force() {
            return this.force;
        }

        public double torque() {
            return this.torque;
        }

        static {
            ZERO = new Elasticity(Vec3.ZERO, 0.0F);
        }
    }

    ChainData getChainData();

    void setChainData(ChainData chainData);

    default void setDelayedLeashHolderId(int p_352387_) {
        this.setChainData(new ChainData(p_352387_));
        dropChain((Entity & Chainable) this, false, false);
    }


    public static final class ChainData {
        int delayedChainHolderId;
        @Nullable
        public Entity chainHolder;
        @Nullable
        public Either<UUID, BlockPos> delayedChainInfo;

        ChainData(Either<UUID, BlockPos> p_352282_) {
            this.delayedChainInfo = p_352282_;
        }

        ChainData(Entity p_352066_) {
            this.chainHolder = p_352066_;
        }

        ChainData(int p_352297_) {
            this.delayedChainHolderId = p_352297_;
        }

        public void setChainHolder(Entity p_352464_) {
            this.chainHolder = p_352464_;
            this.delayedChainInfo = null;
            this.delayedChainHolderId = 0;
        }
    }
}
