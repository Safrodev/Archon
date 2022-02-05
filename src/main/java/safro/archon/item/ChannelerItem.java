package safro.archon.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.archon.Archon;
import safro.archon.recipe.ChannelingRecipe;
import safro.archon.registry.MiscRegistry;
import safro.archon.registry.SoundRegistry;
import safro.archon.util.ArchonUtil;

public class ChannelerItem extends Item {
    
    public ChannelerItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != null) {
            return tryRecipe(context.getPlayer(), world, state.getBlock(), pos);
        }
        return ActionResult.PASS;
    }

    private ActionResult tryRecipe(PlayerEntity player, World world, Block block, BlockPos pos) {
        ChannelingRecipe channeling = world.getRecipeManager().listAllOfType(MiscRegistry.CHANNELING).stream().filter(recipe -> recipe.getBlock() == block).findFirst().orElse(null);
        if (channeling != null && ArchonUtil.canRemoveMana(player, channeling.getManaCost())) {
            if (!world.isClient) {
                ItemEntity result = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), channeling.getOutput());
                result.setToDefaultPickupDelay();
                world.spawnEntity(result);

                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                ArchonUtil.get(player).removeMana(channeling.getManaCost());

                if (player instanceof ServerPlayerEntity) {
                    MiscRegistry.CHANNELED_CRITERION.trigger((ServerPlayerEntity) player, channeling.getOutput().copy());
                }
            }
            if (Archon.CONFIG.play_channel_sound) {
                world.playSound(null, pos, SoundRegistry.CHANNEL_MANA, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
