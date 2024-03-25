package safro.archon.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;
import safro.archon.entity.boss.InigoEntity;

import java.util.List;

public class BurstGoal extends Goal {
    private static final TargetPredicate TARGET_PREDICATE = TargetPredicate.createAttackable().setPredicate(entity -> entity.isPlayer() || entity instanceof IronGolemEntity);
    private final InigoEntity mob;
    private final double range;
    private int ticks;

    public BurstGoal(InigoEntity mob, double range) {
        this.mob = mob;
        this.range = range;
    }

    @Override
    public boolean canStart() {
        return this.mob.getBurstCooldown() <= 0 && areTargetsInRange();
    }

    @Override
    public boolean shouldContinue() {
        return areTargetsInRange() && this.ticks < 60;
    }

    @Override
    public void start() {
        this.mob.setBurstCooldown(200);
        this.ticks = 0;
    }

    @Override
    public void tick() {
        ++this.ticks;
        this.mob.getWorld().createExplosion(this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 6.0F, World.ExplosionSourceType.NONE);
        this.mob.getWorld().sendEntityStatus(this.mob, (byte)18);
    }

    private boolean areTargetsInRange() {
        List<LivingEntity> list = getTargets(this.mob, this.range);
        return list.size() > 0;
    }

    public static List<LivingEntity> getTargets(LivingEntity mob, double range) {
        return mob.getWorld().getTargets(LivingEntity.class, TARGET_PREDICATE.setBaseMaxDistance(range), mob, mob.getBoundingBox().expand(range));
    }
}
