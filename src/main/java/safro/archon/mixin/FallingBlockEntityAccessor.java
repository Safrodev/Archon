package safro.archon.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FallingBlockEntity.class)
public interface FallingBlockEntityAccessor {
    @Invoker("<init>")
    static FallingBlockEntity create(World world, double x, double y, double z, BlockState block) {
        throw new AssertionError();
    }
}
