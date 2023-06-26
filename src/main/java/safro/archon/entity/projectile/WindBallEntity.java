package safro.archon.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import safro.archon.registry.EntityRegistry;

import java.util.List;

public class WindBallEntity extends ExplosiveProjectileEntity {

    public WindBallEntity(EntityType<? extends WindBallEntity> entityType, World world) {
        super(entityType, world);
    }

    public WindBallEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(EntityRegistry.WIND_BALL, owner, directionX, directionY, directionZ, world);
    }

    public boolean isOnFire() {
        return false;
    }

    public void tick() {
        super.tick();
        if (this.getOwner() instanceof LivingEntity owner) {
            List<LivingEntity> list = getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(5D), EntityPredicates.VALID_LIVING_ENTITY);
            for (LivingEntity e : list) {
                if (e != owner) {
                    this.push(e, owner);
                }
            }
        }
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            if (owner instanceof LivingEntity && entity instanceof LivingEntity) {
                this.push((LivingEntity) entity, (LivingEntity) owner);
            }
        }
    }

    public void push(LivingEntity target, LivingEntity owner) {
        float f = 3F;
        float g = f / 2.0F + (float) this.random.nextInt((int) f);
        boolean bl = target.damage(this.getWorld().getDamageSources().mobProjectile(this, owner), g);
        if (bl) {
            target.setVelocity(target.getVelocity().add(0.0D, 0.4000000059604645D, 0.0D));
            this.applyDamageEffects(owner, target);
        }
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.discard();
        }
    }

    public boolean collides() {
        return false;
    }

    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    protected boolean isBurning() {
        return false;
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.CLOUD;
    }
}
