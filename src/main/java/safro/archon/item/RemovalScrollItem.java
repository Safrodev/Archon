package safro.archon.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class RemovalScrollItem extends Item {

    public RemovalScrollItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            if (ArchonUtil.hasScroll(player)) {
                String current = ArchonUtil.getScroll(player).substring(0, 1).toUpperCase() + ArchonUtil.getScroll(player).substring(1);
                player.sendMessage(new TranslatableText("text.archon.removed_scroll", current).formatted(Formatting.GREEN), true);
                this.removeBonus(ArchonUtil.getScroll(player), player);
                ArchonUtil.setScroll(player, "none");
                stack.decrement(1);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    private void removeBonus(String name, PlayerEntity player) {
        if (name.equals("capacity")) {
            ArchonUtil.get(player).setMaxMana(ArchonUtil.get(player).getMaxMana() - 100);
        } else if (name.equals("accelerate")) {
            ArchonUtil.get(player).setRegenSpeed(20);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("text.archon.removal").formatted(Formatting.ITALIC));
    }
}
