package baguchan.nether_invader.world.savedata;

import baguchan.nether_invader.NetherConfigs;
import baguchan.nether_invader.entity.PiglinRaider;
import baguchan.nether_invader.world.raid.PiglinRaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

public class PiglinRaidData extends SavedData {
    private static final String RAID_FILE_ID = "raids";
    public static final Codec<PiglinRaidData> CODEC = RecordCodecBuilder.create((p_400930_) -> p_400930_.group(RaidWithId.CODEC.listOf().optionalFieldOf("raids", List.of()).forGetter((p_400932_) -> p_400932_.raidMap.int2ObjectEntrySet().stream().map(RaidWithId::from).toList()), Codec.INT.fieldOf("next_id").forGetter((p_400933_) -> p_400933_.nextId), Codec.INT.fieldOf("tick").forGetter((p_400931_) -> p_400931_.tick)).apply(p_400930_, PiglinRaidData::new));
    public static final SavedDataType<PiglinRaidData> TYPE;
    public static final SavedDataType<PiglinRaidData> TYPE_END;
    private final Int2ObjectMap<PiglinRaid> raidMap = new Int2ObjectOpenHashMap();
    private int nextId = 1;
    private int tick;
    private static final Map<Level, PiglinRaidData> dataMap = new HashMap<>();


    public PiglinRaidData() {
        this.setDirty();
    }

    private PiglinRaidData(List<RaidWithId> p_401252_, int p_401028_, int p_401314_) {
        for (RaidWithId raids$raidwithid : p_401252_) {
            this.raidMap.put(raids$raidwithid.id, raids$raidwithid.raid);
        }

        this.nextId = p_401028_;
        this.tick = p_401314_;
    }

