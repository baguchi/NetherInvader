package baguchan.nether_invader.effect;

import baguchan.nether_invader.registry.ModPotions;
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
    public boolean applyEffectTick(LivingEntity p_296327_, int p_294357_) {
        if (p_296327_ instanceof ServerPlayer serverplayer && !serverplayer.isSpectator()) {
            ServerLevel serverlevel = serverplayer.serverLevel();
            if (serverlevel.getDifficulty() != Difficulty.PEACEFUL && serverlevel.isVillage(serverplayer.blockPosition())) {
                PiglinRaid raid = PiglinRaidData.get(serverlevel).getRaidAt(serverplayer.blockPosition());
                if (raid == null || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
                    PiglinRaidData.get(serverlevel).createOrExtendRaid(serverplayer, serverplayer.blockPosition());
                    serverplayer.removeEffect(ModPotions.PIGLIN_OMEN);

                    return false;
                }
            }
        }

        return true;
    }
}
