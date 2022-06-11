package safro.archon.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.item.TomeItem;
import safro.archon.spell.EffectSpell;
import safro.archon.spell.FireballSpell;
import safro.archon.spell.FreezeSpell;
import safro.archon.spell.ScorchSpell;

public class SpellRegistry {
    // Fire
    public static Spell FIREBALL = register("fireball", new FireballSpell(Element.FIRE, 20));
    public static Spell INCOMBUSTIBLE = register("incombustible", new EffectSpell(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200), Element.FIRE, 50));
    public static Spell SCORCH = register("scorch", new ScorchSpell(Element.FIRE, 40));

    // Water
    public static Spell AQUA_SHIELD = register("aqua_shield", new EffectSpell(new StatusEffectInstance(EffectRegistry.AQUA_SHIELD, 400, 0, false, false, true), Element.WATER, 30));
    public static Spell FREEZE = register("freeze", new FreezeSpell(Element.WATER, 10));

    public static void init() {
    }

    private static Spell register(String name, Spell spell) {
        createTome(name + "_tome", spell);
        return Registry.register(Archon.SPELL, new Identifier(Archon.MODID, name), spell);
    }

    public static TomeItem createTome(String name, Spell spell) {
        return ItemRegistry.register(name, new TomeItem(spell, ItemRegistry.simple().maxCount(1)));
    }
}
