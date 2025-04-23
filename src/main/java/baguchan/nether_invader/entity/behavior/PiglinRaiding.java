package baguchan.nether_invader.entity.behavior;

import baguchan.nether_invader.entity.PiglinRaider;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;

public class PiglinRaiding {
    public static OneShot<AbstractPiglin> create(float speed) {
        return BehaviorBuilder.create(
                p_260278_ -> p_260278_.group(
                                p_260278_.registered(MemoryModuleType.WALK_TARGET)
                        )
                        .apply(
                                p_260278_,
                                (p_260206_) -> (p_259617_, p_260038_, p_259374_) -> {
                                    if (p_260038_ instanceof PiglinRaider piglinRaider
                                            && piglinRaider.netherInvader$getCurrentRaid() != null) {
                                        p_260206_.set(new WalkTarget(piglinRaider.netherInvader$getCurrentRaid().getCenter(), speed, 4));
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                        )
        );
    }
}
