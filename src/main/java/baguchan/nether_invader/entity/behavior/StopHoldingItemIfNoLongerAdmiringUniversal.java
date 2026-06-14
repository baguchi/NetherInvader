package baguchan.nether_invader.entity.behavior;

import baguchan.nether_invader.entity.ai.PiglinWarriorAi;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;

public class StopHoldingItemIfNoLongerAdmiringUniversal {
    public static BehaviorControl<AbstractPiglin> create() {
        return BehaviorBuilder.create(i -> i.group(i.absent(MemoryModuleType.ADMIRING_ITEM)).apply(i, admiring -> (level, body, timestamp) -> {
            if (!body.getOffhandItem().isEmpty() && !body.getOffhandItem().has(DataComponents.BLOCKS_ATTACKS)) {
                PiglinWarriorAi.stopHoldingOffHandItem(level, body, true);
                return true;
            } else {
                return false;
            }
        }));
    }
}