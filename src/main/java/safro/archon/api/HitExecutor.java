package safro.archon.api;

import net.minecraft.entity.LivingEntity;
import safro.archon.entity.projectile.spell.SpellProjectileEntity;

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
