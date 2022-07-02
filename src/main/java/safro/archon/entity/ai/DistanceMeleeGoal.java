package safro.archon.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class DistanceMeleeGoal extends MeleeAttackGoal {
    private final PathAwareEntity mob;
    private final float distance;

    public DistanceMeleeGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle, float distance) {
        super(mob, speed, pauseWhenMobIdle);
        this.mob = mob;
        this.distance = distance;
    }

    public boolean canStart() {
        LivingEntity livingEntity = mob.getTarget();
        if (super.canStart()) {
            if (livingEntity != null && livingEntity.isAlive()) {
                return mob.distanceTo(livingEntity) < distance;
            }
        }
        return false;
    }
}
