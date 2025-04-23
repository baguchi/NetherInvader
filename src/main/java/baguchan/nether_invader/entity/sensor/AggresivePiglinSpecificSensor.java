package baguchan.nether_invader.entity.sensor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AggresivePiglinSpecificSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_LIVING_ENTITIES,
                MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
                MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD,
                MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,
                MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN,
                MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN,
                MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS,
                MemoryModuleType.NEARBY_ADULT_PIGLINS,
                MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT,
                MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT,
                MemoryModuleType.NEAREST_REPELLENT
        );
    }

    @Override
    protected void doTick(ServerLevel p_26726_, LivingEntity p_26727_) {
        Brain<?> brain = p_26727_.getBrain();
        Optional<Mob> optional = Optional.empty();
        Optional<Piglin> optional3 = Optional.empty();
        Optional<Player> optional5 = Optional.empty();
        int i = 0;
        List<AbstractPiglin> list = Lists.newArrayList();
        List<AbstractPiglin> list1 = Lists.newArrayList();
        NearestVisibleLivingEntities nearestvisiblelivingentities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                .orElse(NearestVisibleLivingEntities.empty());

        for (LivingEntity livingentity : nearestvisiblelivingentities.findAll(p_186157_ -> true)) {
            if (livingentity instanceof PiglinBrute piglinbrute) {
                list.add(piglinbrute);
            } else if (livingentity instanceof Piglin) {
                Piglin piglin = (Piglin) livingentity;
                if (piglin.isBaby() && optional3.isEmpty()) {
                    optional3 = Optional.of(piglin);
                } else if (piglin.isAdult()) {
                    list.add(piglin);
                }
            } else {
                if (livingentity instanceof WitherSkeleton || livingentity instanceof WitherBoss || livingentity instanceof AbstractVillager) {
                    if (optional.isEmpty() || optional.get().distanceTo(p_26727_) > livingentity.distanceTo(p_26727_)) {
                        optional = Optional.of((Mob) livingentity);
                    }
                }
            }
        }

        for (LivingEntity livingentity1 : brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of())) {
            if (livingentity1 instanceof AbstractPiglin) {
                AbstractPiglin abstractpiglin = (AbstractPiglin) livingentity1;
                if (abstractpiglin.isAdult()) {
                    list1.add(abstractpiglin);
                }
            }
        }

        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional);
        brain.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list1);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, list);
        brain.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, list.size());
        brain.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, i);
    }


}
