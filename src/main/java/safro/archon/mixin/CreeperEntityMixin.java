package safro.archon.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.archon.registry.ItemRegistry;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {

    @Shadow @Final private static TrackedData<Boolean> CHARGED;

    @Inject(method = "interactMob", at = @At("HEAD"))
    private void chargeFromBottle(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        CreeperEntity creeper = (CreeperEntity) (Object) this;
        ItemStack itemStack = player.getStackInHand(hand);
        boolean bl = creeper.getDataTracker().get(CHARGED);
        if (itemStack.isOf(ItemRegistry.LIGHTNING_BOTTLE) && !bl) {
            creeper.getDataTracker().set(CHARGED, true);
            ItemUsage.exchangeStack(itemStack, player, new ItemStack(Items.GLASS_BOTTLE));
        }
    }
}
