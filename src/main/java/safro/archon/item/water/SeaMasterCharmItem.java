package safro.archon.item.water;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeaMasterCharmItem extends Item {

    public SeaMasterCharmItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("text.archon.sea_charm1").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("text.archon.sea_charm2").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("text.archon.sea_charm3").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("text.archon.shift"));
        }
    }
}
