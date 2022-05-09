package safro.archon.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class DistanceRangedGoal extends ProjectileAttackGoal {
    private final PathAwareEntity mob;
    private final float distance;

    public DistanceRangedGoal(RangedAttackMob mob, double mobSpeed, int intervalTicks, float maxShootRange, float distance) {
        super(mob, mobSpeed, intervalTicks, maxShootRange);
        this.mob = (PathAwareEntity) mob;
        this.distance = distance;
    }

    public boolean canStart() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (super.canStart()) {
            if (livingEntity != null && livingEntity.isAlive()) {
                return mob.distanceTo(livingEntity) >= distance;
            }
        }
        return false;
    }
}
