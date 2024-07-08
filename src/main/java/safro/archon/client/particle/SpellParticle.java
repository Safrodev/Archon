package safro.archon.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class SpellParticle extends SpriteBillboardParticle {
    static final Random RANDOM = Random.create();
    private final SpriteProvider spriteProvider;
    private final float size;

    public SpellParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float size, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.collidesWithWorld = false;
        this.velocityX = velocityX * 2.0F;
        this.velocityY = velocityY * 2.0F;
        this.velocityZ = velocityZ * 2.0F;
        this.size = size;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getBrightness(float tint) {
        return 255;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);

        float ageFraction = (float)this.age / (float)this.maxAge;
        this.scale = this.size - ageFraction * this.size;
        this.prevAngle = this.angle;
        this.angle += 1.0F;
    }

    public static class Factory implements ParticleFactory<SpellParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SpellParticleEffect effect, ClientWorld clientWorld, double x, double y, double z, double vx, double vy, double vz) {
            SpellParticle particle = new SpellParticle(clientWorld, x, y, z, vx, vy, vz, effect.size(), this.spriteProvider);
            particle.setColor(color(effect.r()), color(effect.g()), color(effect.b()));
            particle.setMaxAge(clientWorld.random.nextInt(10) + 10);
            return particle;
        }

        private static float color(float a) {
            return a + MathHelper.nextFloat(RANDOM, -0.05F, 0.05F);
        }
    }
}
