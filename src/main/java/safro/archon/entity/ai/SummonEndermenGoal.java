package safro.archon.entity.ai;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import safro.archon.entity.boss.NullEntity;

import java.util.EnumSet;

public class SummonEndermenGoal extends Goal {
    private final NullEntity mob;
    private int ticks = 0;

    public SummonEndermenGoal(NullEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        return this.mob.getTarget() != null && this.mob.getTarget().isAlive() && this.mob.getSummonCooldown() <= 0;
    }

    @Override
    public boolean shouldContinue() {
        return this.ticks <= 100 && this.mob.getTarget() != null;
    }

    @Override
    public void tick() {
        if (this.ticks % 20 == 0) {
            this.mob.swingHand(Hand.OFF_HAND);
            LivingEntity target = this.mob.getTarget();

            EndermanEntity enderman = EntityType.ENDERMAN.create(this.mob.getWorld());
            enderman.setTarget(target);
            enderman.setProvoked();
            enderman.setPosition(this.mob.getX(), this.mob.getWorld().getTopY(), this.mob.getZ());
            this.mob.getWorld().spawnEntity(enderman);

            for (int i = 0; i < 16; i++) {
                double x = this.mob.getX() + (this.mob.getRandom().nextDouble() - 0.5D) * 24.0D;
                double y = MathHelper.clamp(this.mob.getPos().getY() + (double) (this.mob.getRandom().nextInt(8) - 4), this.mob.getWorld().getBottomY(), this.mob.getWorld().getBottomY() + this.mob.getWorld().getHeight() - 1);
                double z = this.mob.getPos().getZ() + (this.mob.getRandom().nextDouble() - 0.5D) * 24.0D;

                if (enderman.teleport(x, y, z, false)) {
                    break;
                }
            }
        }
        this.ticks++;
    }

    @Override
    public void start() {
        this.mob.setSummonCooldown(400);
        this.ticks = 0;
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }
}
