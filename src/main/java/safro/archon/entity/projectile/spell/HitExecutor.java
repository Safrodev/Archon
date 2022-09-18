package safro.archon.entity.projectile.spell;

import net.minecraft.entity.LivingEntity;

/**
 * Used for running actions when a SpellProjectile hits an entity
 */
public interface HitExecutor {
    HitExecutor EMPTY = new HitExecutor() {
        @Override
        public void onHit(LivingEntity target, LivingEntity owner, SpellProjectileEntity projectile) {}
    };

    void onHit(LivingEntity target, LivingEntity owner, SpellProjectileEntity projectile);
}
