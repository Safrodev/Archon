package safro.archon.entity.projectile.spell;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import safro.archon.registry.EntityRegistry;

public class CloudshotEntity extends SpellProjectileEntity {

    public CloudshotEntity(EntityType<? extends SpellProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CloudshotEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ, ItemStack item, HitExecutor hitExecutor) {
        super(EntityRegistry.CLOUDSHOT, world, owner, velocityX, velocityY, velocityZ, hitExecutor, item);
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.CLOUD;
    }
}
