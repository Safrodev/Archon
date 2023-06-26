package safro.archon.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.archon.registry.SoundRegistry;
import safro.archon.util.ArchonUtil;

public class CombustionChargeItem extends Item {

    public CombustionChargeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        for (BlockPos pos : ArchonUtil.getPosArea(context.getWorld(), context.getBlockPos(), context.getPlayer(), 3, true)) {
            if (canBreak(player, world, context.getBlockPos(), pos)) {
                BlockState state = world.getBlockState(pos);
                state.getBlock().afterBreak(world, player, pos, state, world.getBlockEntity(pos), context.getStack());
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                world.removeBlockEntity(pos);
            }
        }
        world.playSound(null, context.getBlockPos(), SoundRegistry.COMBUSTION, SoundCategory.PLAYERS, 1.0F, 1.0F);
        context.getStack().decrement(1);
        return ActionResult.CONSUME;
    }

    private boolean canBreak(PlayerEntity player, World world, BlockPos original, BlockPos pos) {
        if (original.equals(pos)) {
            return false;
        }
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
            return false;
        }
        if (!state.getFluidState().isEmpty()) {
            return false;
        }
        float blockHardness = state.calcBlockBreakingDelta(player, world, pos);
        if (blockHardness == -1.0F) {
            return false;
        }
        if (state.isIn(BlockTags.WITHER_IMMUNE)) {
            return false;
        }
        float originalHardness = world.getBlockState(original).getHardness(world, original);
        return !((originalHardness / blockHardness) > 10.0F);
    }
}
