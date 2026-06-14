package baguchan.nether_invader.entity.behavior;

import baguchan.nether_invader.world.raid.PiglinRaid;
import baguchan.nether_invader.world.savedata.PiglinRaidData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.schedule.Activity;

public class SetPiglinRaidStatus {
    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(i -> i.point((level, body, timestamp) -> {
            if (level.getRandom().nextInt(20) != 0) {
                return false;
            } else {
                Brain<?> brain = body.getBrain();
                PiglinRaid nearbyRaid = PiglinRaidData.get(level).getPiglinRaidAt(body.blockPosition());
                if (nearbyRaid != null) {
                    if (nearbyRaid.hasFirstWaveSpawned() && !nearbyRaid.isBetweenWaves()) {
                        brain.setDefaultActivity(Activity.RAID);
                        brain.setActiveActivityIfPossible(Activity.RAID);
                    } else {
                        brain.setDefaultActivity(Activity.PRE_RAID);
                        brain.setActiveActivityIfPossible(Activity.PRE_RAID);
                    }
                }

                return true;
            }
        }));
    }
}
