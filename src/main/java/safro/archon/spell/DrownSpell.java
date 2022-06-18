package safro.archon.spell;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;

public class DrownSpell extends RaycastSpell {

    public DrownSpell(Element type, int manaCost) {
        super(type, manaCost, 8);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, ItemStack stack, LivingEntity target) {
        world.setBlockState(new BlockPos(target.getX(), target.getEyeY(), target.getZ()), Blocks.WATER.getDefaultState());
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.AMBIENT_UNDERWATER_ENTER;
    }
}
