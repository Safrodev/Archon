package safro.archon.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.summon.Summon;

import java.util.List;

public class SoulTomeItem extends Item {
    private final Summon summon;

    public SoulTomeItem(Summon summon, Settings settings) {
        super(settings);
        this.summon = summon;
    }

    public Summon getSummon() {
        return this.summon;
    }

    @Override
    public String getTranslationKey() {
        return "item.archon.soul_tome";
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(this.summon.getTranslationKey()).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("text.archon.soul_tome").formatted(Formatting.WHITE));
    }
}
