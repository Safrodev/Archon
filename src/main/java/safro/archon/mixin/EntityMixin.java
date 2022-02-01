package safro.archon.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.registry.ItemRegistry;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "onStruckByLightning", at = @At("HEAD"), cancellable = true)
    private void lightningImmune(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        Entity e = (Entity) (Object) this;
        if (e instanceof LivingEntity entity) {
            if (entity.getMainHandStack().isOf(ItemRegistry.THUNDER_BOLT)) {
                ci.cancel();
            }
        }
    }
}
