package safro.archon.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.registry.ItemRegistry;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends ThrownItemEntity {

    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void spawnLightningFromBottle(HitResult result, CallbackInfo ci) {
        PotionEntity entity = (PotionEntity) (Object) this;
        if (!entity.getWorld().isClient) {
            if (entity.getStack().isOf(ItemRegistry.LIGHTNING_BOTTLE)) {
                super.onCollision(result);
                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(entity.getWorld());
                lightningEntity.refreshPositionAfterTeleport(entity.getPos());
                entity.getWorld().spawnEntity(lightningEntity);

                entity.getWorld().syncWorldEvent(2007, entity.getBlockPos(), 16777215);
                this.discard();
                ci.cancel();
            }
        }
    }
}
