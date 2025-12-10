package baguchan.nether_invader.entity;

import baguchan.nether_invader.network.ChainPacket;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Chainable {
    String LEASH_TAG = "chain";
    double LEASH_TOO_FAR_DIST = 12.0F;
    double LEASH_ELASTIC_DIST = 6.0F;
    double MAXIMUM_ALLOWED_LEASHED_DIST = 16.0F;
    Vec3 AXIS_SPECIFIC_ELASTICITY = new Vec3(0.8, 0.2, 0.8);
    float SPRING_DAMPENING = 0.7F;
    double TORSIONAL_ELASTICITY = 10.0F;
    double STIFFNESS = 0.11;
    List<Vec3> ENTITY_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0F, 0.5F, 0.5F));
    List<Vec3> LEASHER_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0F, 0.5F, 0.0F));
    List<Vec3> SHARED_QUAD_ATTACHMENT_POINTS = ImmutableList.of(new Vec3(-0.5F, 0.5F, 0.5F), new Vec3(-0.5F, 0.5F, -0.5F), new Vec3(0.5F, 0.5F, -0.5F), new Vec3(0.5F, 0.5F, 0.5F));

    @Nullable
    Chainable.ChainData getChainData();

    void setChainData(@Nullable Chainable.ChainData var1);

    default boolean isChained() {
        return this.getChainData() != null && this.getChainData().chainHolder != null;
    }

    default boolean mayBeChained() {
        return this.getChainData() != null;
    }

    default boolean canHaveAChainAttachedTo(Entity p_418026_) {
        if (this == p_418026_) {
            return false;
        } else {
            return !(this.leashDistanceTo(p_418026_) > this.chainSnapDistance()) && this.canBeChained();
        }
    }

    default double leashDistanceTo(Entity p_418359_) {
        return p_418359_.getBoundingBox().getCenter().distanceTo(((Entity) this).getBoundingBox().getCenter());
    }

    default boolean canBeChained() {
        return true;
    }

    default void setDelayedChainHolderId(int p_352387_) {
        this.setChainData(new Chainable.ChainData(p_352387_));
        dropChain((Entity & Chainable) this, false, false);
    }

    default void readChainData(ValueInput p_422278_) {
        Chainable.ChainData leashable$leashdata = p_422278_.read("chain", ChainData.CODEC).orElse(null);
        if (this.getChainData() != null && leashable$leashdata == null) {
            this.removeChain();
        }

        this.setChainData(leashable$leashdata);
    }

    default void writeChainData(ValueOutput p_422090_, @Nullable Chainable.ChainData p_352363_) {
        p_422090_.storeNullable("chain", Chainable.ChainData.CODEC, p_352363_);
    }

    private static <E extends Entity & Chainable> void restoreChainFromSave(E p_352354_, Chainable.ChainData p_352106_) {
        if (p_352106_.delayedChainInfo != null) {
            Level var3 = p_352354_.level();
            if (var3 instanceof ServerLevel serverlevel) {
                Optional<UUID> optional1 = p_352106_.delayedChainInfo.left();
                Optional<BlockPos> optional = p_352106_.delayedChainInfo.right();
                if (optional1.isPresent()) {
                    Entity entity = serverlevel.getEntity(optional1.get());
                    if (entity != null) {
                        setChainedTo(p_352354_, entity, true);
                        return;
                    }
                } else if (optional.isPresent()) {
                    //setChainedTo(p_352354_, ChainFenceKnotEntity.getOrCreateKnot(serverlevel, (BlockPos)optional.get()), true);
                    return;
                }

                if (p_352354_.tickCount > 100) {
                    p_352354_.spawnAtLocation(serverlevel, Items.IRON_CHAIN);
                    p_352354_.setChainData(null);
                }
            }
        }

    }

    default void dropChain() {
        dropChain((Entity & Chainable) this, true, true);
    }

    default void removeChain() {
        dropChain((Entity & Chainable) this, true, false);
    }

    default void onChainRemoved() {
    }

    private static <E extends Entity & Chainable> void dropChain(E p_352163_, boolean p_352286_, boolean p_352272_) {
        Chainable.ChainData leashable$leashdata = p_352163_.getChainData();
        if (leashable$leashdata != null && leashable$leashdata.chainHolder != null) {
            p_352163_.setChainData(null);
            p_352163_.onChainRemoved();
            Level var5 = p_352163_.level();
            if (var5 instanceof ServerLevel serverlevel) {
                if (p_352272_) {
                    p_352163_.spawnAtLocation(serverlevel, Items.IRON_CHAIN);
                }

                if (p_352286_) {
                    PacketDistributor.sendToAllPlayers(new ChainPacket(p_352163_, null));
                }
            }
        }

    }

    static <E extends Entity & Chainable> void tickChain(ServerLevel p_376374_, E p_352082_) {
        Chainable.ChainData leashable$leashdata = p_352082_.getChainData();
        if (leashable$leashdata != null && leashable$leashdata.delayedChainInfo != null) {
            restoreChainFromSave(p_352082_, leashable$leashdata);
        }

        if (leashable$leashdata != null && leashable$leashdata.chainHolder != null) {
            if (!p_352082_.canInteractWithLevel() || !leashable$leashdata.chainHolder.canInteractWithLevel()) {
                if (p_376374_.getGameRules().get(GameRules.ENTITY_DROPS)) {
                    p_352082_.dropChain();
                } else {
                    p_352082_.removeChain();
                }
            }

            Entity entity = p_352082_.getChainHolder();
            if (entity != null && entity.level() == p_352082_.level()) {
                double d0 = p_352082_.leashDistanceTo(entity);
                p_352082_.whenChainedTo(entity);
                if (d0 > p_352082_.chainSnapDistance()) {
                    p_376374_.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.LEAD_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    p_352082_.chainTooFarBehaviour();
                } else if (d0 > p_352082_.leashElasticDistance() - (double) entity.getBbWidth() - (double) p_352082_.getBbWidth() && p_352082_.checkElasticInteractions(entity, leashable$leashdata)) {
                    p_352082_.onElasticChainPull();
                } else {
                    p_352082_.closeRangeChainBehaviour(entity);
                }

                p_352082_.setYRot((float) ((double) p_352082_.getYRot() - leashable$leashdata.angularMomentum));
                leashable$leashdata.angularMomentum *= angularFriction(p_352082_);
            }
        }

    }

    default void onElasticChainPull() {
        Entity entity = (Entity) this;
        entity.checkFallDistanceAccumulation();
    }

    default double chainSnapDistance() {
        return 12.0F;
    }

    default double leashElasticDistance() {
        return 6.0F;
    }

    static <E extends Entity & Chainable> float angularFriction(E p_418015_) {
        if (p_418015_.onGround()) {
            BlockPos groundPos = p_418015_.getBlockPosBelowThatAffectsMyMovement();
            return p_418015_.level().getBlockState(groundPos).getFriction(p_418015_.level(), groundPos, p_418015_) * 0.91F;
        } else {
            return p_418015_.isInLiquid() ? 0.8F : 0.91F;
        }
    }

    default void whenChainedTo(Entity p_418473_) {
    }

    default void chainTooFarBehaviour() {
        this.dropChain();
    }

    default void closeRangeChainBehaviour(Entity p_352073_) {
    }

    default boolean checkElasticInteractions(Entity p_418481_, Chainable.ChainData p_418158_) {
        boolean flag = p_418481_.supportQuadLeashAsHolder() && this.supportQuadChain();
        List<Chainable.Wrench> list = computeElasticInteraction((Entity & Chainable) this, p_418481_, flag ? SHARED_QUAD_ATTACHMENT_POINTS : ENTITY_ATTACHMENT_POINT, flag ? SHARED_QUAD_ATTACHMENT_POINTS : LEASHER_ATTACHMENT_POINT);
        if (list.isEmpty()) {
            return false;
        } else {
            Chainable.Wrench leashable$wrench = Chainable.Wrench.accumulate(list).scale(flag ? (double) 0.25F : (double) 1.0F);
            p_418158_.angularMomentum += (double) 10.0F * leashable$wrench.torque();
            Vec3 vec3 = getHolderMovement(p_418481_).subtract(((Entity) this).getKnownMovement());
            ((Entity) this).addDeltaMovement(leashable$wrench.force().multiply(AXIS_SPECIFIC_ELASTICITY).add(vec3.scale(0.11)));
            return true;
        }
    }

    private static Vec3 getHolderMovement(Entity p_426117_) {
        Vec3 var10000;
        if (p_426117_ instanceof Mob mob) {
            if (mob.isNoAi()) {
                var10000 = Vec3.ZERO;
                return var10000;
            }
        }

        var10000 = p_426117_.getKnownMovement();
        return var10000;
    }

    private static <E extends Entity & Chainable> List<Chainable.Wrench> computeElasticInteraction(E p_418297_, Entity p_418456_, List<Vec3> p_418351_, List<Vec3> p_418397_) {
        double d0 = p_418297_.leashElasticDistance();
        Vec3 vec3 = getHolderMovement(p_418297_);
        float f = p_418297_.getYRot() * ((float) Math.PI / 180F);
        Vec3 vec31 = new Vec3(p_418297_.getBbWidth(), p_418297_.getBbHeight(), p_418297_.getBbWidth());
        float f1 = p_418456_.getYRot() * ((float) Math.PI / 180F);
        Vec3 vec32 = new Vec3(p_418456_.getBbWidth(), p_418456_.getBbHeight(), p_418456_.getBbWidth());
        List<Chainable.Wrench> list = new ArrayList();

        for (int i = 0; i < p_418351_.size(); ++i) {
            Vec3 vec33 = p_418351_.get(i).multiply(vec31).yRot(-f);
            Vec3 vec34 = p_418297_.position().add(vec33);
            Vec3 vec35 = p_418397_.get(i).multiply(vec32).yRot(-f1);
            Vec3 vec36 = p_418456_.position().add(vec35);
            Optional<Chainable.Wrench> var10000 = computeDampenedSpringInteraction(vec36, vec34, d0, vec3, vec33);
            Objects.requireNonNull(list);
            var10000.ifPresent(list::add);
        }

        return list;
    }

    private static Optional<Chainable.Wrench> computeDampenedSpringInteraction(Vec3 p_418217_, Vec3 p_418024_, double p_418081_, Vec3 p_418190_, Vec3 p_418069_) {
        double d0 = p_418024_.distanceTo(p_418217_);
        if (d0 < p_418081_) {
            return Optional.empty();
        } else {
            Vec3 vec3 = p_418217_.subtract(p_418024_).normalize().scale(d0 - p_418081_);
            double d1 = Chainable.Wrench.torqueFromForce(p_418069_, vec3);
            boolean flag = p_418190_.dot(vec3) >= (double) 0.0F;
            if (flag) {
                vec3 = vec3.scale(0.3F);
            }

            return Optional.of(new Chainable.Wrench(vec3, d1));
        }
    }

    default boolean supportQuadChain() {
        return false;
    }

    default Vec3[] getQuadChainOffsets() {
        return createQuadChainOffsets((Entity) this, (double) 0.0F, (double) 0.5F, (double) 0.5F, (double) 0.5F);
    }

    static Vec3[] createQuadChainOffsets(Entity p_418142_, double p_418244_, double p_418078_, double p_418298_, double p_418121_) {
        float f = p_418142_.getBbWidth();
        double d0 = p_418244_ * (double) f;
        double d1 = p_418078_ * (double) f;
        double d2 = p_418298_ * (double) f;
        double d3 = p_418121_ * (double) p_418142_.getBbHeight();
        return new Vec3[]{new Vec3(-d2, d3, d1 + d0), new Vec3(-d2, d3, -d1 + d0), new Vec3(d2, d3, -d1 + d0), new Vec3(d2, d3, d1 + d0)};
    }

    default Vec3 getChainOffset(float p_418480_) {
        return this.getChainOffset();
    }

    default Vec3 getChainOffset() {
        Entity entity = (Entity) this;
        return new Vec3(0.0F, entity.getEyeHeight(), entity.getBbWidth() * 0.4F);
    }

    default void setChainedTo(Entity p_352411_, boolean p_352183_) {
        if (this != p_352411_) {
            setChainedTo((Entity & Chainable) this, p_352411_, p_352183_);
        }

    }

    private static <E extends Entity & Chainable> void setChainedTo(E p_352280_, Entity p_352109_, boolean p_352239_) {
        Chainable.ChainData leashable$leashdata = p_352280_.getChainData();
        if (leashable$leashdata == null) {
            leashable$leashdata = new Chainable.ChainData(p_352109_);
            p_352280_.setChainData(leashable$leashdata);
        } else {
            Entity entity = leashable$leashdata.chainHolder;
            leashable$leashdata.setChainHolder(p_352109_);
            if (entity != null && entity != p_352109_) {
            }
        }

            Level var5 = p_352280_.level();
            if (var5 instanceof ServerLevel serverlevel) {
                PacketDistributor.sendToAllPlayers(new ChainPacket(p_352280_, p_352109_));
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
    private static <E extends Entity & Chainable> Entity getChainHolder(E p_352466_) {
        Chainable.ChainData leashable$leashdata = p_352466_.getChainData();
        if (leashable$leashdata == null) {
            return null;
        } else {
            if (leashable$leashdata.delayedChainHolderId != 0 && p_352466_.level().isClientSide()) {
                Entity entity = p_352466_.level().getEntity(leashable$leashdata.delayedChainHolderId);
                if (entity instanceof Entity) {
                    leashable$leashdata.setChainHolder(entity);
                }
            }

            return leashable$leashdata.chainHolder;
        }
    }

    static List<Chainable> leashableChainedTo(Entity p_418021_) {
        return leashableInArea(p_418021_, (p_418528_) -> p_418528_.getChainHolder() == p_418021_);
    }

    static List<Chainable> leashableInArea(Entity p_418133_, Predicate<Chainable> p_418334_) {
        return leashableInArea(p_418133_.level(), p_418133_.getBoundingBox().getCenter(), p_418334_);
    }

    static List<Chainable> leashableInArea(Level p_418478_, Vec3 p_418494_, Predicate<Chainable> p_418347_) {
        double d0 = 32.0F;
        AABB aabb = AABB.ofSize(p_418494_, 32.0F, 32.0F, 32.0F);
        Stream var10000 = p_418478_.getEntitiesOfClass(Entity.class, aabb, (p_418131_) -> {
            boolean b1;
            if (p_418131_ instanceof Chainable leashable) {
                if (p_418347_.test(leashable)) {
                    b1 = true;
                    return b1;
                }
            }

            b1 = false;
            return b1;
        }).stream();
        Objects.requireNonNull(Chainable.class);
        return var10000.map(o -> o).toList();
    }

    final class ChainData {
        public static final Codec<Chainable.ChainData> CODEC;
        int delayedChainHolderId;
        @Nullable
        public Entity chainHolder;
        @Nullable
        public Either<UUID, BlockPos> delayedChainInfo;
        public double angularMomentum;

        private ChainData(Either<UUID, BlockPos> p_352282_) {
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

        static {
            CODEC = Codec.xor(UUIDUtil.CODEC.fieldOf("UUID").codec(), BlockPos.CODEC).xmap(Chainable.ChainData::new, (p_412912_) -> {
                return p_412912_.chainHolder != null ? Either.left(p_412912_.chainHolder.getUUID()) : (Either) Objects.requireNonNull(p_412912_.delayedChainInfo, "Invalid ChainData had no attachment");

            });
        }
    }

    record Wrench(Vec3 force, double torque) {
        static Chainable.Wrench ZERO;

        static double torqueFromForce(Vec3 p_418322_, Vec3 p_418094_) {
            return p_418322_.z * p_418094_.x - p_418322_.x * p_418094_.z;
        }

        static Chainable.Wrench accumulate(List<Chainable.Wrench> p_418210_) {
            if (p_418210_.isEmpty()) {
                return ZERO;
            } else {
                double d0 = 0.0F;
                double d1 = 0.0F;
                double d2 = 0.0F;
                double d3 = 0.0F;

                for (Chainable.Wrench leashable$wrench : p_418210_) {
                    Vec3 vec3 = leashable$wrench.force;
                    d0 += vec3.x;
                    d1 += vec3.y;
                    d2 += vec3.z;
                    d3 += leashable$wrench.torque;
                }

                return new Chainable.Wrench(new Vec3(d0, d1, d2), d3);
            }
        }

        public Chainable.Wrench scale(double p_418466_) {
            return new Chainable.Wrench(this.force.scale(p_418466_), this.torque * p_418466_);
        }

        static {
            ZERO = new Chainable.Wrench(Vec3.ZERO, 0.0F);
        }
    }
}
