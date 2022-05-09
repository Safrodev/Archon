package safro.archon.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.util.TridentAccess;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity implements TridentAccess {
    private boolean quickDespawn = false;

    public TridentEntityMixin(EntityType<? extends TridentEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void quickDespawn(CallbackInfo ci) {
        if (this.quickDespawn && this.inGround) {
            this.discard();
        }
    }

    @Override
    public void setQuickDespawn() {
        this.quickDespawn = true;
    }
}
