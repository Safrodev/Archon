package safro.archon.entity.ai;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import safro.archon.entity.boss.NullEntity;
import safro.archon.api.spell.HitExecutor;
import safro.archon.entity.projectile.TerrainEntity;

import java.util.EnumSet;
import java.util.List;

public class ShardShotGoal extends Goal {
    private final NullEntity mob;
    private int ticks = 0;

    public ShardShotGoal(NullEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return areTargetsInRange() && this.mob.getShardCooldown() <= 0;
    }

    @Override
    public boolean shouldContinue() {
        return this.ticks <= 60 && areTargetsInRange();
    }

    @Override
    public void start() {
        this.mob.setShardCooldown(200);
        this.ticks = 0;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.ticks % 20 == 0) {
            this.mob.addVelocity(0.0D, 0.5D, 0.0D);
            this.mob.getWorld().sendEntityStatus(this.mob, (byte)12);
            List<LivingEntity> list = BurstGoal.getTargets(this.mob, 32.0D).stream().filter(this.mob.getVisibilityCache()::canSee).toList();
            list.forEach(entity -> {
                this.mob.lookAtEntity(entity, 180.0F, 180.0F);
                this.mob.getWorld().spawnEntity(this.createShard(entity));
            });
        }
        this.ticks++;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    private TerrainEntity createShard(LivingEntity livingEntity) {
        double e = livingEntity.getX() - this.mob.getX();
        double f = livingEntity.getBodyY(0.5D) - this.mob.getBodyY(0.5D);
        double g = livingEntity.getZ() - this.mob.getZ();
        double h = Math.sqrt(Math.sqrt(this.mob.squaredDistanceTo(livingEntity))) * 0.5D;
        HitExecutor executor = (target1, owner, projectile) -> {
            target1.damage(owner.getWorld().getDamageSources().mobProjectile(projectile, owner), 20.0F);
            target1.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0, false, false));
        };

        TerrainEntity shard = new TerrainEntity(this.mob.getWorld(), this.mob, executor, Blocks.OBSIDIAN);
        shard.setPosition(shard.getX(), this.mob.getBodyY(0.5D) + 0.5D, shard.getZ());
        shard.setVelocity(this.mob.getRandom().nextTriangular(e, 2.297D * h), f, this.mob.getRandom().nextTriangular(g, 2.297D * h));
        shard.setBlock(Blocks.OBSIDIAN);
        shard.setCustomNameVisible(false);
        shard.setCustomName(Text.of("NullShard"));
        return shard;
    }

    private boolean areTargetsInRange() {
        List<LivingEntity> list = BurstGoal.getTargets(this.mob, 32.0D);
        return list.size() > 0;
    }
}
