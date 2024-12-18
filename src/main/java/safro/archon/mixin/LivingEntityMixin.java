package safro.archon.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.archon.api.ManaComponent;
import safro.archon.registry.EffectRegistry;
import safro.archon.registry.ItemRegistry;
import safro.archon.util.ArchonUtil;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
    private void seaMasterCharm(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if (((LivingEntity)(Object) this) instanceof PlayerEntity p) {
            if (p.getMainHandStack().isOf(ItemRegistry.SEA_MASTER_CHARM)) {
                cir.setReturnValue(fluidState.isIn(FluidTags.WATER));
            }
        }
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void tickAquaShield(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasStatusEffect(EffectRegistry.AQUA_SHIELD)) {
            if (entity.getRandom().nextFloat() < 0.3F) {
                entity.getWorld().addParticle(ParticleTypes.NAUTILUS, entity.getParticleX(1.0D), entity.getRandomBodyY() + 0.5D, entity.getParticleZ(1.0D), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void archonStatusEffectDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.hasStatusEffect(EffectRegistry.STURDY)) {
            if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
                if (!(source.getSource() instanceof PotionEntity)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void soulCrusherMana(DamageSource source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (source.getAttacker() instanceof PlayerEntity player) {
            ManaComponent mana = ArchonUtil.get(player);
            if (player.getMainHandStack().isOf(ItemRegistry.SOUL_CRUSHER) && mana.getMana() < mana.getMaxMana()) {
                mana.addMana(40);
            } else if (player.getMainHandStack().isOf(ItemRegistry.SOUL_SCYTHE)) {
                ArchonUtil.dropItem(entity.getWorld(), entity.getBlockPos(), ItemRegistry.SOUL);
            }
        }
    }
}
