package safro.archon.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.archon.recipe.ChannelingInventory;
import safro.archon.recipe.ChannelingRecipe;
import safro.archon.registry.MiscRegistry;

import java.util.List;

public class ChannelerItem extends Item {
    
    public ChannelerItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (!world.isClient) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock().asItem() != null) {
                ItemStack blockStack = new ItemStack(state.getBlock().asItem());
                tryRecipe(context.getPlayer(), world, blockStack, pos);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private void tryRecipe(PlayerEntity player, World world, ItemStack block, BlockPos pos) {
        ChannelingInventory inv = new ChannelingInventory(player, world, pos);
        inv.setBlockStack(block);
        List<ChannelingRecipe> match = world.getRecipeManager().getAllMatches(MiscRegistry.CHANNELING, inv, world);
        for (ChannelingRecipe recipe : match) {
            if (recipe.matches(inv, world)) {
                recipe.craft(inv);
                world.removeBlock(pos, false);
            }
        }
    }
}
