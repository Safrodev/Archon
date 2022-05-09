package safro.archon.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.BowItem;
import safro.archon.entity.boss.TarEntity;

import java.util.EnumSet;

public class DropRocksGoal extends Goal {
    private final TarEntity actor;
    private final double speed;
    private int attackInterval;
    private final float squaredRange;
    private int cooldown = -1;
    private int targetSeeingTicker;
    private boolean movingToLeft;
    private boolean backward;
    private int combatTicks = -1;

    public DropRocksGoal(TarEntity actor, double speed, int attackInterval, float range) {
        this.actor = actor;
        this.speed = speed;
        this.attackInterval = attackInterval;
        this.squaredRange = range * range;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    public void setAttackInterval(int attackInterval) {
        this.attackInterval = attackInterval;
    }

    public boolean canStart() {
        return this.actor.getTarget() != null && this.actor.distanceTo(this.actor.getTarget()) > 10;
    }

    public boolean shouldContinue() {
        return (this.canStart() || !this.actor.getNavigation().isIdle()) && this.actor.distanceTo(this.actor.getTarget()) > 10;
    }

    public void start() {
        super.start();
        this.actor.setAttacking(true);
    }

    public void stop() {
        super.stop();
        this.actor.setAttacking(false);
        this.targetSeeingTicker = 0;
        this.cooldown = -1;
        this.actor.clearActiveItem();
    }

    public boolean shouldRunEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingEntity = this.actor.getTarget();
        if (livingEntity != null) {
            double d = this.actor.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            boolean bl = this.actor.getVisibilityCache().canSee(livingEntity);
            boolean bl2 = this.targetSeeingTicker > 0;
            if (bl != bl2) {
                this.targetSeeingTicker = 0;
            }

            if (bl) {
                ++this.targetSeeingTicker;
            } else {
                --this.targetSeeingTicker;
            }

            if (!(d > (double)this.squaredRange) && this.targetSeeingTicker >= 20) {
                this.actor.getNavigation().stop();
                ++this.combatTicks;
            } else {
                this.actor.getNavigation().startMovingTo(livingEntity, this.speed);
                this.combatTicks = -1;
            }

            if (this.combatTicks >= 20) {
                if ((double)this.actor.getRandom().nextFloat() < 0.3D) {
                    this.movingToLeft = !this.movingToLeft;
                }

                if ((double)this.actor.getRandom().nextFloat() < 0.3D) {
                    this.backward = !this.backward;
                }

                this.combatTicks = 0;
            }

            if (this.combatTicks > -1) {
                if (d > (double)(this.squaredRange * 0.75F)) {
                    this.backward = false;
                } else if (d < (double)(this.squaredRange * 0.25F)) {
                    this.backward = true;
                }

                this.actor.getMoveControl().strafeTo(this.backward ? -0.5F : 0.5F, this.movingToLeft ? 0.5F : -0.5F);
                this.actor.lookAtEntity(livingEntity, 30.0F, 30.0F);
            } else {
                this.actor.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);
            }

            if (this.actor.isUsingItem()) {
                if (!bl && this.targetSeeingTicker < -60) {
                    this.actor.clearActiveItem();
                } else if (bl) {
                    int i = this.actor.getItemUseTime();
                    if (i >= 20) {
                        this.actor.clearActiveItem();
                        ((RangedAttackMob)this.actor).attack(livingEntity, BowItem.getPullProgress(i));
                        this.cooldown = this.attackInterval;
                    }
                }
            } else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {

            }

        }
    }
}
