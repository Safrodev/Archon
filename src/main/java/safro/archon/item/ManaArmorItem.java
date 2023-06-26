package safro.archon.item;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import safro.archon.api.ManaAttributes;
import safro.archon.mixin.ArmorItemAccessor;

public class ManaArmorItem extends ArmorItem {

    public ManaArmorItem(ArmorMaterial material, Type slot, int max, Settings settings) {
        super(material, slot, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(this.getAttributeModifiers(slot.getEquipmentSlot()));

        if (max > 0) {
            builder.put(ManaAttributes.MAX_MANA, new EntityAttributeModifier(ManaAttributes.MAX_ITEM_MODIFIER, "Armor Max Mana", max, EntityAttributeModifier.Operation.ADDITION));
        }
        ((ArmorItemAccessor)this).setAttributeModifiers(builder.build());
    }
}
