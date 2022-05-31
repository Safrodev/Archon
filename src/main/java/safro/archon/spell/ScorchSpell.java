package safro.archon.spell;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import safro.archon.api.Element;
import safro.archon.api.Spell;

public class ScorchSpell extends Spell {

    public ScorchSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {

//        for (int i = 0; i < 5; i++) {
//            BlockState state = world.getBlockState(pos);
//            int burn = FlammableBlockRegistry.getDefaultInstance().get(state.getBlock()).getBurnChance();
//            if (state.isAir() || burn >= 50) {
//                world.setBlockState(pos, Blocks.FIRE.getDefaultState());
//            }
//            pos = pos.offset(dir);
//        }

        if (player.getHorizontalFacing() == Direction.NORTH) {
            for (int j = -10; j <= 10; j++) {
                BlockPos pos = player.getBlockPos().add(j, -1, -2);
                if (isValid(world, pos)) world.setBlockState(pos, Blocks.FIRE.getDefaultState());
            }
        }

        if (player.getHorizontalFacing() == Direction.EAST) {
            for (int j = -10; j <= 10; j++) {
                BlockPos pos = player.getBlockPos().add(2, 0, j);
                if (isValid(world, pos)) world.setBlockState(pos, Blocks.FIRE.getDefaultState());
            }
        }

        if (player.getHorizontalFacing() == Direction.SOUTH) {
            for (int j = -10; j <= 10; j++) {
                BlockPos pos = player.getBlockPos().add(j, 0, 2);
                if (isValid(world, pos)) world.setBlockState(pos, Blocks.FIRE.getDefaultState());
            }
        }

        if (player.getHorizontalFacing() == Direction.WEST) {
            for (int j = -10; j <= 10; j++) {
                BlockPos pos = player.getBlockPos().add(-2, 0, j);
                if (isValid(world, pos)) world.setBlockState(pos, Blocks.FIRE.getDefaultState());
            }
        }
    }

    private boolean isValid(World world, BlockPos pos) {
        int burn = FlammableBlockRegistry.getDefaultInstance().get(world.getBlockState(pos).getBlock()).getBurnChance();
        return world.getBlockState(pos).isAir() || burn >= 50;
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ITEM_FLINTANDSTEEL_USE;
    }
}
