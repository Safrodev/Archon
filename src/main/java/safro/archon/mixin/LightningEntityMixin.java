package safro.archon.mixin;

import net.minecraft.entity.LightningEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.util.LightningAccess;

@Mixin(LightningEntity.class)
public class LightningEntityMixin implements LightningAccess {
    @Shadow private boolean cosmetic;
    private boolean fire = true;

    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    private void checkForNoFire(int spreadAttempts, CallbackInfo ci) {
        if (!spawnFire()) {
            ci.cancel();
        }
    }

    public boolean spawnFire() {
        return fire;
    }

    @Override
    public void setFireSpawning(boolean bl) {
        this.fire = bl;
    }

    @Override
    public boolean isCosmetic() {
        return cosmetic;
    }
}
