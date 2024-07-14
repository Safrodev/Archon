package safro.archon.item.fire;

import com.google.common.collect.ImmutableMultimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.SpellAttributable;
import safro.archon.mixin.ArmorItemAccessor;

import java.util.List;

public class InfernalCoatItem extends ArmorItem implements SpellAttributable {

    public InfernalCoatItem(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(this.getAttributeModifiers(slot.getEquipmentSlot()));

        addCritChance(builder, 0.15D);
        ((ArmorItemAccessor)this).setAttributeModifiers(builder.build());
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

    @Override
    public Element getElement() {
        return Element.FIRE;
    }
}
