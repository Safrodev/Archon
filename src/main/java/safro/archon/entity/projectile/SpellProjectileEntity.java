package safro.archon.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import safro.archon.registry.EntityRegistry;

public class SpellProjectileEntity extends AbstractFireballEntity {
    private final HitExecutor executor;
    private DefaultParticleType particle;

    public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.executor = HitExecutor.EMPTY;
    }

    public SpellProjectileEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ, HitExecutor executor, ItemStack item) {
        super(EntityRegistry.SPELL_PROJECTILE, owner, velocityX, velocityY, velocityZ, world);
        this.executor = executor;
        this.setItem(item);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.world.isClient) {
            if (entityHitResult.getEntity() instanceof LivingEntity target && this.getOwner() instanceof LivingEntity owner) {
                this.executor.onHit(target, owner, this);
            }
        }
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    public void setParticle(DefaultParticleType particle) {
        this.particle = particle;
    }

    @Override
    protected ParticleEffect getParticleType() {
        return this.particle == null ? super.getParticleType() : this.particle;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient && hitResult.getType() == HitResult.Type.ENTITY) {
            this.discard();
        }
    }
}
