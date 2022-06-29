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

public class SpikeSpell extends RaycastSpell {

    public SpikeSpell(Element type, int manaCost) {
        super(type, manaCost, 10);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, ItemStack stack, LivingEntity target) {
        BlockPos pos = target.getBlockPos();
        if (world.getBlockState(pos).isAir()) {
            world.setBlockState(pos, Blocks.POINTED_DRIPSTONE.getDefaultState());
        }
        target.addVelocity(0, 1.5, 0);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_POINTED_DRIPSTONE_LAND;
    }
}
