package safro.archon.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class InfernoLaserParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    protected InfernoLaserParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f);
        this.spriteProvider = spriteProvider;
        this.velocityX = 0;
        this.velocityY = 0;
        this.velocityZ = 0;
        this.red = 1;
        this.green = 0;
        this.blue = 0;
        this.maxAge = 2 + this.random.nextInt(2);
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
        this.scale(1.0F);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getSize(float tickDelta) {
        return this.scale * MathHelper.clamp(((float)this.age + tickDelta) / (float)this.maxAge * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.setSpriteForAge(this.spriteProvider);
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.96F;
            this.velocityY *= 0.96F;
            this.velocityZ *= 0.96F;
            PlayerEntity playerentity = this.world.getClosestPlayer(this.x, this.y, this.z, 2.0D, false);
            if (playerentity != null) {
                double d0 = playerentity.getY();
                if (this.y > d0) {
                    this.y += (d0 - this.y) * 0.2D;
                    this.velocityY += (playerentity.getVelocity().y - this.velocityY) * 0.2D;
                    this.setPos(this.x, this.y, this.z);
                }
            }

            if (this.onGround) {
                this.velocityX *= 0.7F;
                this.velocityZ *= 0.7F;
            }

        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new InfernoLaserParticle(clientWorld, d, e, f, this.spriteProvider);
        }
    }
}
