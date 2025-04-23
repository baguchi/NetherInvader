package baguchan.nether_invader.effect;

import baguchan.nether_invader.world.raid.PiglinRaid;
import baguchan.nether_invader.world.savedata.PiglinRaidData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PiglinRaidEffect extends MobEffect {
    public PiglinRaidEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_295828_, int p_295171_) {
        return true;
    }


    @Override
    public boolean applyEffectTick(ServerLevel p_376587_, LivingEntity p_19467_, int p_19468_) {
        if (p_19467_ instanceof ServerPlayer serverplayer && !serverplayer.isSpectator()) {
            ServerLevel serverlevel = serverplayer.serverLevel();
            if (serverlevel.getDifficulty() != Difficulty.PEACEFUL && serverlevel.isVillage(serverplayer.blockPosition())) {
                PiglinRaid raid = PiglinRaidData.get(serverlevel).getPiglinRaidAt(serverplayer.blockPosition());
                if (raid == null || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
                    PiglinRaidData.get(serverlevel).createOrExtendPiglinRaid(serverplayer, serverplayer.blockPosition());
                    return false;
                }
            }
        }

        return true;
    }
}
