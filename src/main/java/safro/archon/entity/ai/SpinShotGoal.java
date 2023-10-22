package safro.archon.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.math.MathHelper;
import safro.archon.entity.boss.NullEntity;

import java.util.List;

public class SpinShotGoal extends Goal {
    private final NullEntity mob;
    private int ticks;

    public SpinShotGoal(NullEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return areTargetsInRange() && this.mob.getSpinCooldown() <= 0;
    }

    @Override
    public boolean shouldContinue() {
        return this.ticks < 60 && areTargetsInRange();
    }

    @Override
    public void start() {
        this.mob.setSpinCooldown(200);
        this.ticks = 0;
    }

    @Override
    public void tick() {
        ++this.ticks;
        this.mob.setYaw(MathHelper.wrapDegrees(this.mob.getYaw() + 30.0F));
        if (this.ticks % 4 == 0) {
            DragonFireballEntity fireball = new DragonFireballEntity(this.mob.getWorld(), this.mob, 0.0D, 0.0D, 0.0D);
            fireball.refreshPositionAndAngles(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), 0.0F, 0.0F);
            fireball.setVelocity(this.mob, this.mob.getPitch(), this.mob.getYaw(), 0.0F, 3.0F, 1.0F);
            this.mob.getWorld().spawnEntity(fireball);
        }
    }

    private boolean areTargetsInRange() {
        List<LivingEntity> list = BurstGoal.getTargets(this.mob, 32.0D);
        return list.size() > 0;
    }
}
