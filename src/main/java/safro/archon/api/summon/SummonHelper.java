package safro.archon.api.summon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class SummonHelper {

    public static void createParticlesAround(LivingEntity entity, ServerWorld world) {
        for (int i = 0; i < 80; i++) {
            world.spawnParticles(ParticleTypes.LARGE_SMOKE, entity.getParticleX(0.5), entity.getRandomBodyY(), entity.getParticleZ(0.5), 1, 0.0, 0.0, 0.0, 1.0);
        }
    }

    public static void setScaledLife(LivingEntity entity, int soulPower, int base) {
        double scaled = (double)base * Math.pow(1.018, soulPower);
        int capped = (int)Math.min(scaled, base * 10.0);
        ((SummonedMob)entity).archon$setLifetime(capped);
    }
}
