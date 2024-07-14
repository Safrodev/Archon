package safro.archon.item;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.spell_power.api.SpellPowerMechanics;
import safro.archon.api.ManaAttributes;
import safro.archon.api.spell.SpellAttributable;
import safro.archon.mixin.ArmorItemAccessor;

public class ManaArmorItem extends ArmorItem {

    public ManaArmorItem(ArmorMaterial material, Type slot, int maxMana, double critChance, double critDmg, Settings settings) {
        super(material, slot, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(this.getAttributeModifiers(slot.getEquipmentSlot()));

        if (maxMana > 0) {
            builder.put(ManaAttributes.MAX_MANA, new EntityAttributeModifier(ManaAttributes.MAX_ITEM_MODIFIER, "Armor Max Mana", maxMana, EntityAttributeModifier.Operation.ADDITION));
        }

        if (critChance > 0.0D) {
            builder.put(SpellPowerMechanics.CRITICAL_CHANCE.attribute, new EntityAttributeModifier(SpellAttributable.CRIT_CHANCE_ID, "Spell Crit Chance", critChance, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }

        if (critDmg > 0.0D) {
            builder.put(SpellPowerMechanics.CRITICAL_DAMAGE.attribute, new EntityAttributeModifier(SpellAttributable.CRIT_DAMAGE_ID, "Spell Crit Damage", critDmg, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        ((ArmorItemAccessor)this).setAttributeModifiers(builder.build());
    }
}
