package safro.archon.entity.projectile;

import net.minecraft.entity.LivingEntity;

public interface HitExecutor {
    HitExecutor EMPTY = new HitExecutor() {
        @Override
        public void onHit(LivingEntity target, LivingEntity owner) {}
    };

    void onHit(LivingEntity target, LivingEntity owner);
}
