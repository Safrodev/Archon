package safro.archon.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow public abstract Iterable<ItemStack> getArmorItems();

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
}
