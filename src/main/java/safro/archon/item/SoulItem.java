package safro.archon.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.SoulType;

import java.util.List;

public class SoulItem extends Item {
    private final SoulType type;

    public SoulItem(SoulType type, Settings settings) {
        super(settings);
        this.type = type;
    }

    public SoulType getType() {
        return type;
    }

    public String getTypeString() {
        if (getType() == SoulType.PLAYER) {
            return "Player";
        } else if (getType() == SoulType.CREATURE) {
            return "Creature";
        } else if (getType() == SoulType.BOSS) {
            return "Boss";
        }
        return "None";
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText(getTypeString()).formatted(Formatting.GRAY));
    }
}
