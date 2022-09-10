package safro.archon.entity.projectile;

/**
 * Used for running actions when a SpellProjectile is ticking
 * Mainly for particle spawning
 */
public interface TickExecutor {
    TickExecutor EMPTY = new TickExecutor() {
        @Override
        public void onTick(SpellProjectileEntity projectile) {}
    };

    void onTick(SpellProjectileEntity projectile);
}
