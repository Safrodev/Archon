package safro.archon.api.spell;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.spell_power.api.SpellPowerMechanics;
import safro.archon.api.Element;

import java.util.UUID;

public interface SpellAttributable {
    UUID CRIT_CHANCE_ID = UUID.fromString("b2866e6c-1a88-4d4a-8e7a-41954921e2b6");
    UUID CRIT_DAMAGE_ID = UUID.fromString("ee7ef13e-0ff2-4826-aedc-6aa6bc3af4f1");

    Element getElement();

    default void addPower(ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder, Element element, double amount) {
        builder.put(element.getSchool().attribute, new EntityAttributeModifier(element.getAttributeUUID(), "Spell Power", amount, EntityAttributeModifier.Operation.ADDITION));
    }

    default void addCritChance(ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder, double amount) {
        builder.put(SpellPowerMechanics.CRITICAL_CHANCE.attribute, new EntityAttributeModifier(CRIT_CHANCE_ID, "Spell Crit Chance", amount, EntityAttributeModifier.Operation.MULTIPLY_BASE));
    }

    default void addCritDamage(ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder, double amount) {
        builder.put(SpellPowerMechanics.CRITICAL_DAMAGE.attribute, new EntityAttributeModifier(CRIT_DAMAGE_ID, "Spell Crit Damage", amount, EntityAttributeModifier.Operation.MULTIPLY_BASE));
    }
}
