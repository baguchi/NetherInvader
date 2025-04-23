package baguchan.nether_invader.entity;

import baguchan.nether_invader.world.raid.PiglinRaid;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

public interface PiglinRaider {

    int netherInvader$getTicksOutsideRaid();

    void netherInvader$setTicksOutsideRaid(int p_37864_);


    void netherInvader$setPatrolLeader(boolean p_33076_);

    boolean netherInvader$isPatrolLeader();

    boolean netherInvader$canJoinRaid();

    void netherInvader$setCanJoinRaid(boolean p_37898_);


    default boolean netherInvader$canJoinPatrol() {
        return !this.netherInvader$hasActiveRaid();
    }

    void netherInvader$setCurrentRaid(@Nullable PiglinRaid p_37852_);

    @Nullable
    PiglinRaid netherInvader$getCurrentRaid();

    boolean netherInvader$isCaptain();

    boolean netherInvader$hasRaid();

    default boolean netherInvader$hasActiveRaid() {
        return this.netherInvader$getCurrentRaid() != null && this.netherInvader$getCurrentRaid().isActive();
    }

    void netherInvader$setWave(int p_37843_);

    int netherInvader$getWave();

    void netherInvader$applyRaidBuffs(ServerLevel level, int p37714, boolean b);
}
