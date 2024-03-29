package safro.archon.entity.projectile.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.registry.EntityRegistry;

public class HellbeamEntity extends SpellProjectileEntity {

    public HellbeamEntity(EntityType<? extends SpellProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public HellbeamEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ, ItemStack item, HitExecutor hitExecutor) {
        super(EntityRegistry.HELLBEAM, world, owner, velocityX, velocityY, velocityZ, hitExecutor, item);
    }

    public void tick() {
        super.tick();
        Entity entity = this.getOwner();
        if (this.getWorld().isClient || (entity == null || !entity.isRemoved()) && this.getWorld().isChunkLoaded(this.getBlockPos())) {
            Vec3d vec3d = this.getVelocity();
            double d = this.getX();
            double e = this.getY();
            double f = this.getZ();
            for (int i = 0; i < 5; i++) {
                this.getWorld().addParticle(ParticleTypes.SMALL_FLAME, d, e + 0.5D, f, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.FLAME;
    }
}
