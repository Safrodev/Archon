package safro.archon.registry;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;
import safro.archon.effect.CustomStatusEffect;
import safro.archon.effect.ManaBoostStatusEffect;

import java.util.LinkedHashMap;
import java.util.Map;

public class EffectRegistry {
    private static final Map<StatusEffect, Identifier> STATUS_EFFECTS = new LinkedHashMap<>();

    public static final StatusEffect RAGE = register("rage", new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xE81E1E).addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 2D, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final StatusEffect OBSTRUCTED = register("obstructed", new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0x20156E));
    public static final StatusEffect STURDY = register("sturdy", new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xA06A41));
    public static final StatusEffect AQUA_SHIELD = register("aqua_shield", new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x1CDFEC));
    public static final StatusEffect MANA_BOOST = register("mana_boost", new ManaBoostStatusEffect(StatusEffectCategory.BENEFICIAL, 0x5978D3));

    private static <T extends StatusEffect> T register(String name, T effect) {
        STATUS_EFFECTS.put(effect, new Identifier(Archon.MODID, name));
        return effect;
    }

    public static void init() {
        STATUS_EFFECTS.keySet().forEach(effect -> Registry.register(Registry.STATUS_EFFECT, STATUS_EFFECTS.get(effect), effect));
    }
}
