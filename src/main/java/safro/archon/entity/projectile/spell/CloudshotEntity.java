package safro.archon.entity.projectile.spell;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import safro.archon.api.spell.HitExecutor;
import safro.archon.registry.EntityRegistry;

public class CloudshotEntity extends SpellProjectileEntity {

    public CloudshotEntity(EntityType<? extends SpellProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public CloudshotEntity(World world, PlayerEntity owner, HitExecutor hitExecutor) {
        super(EntityRegistry.CLOUDSHOT, world, owner, hitExecutor);
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.CLOUD;
    }
}
