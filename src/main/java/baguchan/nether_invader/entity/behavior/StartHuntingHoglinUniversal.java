package baguchan.nether_invader.entity.behavior;

import baguchan.nether_invader.entity.ai.PiglinWarriorAi;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.StartHuntingHoglin;

public class StartHuntingHoglinUniversal {
    public static OneShot<AbstractPiglin> create() {
        return BehaviorBuilder.create(
                i -> i.group(
                                i.present(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN),
                                i.absent(MemoryModuleType.ANGRY_AT),
                                i.absent(MemoryModuleType.HUNTED_RECENTLY),
                                i.registered(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS)
                        )
                        .apply(i, (huntable, angryAt, huntedRecently, nearestPiglins) -> (level, body, timestamp) -> {
                            if (!body.isBaby() && !i.tryGet(nearestPiglins).filter(p -> p.stream().anyMatch(StartHuntingHoglin::hasHuntedRecently)).isPresent()) {
                                Hoglin target = i.get(huntable);
                                PiglinWarriorAi.setAngerTarget(level, body, target);
                                PiglinWarriorAi.dontKillAnyMoreHoglinsForAWhile(body);
                                PiglinWarriorAi.broadcastAngerTarget(level, body, target);
                                i.tryGet(nearestPiglins).ifPresent(p -> p.forEach(PiglinAi::dontKillAnyMoreHoglinsForAWhile));
                                return true;
                            } else {
                                return false;
                            }
                        })
        );
    }

    private static boolean hasHuntedRecently(AbstractPiglin otherPiglin) {
        return otherPiglin.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY);
    }
}
