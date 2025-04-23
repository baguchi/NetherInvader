package baguchan.nether_invader.world.savedata;

import baguchan.nether_invader.NetherConfigs;
import baguchan.nether_invader.entity.PiglinRaider;
import baguchan.nether_invader.world.raid.PiglinRaid;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PiglinRaidData extends SavedData {

    private static final String IDENTIFIER = "piglin_raid_world_data";
    private final Map<Integer, PiglinRaid> raidMap = Maps.newHashMap();
    private final ServerLevel level;
    private int nextAvailableID;
    private int tick;
    private static Map<Level, PiglinRaidData> dataMap = new HashMap<>();

    public PiglinRaidData(ServerLevel p_300199_) {
        super();
        this.level = p_300199_;
        this.nextAvailableID = 1;
        this.setDirty();
    }

    public PiglinRaid get(int p_37959_) {
        return this.raidMap.get(p_37959_);
    }

    public void tick() {
        this.tick++;
        Iterator<PiglinRaid> iterator = this.raidMap.values().iterator();

        while (iterator.hasNext()) {
            PiglinRaid raid = iterator.next();
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
                raid.stop();
            }

            if (raid.isStopped()) {
                iterator.remove();
                this.setDirty();
            } else {
                raid.tick();
            }
        }

        if (this.tick % 200 == 0) {
            this.setDirty();
        }
    }

    public static boolean canJoinRaid(AbstractPiglin p_37966_, PiglinRaid p_37967_) {
        return p_37966_ != null && p_37967_ != null && p_37967_.getLevel() != null && p_37967_ instanceof PiglinRaider piglinRaider
                ? p_37966_.isAlive()
                && piglinRaider.netherInvader$canJoinRaid()
                && p_37966_.getNoActionTime() <= 2400
                && p_37966_.level().dimensionType() == p_37967_.getLevel().dimensionType()
                : false;
    }

    @Nullable
    public PiglinRaid createOrExtendRaid(ServerPlayer p_37964_, BlockPos p_338602_) {
        if (p_37964_.isSpectator()) {
            return null;
        } else if (this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
            return null;
        } else {
            ResourceKey<Level> dimension = p_37964_.level().dimension();
            if (!NetherConfigs.COMMON.ENABLE_DIMENSIONS.get().contains(dimension.location().toString())) {
                return null;
            } else {
                List<PoiRecord> list = this.level
                        .getPoiManager()
                        .getInRange(p_219845_ -> p_219845_.is(PoiTypeTags.VILLAGE), p_338602_, 64, PoiManager.Occupancy.IS_OCCUPIED)
                        .toList();
                int i = 0;
                Vec3 vec3 = Vec3.ZERO;

                for (PoiRecord poirecord : list) {
                    BlockPos blockpos = poirecord.getPos();
                    vec3 = vec3.add((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
                    i++;
                }

                BlockPos blockpos1;
                if (i > 0) {
                    vec3 = vec3.scale(1.0 / (double) i);
                    blockpos1 = BlockPos.containing(vec3);
                } else {
                    blockpos1 = p_338602_;
                }

                PiglinRaid raid = this.getOrCreateRaid(p_37964_.serverLevel(), blockpos1);
                if (!raid.isStarted() && !this.raidMap.containsKey(raid.getId())) {
                    this.raidMap.put(raid.getId(), raid);
                }

                if (!raid.isStarted() || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
                    raid.absorbRaidOmen(p_37964_);
                }

                this.setDirty();
                return raid;
            }
        }
    }

    private PiglinRaid getOrCreateRaid(ServerLevel p_37961_, BlockPos p_37962_) {
        PiglinRaid raid = PiglinRaidData.get(this.level).getNearbyRaid(p_37962_, 9216);
        return raid != null ? raid : new PiglinRaid(this.getUniqueId(), p_37961_, p_37962_);
    }

    public static String getFileId(Holder<DimensionType> p_211597_) {
        return p_211597_.is(BuiltinDimensionTypes.END) ? "raids_end" : "raids";
    }

    private int getUniqueId() {
        return ++this.nextAvailableID;
    }

    @Nullable
    public PiglinRaid getNearbyRaid(BlockPos p_37971_, int p_37972_) {
        PiglinRaid raid = null;
        double d0 = (double) p_37972_;

        for (PiglinRaid raid1 : this.raidMap.values()) {
            double d1 = raid1.getCenter().distSqr(p_37971_);
            if (raid1.isActive() && d1 < d0) {
                raid = raid1;
                d0 = d1;
            }
        }

        return raid;
    }

    public static PiglinRaidData get(Level world) {
        if (world instanceof ServerLevel serverLevel) {
            ServerLevel overworld = world.getServer().getLevel(world.dimension());
            PiglinRaidData fromMap = dataMap.get(overworld);
            if (fromMap == null) {
                DimensionDataStorage storage = overworld.getDataStorage();
                PiglinRaidData data = storage.computeIfAbsent(PiglinRaidData.factory(serverLevel), IDENTIFIER);
                if (data != null) {
                    data.setDirty();
                }
                dataMap.put(world, data);
                return data;
            }
            return fromMap;
        }
        return null;
    }

    public static SavedData.Factory<PiglinRaidData> factory(ServerLevel p_300199_) {
        return new SavedData.Factory<>(() -> {
            return new PiglinRaidData(p_300199_);
        }, (p_296865_, provider) -> {
            return load(p_300199_, p_296865_);
        });
    }

    public static PiglinRaidData load(ServerLevel p_300199_, CompoundTag nbt) {
        PiglinRaidData data = new PiglinRaidData(p_300199_);
        data.nextAvailableID = nbt.getInt("NextAvailableID");
        data.tick = nbt.getInt("Tick");
        ListTag listtag = nbt.getList("PiglinRaidData", 10);

        for (int i = 0; i < listtag.size(); i++) {
            CompoundTag compoundtag = listtag.getCompound(i);
            PiglinRaid raid = new PiglinRaid(p_300199_, compoundtag);
            data.raidMap.put(raid.getId(), raid);
        }

        return data;
    }


    @Override
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider p_323640_) {
        compound.putInt("NextAvailableID", this.nextAvailableID);
        compound.putInt("Tick", this.tick);
        ListTag listtag = new ListTag();

        for (PiglinRaid raid : this.raidMap.values()) {
            CompoundTag compoundtag = new CompoundTag();
            raid.save(compoundtag);
            listtag.add(compoundtag);
        }

        compound.put("PiglinRaidData", listtag);
        return compound;
    }

    @Nullable
    public PiglinRaid getRaidAt(BlockPos p_8833_) {
        return this.getNearbyRaid(p_8833_, 9216);
    }
}