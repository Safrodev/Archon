package safro.archon.item.fire;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfernalCoatItem extends ArmorItem {

    public InfernalCoatItem(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("text.archon.infernal_coat1").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("text.archon.infernal_coat2").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("text.archon.infernal_coat3").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("text.archon.shift"));
        }
        tooltip.add(Text.translatable("text.archon.mana_cost_all").formatted(Formatting.BLUE));
    }
}
