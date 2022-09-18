package safro.archon.entity.projectile.spell;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import safro.archon.registry.EntityRegistry;

public class SpellProjectileEntity extends AbstractFireballEntity {
    private final HitExecutor hitExecutor;

    public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.hitExecutor = HitExecutor.EMPTY;
    }

    public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ, HitExecutor hitExecutor, ItemStack item) {
        super(entityType, owner, velocityX, velocityY, velocityZ, world);
        this.hitExecutor = hitExecutor;
        this.setItem(item);
    }

    public SpellProjectileEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ, HitExecutor hitExecutor, ItemStack item) {
        this(EntityRegistry.SPELL_PROJECTILE, world, owner, velocityX, velocityY, velocityZ, hitExecutor, item);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.world.isClient) {
            if (entityHitResult.getEntity() instanceof LivingEntity target && this.getOwner() instanceof LivingEntity owner) {
                this.hitExecutor.onHit(target, owner, this);
            }
        }
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient && hitResult.getType() == HitResult.Type.ENTITY) {
            this.discard();
        }
    }
}
