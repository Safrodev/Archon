package safro.archon.item;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import safro.archon.api.ManaComponent;
import safro.archon.util.ArchonUtil;

public class ManaBerriesItem extends AliasedBlockItem {

    public ManaBerriesItem(Block block, Settings settings) {
        super(block, settings);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity p) {
            ArchonUtil.get(p).addMana(10);
        }
        return super.finishUsing(stack, world, user);
    }
}