    public static PiglinRaidData get(Level world) {
        if (world instanceof ServerLevel serverLevel) {
            ServerLevel overworld = world.getServer().getLevel(world.dimension());

            PiglinRaidData fromMap = dataMap.get(overworld);
            if (fromMap == null) {
                DimensionDataStorage storage = overworld.getDataStorage();
                PiglinRaidData data = storage.computeIfAbsent(TYPE);
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

    @Nullable
    public PiglinRaid get(int p_37959_) {
        return (PiglinRaid) this.raidMap.get(p_37959_);
    }

    public OptionalInt getId(PiglinRaid p_401241_) {
        ObjectIterator var2 = this.raidMap.int2ObjectEntrySet().iterator();

        while (var2.hasNext()) {
            Int2ObjectMap.Entry<PiglinRaid> entry = (Int2ObjectMap.Entry) var2.next();
            if (entry.getValue() == p_401241_) {
                return OptionalInt.of(entry.getIntKey());
            }
        }

        return OptionalInt.empty();
    }

    public void tick(ServerLevel serverLevel) {
        ++this.tick;
        Iterator<PiglinRaid> iterator = this.raidMap.values().iterator();

        while (iterator.hasNext()) {
            PiglinRaid raid = (PiglinRaid) iterator.next();
            Identifier dimensiontype = serverLevel.dimension().identifier();
            if (!NetherConfigs.COMMON.ENABLE_DIMENSIONS.get().contains(dimensiontype.toString())) {
                raid.stop();
            }

            if (raid.isStopped()) {
                iterator.remove();
                this.setDirty();
            } else {
                raid.tick(serverLevel);
            }
        }

        if (this.tick % 200 == 0) {
            this.setDirty();
        }
    }

    public static boolean canJoinPiglinRaid(PiglinRaider p_37966_) {
        return p_37966_ instanceof Mob entity && entity.isAlive() && p_37966_.netherInvader$canJoinRaid() && entity.getNoActionTime() <= 2400;
    }

    @Nullable
    public PiglinRaid createOrExtendPiglinRaid(ServerPlayer p_37964_, BlockPos p_338602_) {
        if (p_37964_.isSpectator()) {
            return null;
        } else {
            ServerLevel serverlevel = p_37964_.level();
            Identifier dimensiontype = serverlevel.dimension().identifier();
            if (!NetherConfigs.COMMON.ENABLE_DIMENSIONS.get().contains(dimensiontype.toString())) {
                return null;
            } else {
                List<PoiRecord> list = serverlevel.getPoiManager()
                        .getInRange(p_219845_ -> p_219845_.is(PoiTypeTags.VILLAGE), p_338602_, 64, PoiManager.Occupancy.IS_OCCUPIED)
                        .toList();
                int i = 0;
                Vec3 vec3 = Vec3.ZERO;

                for (PoiRecord poirecord : list) {
                    BlockPos blockpos = poirecord.getPos();
                    vec3 = vec3.add(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                    i++;
                }

                BlockPos blockpos1;
                if (i > 0) {
                    vec3 = vec3.scale(1.0 / i);
                    blockpos1 = BlockPos.containing(vec3);
                } else {
                    blockpos1 = p_338602_;
                }

                PiglinRaid raid = this.getOrCreatePiglinRaid(serverlevel, blockpos1);
                if (!raid.isStarted() && !this.raidMap.containsValue(raid)) {
                    this.raidMap.put(this.getUniqueId(), raid);
                }

                if (!raid.isStarted() || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
                    raid.absorbRaidOmen(p_37964_);
                }

                this.setDirty();
                return raid;
            }
        }
    }

    private PiglinRaid getOrCreatePiglinRaid(ServerLevel p_37961_, BlockPos p_37962_) {
        PiglinRaid raid = getPiglinRaidAt(p_37962_);
        return raid != null ? raid : new PiglinRaid(p_37962_, p_37961_.getDifficulty());
    }

    @Nullable
    public PiglinRaid getPiglinRaidAt(BlockPos p_8833_) {
        return this.getNearbyPiglinRaid(p_8833_, 9216);
    }

    public static PiglinRaidData load(CompoundTag p_150237_) {
        return (PiglinRaidData) CODEC.parse(NbtOps.INSTANCE, p_150237_).resultOrPartial().orElseGet(PiglinRaidData::new);
    }

    private int getUniqueId() {
        return ++this.nextId;
    }

    @Nullable
    public PiglinRaid getNearbyPiglinRaid(BlockPos p_37971_, int p_37972_) {
        PiglinRaid raid = null;
        double d0 = (double) p_37972_;
        ObjectIterator var6 = this.raidMap.values().iterator();

        while (var6.hasNext()) {
            PiglinRaid raid1 = (PiglinRaid) var6.next();
            double d1 = raid1.getCenter().distSqr(p_37971_);
            if (raid1.isActive() && d1 < d0) {
                raid = raid1;
                d0 = d1;
            }
        }

        return raid;
    }

    static {
        TYPE = new SavedDataType<>("piglin_raids", PiglinRaidData::new, CODEC);
        TYPE_END = new SavedDataType<>("piglin_raids_end", PiglinRaidData::new, CODEC);
    }

    static record RaidWithId(int id, PiglinRaid raid) {
        public static final Codec<RaidWithId> CODEC = RecordCodecBuilder.create((p_401087_) -> p_401087_.group(Codec.INT.fieldOf("id").forGetter(RaidWithId::id), PiglinRaid.MAP_CODEC.forGetter(RaidWithId::raid)).apply(p_401087_, RaidWithId::new));

        RaidWithId(int id, PiglinRaid raid) {
            this.id = id;
            this.raid = raid;
        }

        public static RaidWithId from(Int2ObjectMap.Entry<PiglinRaid> p_401228_) {
            return new RaidWithId(p_401228_.getIntKey(), (PiglinRaid) p_401228_.getValue());
        }

        public int id() {
            return this.id;
        }

        public PiglinRaid raid() {
            return this.raid;
        }
    }
}
