package safro.archon.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;
import safro.archon.effect.AquaShieldEffect;
import safro.archon.effect.ObstructedEffect;
import safro.archon.effect.RageEffect;
import safro.archon.effect.SturdyEffect;

import java.util.LinkedHashMap;
import java.util.Map;

public class EffectRegistry {
    private static final Map<StatusEffect, Identifier> STATUS_EFFECTS = new LinkedHashMap<>();

    public static final StatusEffect RAGE = register("rage", new RageEffect());
    public static final StatusEffect OBSTRUCTED = register("obstructed", new ObstructedEffect());
    public static final StatusEffect STURDY = register("sturdy", new SturdyEffect());
    public static final StatusEffect AQUA_SHIELD = register("aqua_shield", new AquaShieldEffect());

    private static <T extends StatusEffect> T register(String name, T effect) {
        STATUS_EFFECTS.put(effect, new Identifier(Archon.MODID, name));
        return effect;
    }

    public static void init() {
        STATUS_EFFECTS.keySet().forEach(effect -> Registry.register(Registry.STATUS_EFFECT, STATUS_EFFECTS.get(effect), effect));
    }
}
