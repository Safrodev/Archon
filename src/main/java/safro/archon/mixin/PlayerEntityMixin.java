package safro.archon.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.archon.api.ManaAttributes;
import safro.archon.registry.ItemRegistry;
import safro.archon.util.ArchonUtil;

import java.util.Set;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Inject(method = "createPlayerAttributes", require = 1, allow = 1, at = @At("RETURN"))
    private static void addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(ManaAttributes.MAX_MANA);
    }

    private final DefaultedList<ItemStack> cache = DefaultedList.ofSize(4, ItemStack.EMPTY);

    @Inject(method = "tick", at = @At("HEAD"))
    private void onRemoveMask(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        int i = 0;
        for (ItemStack stack : getArmorItems()) {
            ItemStack cacheStack = cache.get(i);
            if (cacheStack.getItem() != stack.getItem()) {
                if (cacheStack.isOf(ItemRegistry.MASK_OF_POWER)) {
                    ArchonUtil.get(player).setDefaultRegenSpeed();
                }
                cache.set(i, stack.copy());
            }
            ++i;
        }

        if (player.getEquippedStack(EquipmentSlot.HEAD).isOf(ItemRegistry.MASK_OF_POWER)) {
            ArchonUtil.get(player).setRegenSpeed(5);
        }
    }


    @Unique
    private int druidBootsTimer = 60;

    @Inject(method = "tick", at = @At("TAIL"))
    private void druidBootsTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getEquippedStack(EquipmentSlot.FEET).isOf(ItemRegistry.DRUID_BOOTS)) {
            if (druidBootsTimer < 1 && player.isSneaking() && ArchonUtil.canRemoveMana(player, 20)) {
                for (BlockPos pos : ArchonUtil.getSphere(player.getBlockPos(), 5)) {
                    ArchonUtil.growBlock(player.world, pos);
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
}
