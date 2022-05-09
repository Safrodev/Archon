package safro.archon.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Hand;

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

 /*   @Override
    protected void attack(LivingEntity target, double sq) {
        double d = this.getSquaredMaxAttackDistance(target);
        if (sq <= d && this.getCooldown() <= 0) {
            this.resetCooldown();
            this.mob.swingHand(Hand.MAIN_HAND);
            this.mob.tryAttack(target);
            if (this.mob instanceof TickingAttack a) a.tickMeleeAttack(target);
        }
    } */
}
