package safro.archon.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.api.summon.SummonedMob;

@Mixin(LivingEntity.class)
public abstract class SummonLivingEntityMixin extends Entity implements SummonedMob {
    @Unique
    private static final TrackedData<Integer> LIFETIME = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public SummonLivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initLifetime(CallbackInfo ci) {
        this.dataTracker.startTracking(LIFETIME, Integer.MAX_VALUE);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickLife(CallbackInfo ci) {
        if (this.dataTracker.get(LIFETIME) <= 0) {
            this.discard();
        } else {
            this.dataTracker.set(LIFETIME, this.dataTracker.get(LIFETIME) - 1);
        }
    }

    @Override
    public void archon$setLifetime(int seconds) {
        this.dataTracker.set(LIFETIME, seconds * 20);
    }
}
