package safro.archon.item.earth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.item.ManaWeapon;
import safro.archon.registry.EffectRegistry;

import java.util.List;

public class TerraneanAxeItem extends ManaWeapon {

    public TerraneanAxeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public int getManaCost() {
        return 40;
    }

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 2, true, false));
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STURDY, 400, 0, true, false));
        player.swingHand(hand);
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("text.archon.terranean1").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("text.archon.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
