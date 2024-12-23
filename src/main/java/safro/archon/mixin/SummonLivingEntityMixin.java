package safro.archon.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.api.summon.SummonedMob;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class SummonLivingEntityMixin extends Entity implements SummonedMob {
    @Unique
    private static final TrackedData<Integer> LIFETIME = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<String> OWNER_ID = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.STRING);

    public SummonLivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initLifetime(CallbackInfo ci) {
        this.dataTracker.startTracking(LIFETIME, -1);
        this.dataTracker.startTracking(OWNER_ID, "");
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickLife(CallbackInfo ci) {
        if (this.dataTracker.get(LIFETIME) > 0) {
            this.dataTracker.set(LIFETIME, this.dataTracker.get(LIFETIME) - 1);

            if (this.dataTracker.get(LIFETIME) <= 0) {
                this.discard();
            } else if (!this.dataTracker.get(OWNER_ID).isEmpty()) {
                PlayerEntity owner = this.getWorld().getPlayerByUuid(UUID.fromString(this.dataTracker.get(OWNER_ID)));
                if (owner != null && owner.isAlive() && ((LivingEntity) (Object) this) instanceof MobEntity mob) {
                    if (mob.getTarget() == null) {
                        mob.setTarget(owner.getAttacking());
                    }
                }
            }
        }
    }

    @Override
    public void archon$setLifetime(int seconds) {
        this.dataTracker.set(LIFETIME, seconds * 20);
    }

    @Override
    public void archon$setOwner(String uiud) {
        this.dataTracker.set(OWNER_ID, uiud);
    }

    @Override
    public boolean archon$isOwner(PlayerEntity player) {
        String owner = this.dataTracker.get(OWNER_ID);
        if (!owner.isEmpty()) {
            return owner.equals(player.getUuidAsString());
        }
        return false;
    }
}
