package baguchan.nether_invader.entity.behavior;

import baguchi.bagus_lib.entity.brain.behaviors.AttackWithAnimation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GeneralAttack<E extends PathfinderMob> extends AttackWithAnimation<E> {
    public GeneralAttack(int actionPoint, int attackLength, int cooldownBetweenAttacks, float speed) {
        super(actionPoint, attackLength, cooldownBetweenAttacks, speed);
    }

    @Override
    protected void doAttack(E attacker, LivingEntity living) {
        Level var3 = attacker.level();

        if (var3 instanceof ServerLevel serverLevel) {
            List<LivingEntity> entitiesHit = serverLevel.getEntitiesOfClass(LivingEntity.class, getAttackBoundingBox(attacker));
            for (LivingEntity entity : entitiesHit) {
                if (entity != attacker) {
                    if (attacker.canAttack(entity) && !attacker.isAlliedTo(entity)) {
                        Vec3 vec3 = entity.getEyePosition();
                        Vec3 yVector = attacker.calculateViewVector(attacker.getXRot(), attacker.getYHeadRot());
                        Vec3 vec32 = vec3.subtract(attacker.getEyePosition());
                        Vec3 vec33 = (new Vec3(vec32.x, vec32.y, vec32.z)).normalize();
                        double d0 = Math.acos(vec33.dot(yVector));
                        if (resolveAttack(d0, 180)) {
                            attacker.doHurtTarget(serverLevel, entity);
                        }
                    }
                }
            }
        }
    }

    public AABB getAttackBoundingBox(PathfinderMob attacker) {
        Entity entity = attacker.getVehicle();
        AABB aabb;
        if (entity != null) {
            AABB aabb1 = entity.getBoundingBox();
            AABB aabb2 = attacker.getBoundingBox();
            aabb = new AABB(Math.min(aabb2.minX, aabb1.minX), aabb2.minY, Math.min(aabb2.minZ, aabb1.minZ), Math.max(aabb2.maxX, aabb1.maxX), aabb2.maxY, Math.max(aabb2.maxZ, aabb1.maxZ));
        } else {
            aabb = attacker.getBoundingBox();
        }

        return aabb.inflate(Math.sqrt(2.04F) - 0.6F, 0.0F, Math.sqrt(2.04F) - 0.6F);
    }

    public boolean resolveAttack(double yRot, double yRotAttackRange) {
        return !(yRot > (((float) Math.PI / 180F) * yRotAttackRange));
    }
}
