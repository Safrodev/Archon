package safro.archon.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.archon.api.ManaAttributes;
import safro.archon.registry.EffectRegistry;
import safro.archon.registry.ItemRegistry;
import safro.archon.util.ArchonUtil;
import safro.saflib.util.MathUtil;

import java.util.Set;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method = "createPlayerAttributes", require = 1, allow = 1, at = @At("RETURN"))
    private static void addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(ManaAttributes.MAX_MANA);
    }

    @Unique
    private int druidBootsTimer = 60;

    @Inject(method = "tick", at = @At("TAIL"))
    private void druidBootsTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getEquippedStack(EquipmentSlot.FEET).isOf(ItemRegistry.DRUID_BOOTS)) {
            if (druidBootsTimer < 1 && player.isSneaking() && ArchonUtil.canRemoveMana(player, 20)) {
                for (BlockPos pos : MathUtil.getPosSphere(player.getBlockPos(), 5)) {
                    ArchonUtil.growBlock(player.getWorld(), pos);
                }
                druidBootsTimer = 60;
                ArchonUtil.get(player).removeMana(20);
            } else
                --druidBootsTimer;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void seaMasterCharmEffects(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getInventory().containsAny(Set.of(ItemRegistry.SEA_MASTER_CHARM))) {
            if (player.isTouchingWaterOrRain()) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60, 2, true, false, false));
            }
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 60, 0, true, false, false));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void infernalCoatEffects(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.INFERNAL_COAT)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 60, 0, true, false, false));
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z", shift = At.Shift.AFTER))
    private void shadowSpellNoClip(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.hasStatusEffect(EffectRegistry.SHADOW) && player.isSneaking()) {
            player.noClip = true;
            player.setOnGround(false);
        } else if (player.hasStatusEffect(EffectRegistry.SHADOW)) {
            player.noClip = false;
        }
    }
}
