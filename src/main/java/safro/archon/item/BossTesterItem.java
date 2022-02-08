package safro.archon.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import safro.archon.entity.boss.TarEntity;
import safro.archon.registry.EntityRegistry;

public class BossTesterItem extends Item {
    public BossTesterItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            TarEntity tar = EntityRegistry.TAR.create(world);
            tar.refreshPositionAndAngles(player.getBlockPos(), 0.0F, 0.0F);
            tar.onSummoned();
            world.spawnEntity(tar);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }
}
