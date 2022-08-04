package safro.archon.mixin;

import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.archon.registry.BlockRegistry;

@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {

    @Inject(method = "canAccessBookshelf", at = @At("HEAD"), cancellable = true)
    private static void canAccessMagicalBookshelf(World world, BlockPos tablePos, BlockPos bookshelfOffset, CallbackInfoReturnable<Boolean> cir) {
        if (world.getBlockState(tablePos.add(bookshelfOffset)).isOf(BlockRegistry.MAGICAL_BOOKSHELF) && world.isAir(tablePos.add(bookshelfOffset.getX() / 2, bookshelfOffset.getY(), bookshelfOffset.getZ() / 2))) {
            cir.setReturnValue(true);
        }
    }
}
