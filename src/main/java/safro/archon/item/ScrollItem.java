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

public class ScrollItem extends Item {
    private final String name;

    public ScrollItem(String name, Settings settings) {
        super(settings);
        this.name = name;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            if (!ArchonUtil.hasScroll(player)) {
                ArchonUtil.setScroll(player, name);
                addBonus(name, player);
                stack.decrement(1);
                return TypedActionResult.success(stack);
            } else {
                player.sendMessage(new TranslatableText("text.archon.has_scroll").formatted(Formatting.RED), true);
                return TypedActionResult.pass(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    private void addBonus(String name, PlayerEntity player) {
        if (name.equals("capacity")) {
            ArchonUtil.get(player).setMaxMana(ArchonUtil.get(player).getMaxMana() + 100);
        } else if (name.equals("accelerate")) {
            ArchonUtil.get(player).setRegenSpeed(10);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("text.archon." + name).formatted(Formatting.ITALIC));
    }
}
