package safro.archon.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.registry.ItemRegistry;

@Mixin(PotionEntity.class)
public class PotionEntityMixin {

    @Inject(method = "onBlockHit", at = @At("TAIL"))
    private void spawnLightningFromBottle(BlockHitResult blockHitResult, CallbackInfo ci) {
        BlockPos pos = blockHitResult.getBlockPos();
        PotionEntity entity = (PotionEntity) (Object) this;
        if (!entity.world.isClient) {
            if (entity.getStack().isOf(ItemRegistry.LIGHTNING_BOTTLE)) {
                LightningEntity lightningEntity = (LightningEntity) EntityType.LIGHTNING_BOLT.create(entity.world);
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
                entity.world.spawnEntity(lightningEntity);
            }
        }
    }
}
