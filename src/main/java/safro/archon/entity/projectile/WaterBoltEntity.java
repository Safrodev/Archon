package safro.archon.entity.projectile;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import safro.archon.registry.EntityRegistry;

public class WaterBoltEntity extends AbstractFireballEntity {
    private boolean splashes = false;

    public WaterBoltEntity(EntityType<? extends WaterBoltEntity> entityType, World world) {
        super(entityType, world);
    }

    public WaterBoltEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ) {
        super(EntityRegistry.WATER_BOLT, owner, velocityX, velocityY, velocityZ, world);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            this.discard();
        }
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.world.isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            entity.damage(DamageSource.magic(this, entity2), 6.0F);
            if (entity2 instanceof LivingEntity living) {
                this.applyDamageEffects(living, entity);
                if (this.shouldSplash() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                    this.world.setBlockState(new BlockPos(entity.getX(), entity.getEyeY(), entity.getZ()), Blocks.WATER.getDefaultState());
                }
            }
        }
    }

    public void setSplash(boolean bl) {
        this.splashes = bl;
    }

    public boolean shouldSplash() {
        return splashes;
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setSplash(nbt.getBoolean("Splash"));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Splash", this.shouldSplash());
    }

    protected boolean isBurning() {
        return false;
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.SPLASH;
    }
}
