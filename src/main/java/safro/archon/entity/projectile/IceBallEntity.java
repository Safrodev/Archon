package safro.archon.entity.projectile;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import safro.archon.registry.EntityRegistry;

import java.util.Iterator;
import java.util.List;

public class IceBallEntity extends ThrownItemEntity {

    public IceBallEntity(EntityType<? extends IceBallEntity> entityType, World world) {
        super(entityType, world);
    }

    public IceBallEntity(World world, LivingEntity owner) {
        super(EntityRegistry.ICE_BALL, owner, world);
    }

    protected Item getDefaultItem() {
        return Items.ICE;
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        for (int i = 0; i < 32; ++i) {
            this.getWorld().addParticle(ParticleTypes.SNOWFLAKE, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (hitResult.getType() != HitResult.Type.ENTITY || !this.isOwner(((EntityHitResult)hitResult).getEntity())) {
            if (!getWorld().isClient) {
                List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D));
                AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.getWorld(), this.getX(), this.getY(), this.getZ());
                Entity entity = this.getOwner();
                if (entity instanceof LivingEntity) {
                    areaEffectCloudEntity.setOwner((LivingEntity)entity);
                }

                areaEffectCloudEntity.setRadius(3.0F);
                areaEffectCloudEntity.setDuration(600);
                areaEffectCloudEntity.setRadiusGrowth((7.0F - areaEffectCloudEntity.getRadius()) / (float)areaEffectCloudEntity.getDuration());
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 2));
                if (!list.isEmpty()) {
                    Iterator var5 = list.iterator();

                    while(var5.hasNext()) {
                        LivingEntity livingEntity = (LivingEntity)var5.next();
                        double d = this.squaredDistanceTo(livingEntity);
                        if (d < 16.0D) {
                            areaEffectCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                            break;
                        }
                    }
                }
                this.getWorld().spawnEntity(areaEffectCloudEntity);
                this.discard();
            }
        }
    }

    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof PlayerEntity && !entity.isAlive()) {
            this.discard();
        } else {
            super.tick();
        }
    }
}
