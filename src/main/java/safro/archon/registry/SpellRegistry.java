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
import safro.archon.spell.ScorchSpell;

public class SpellRegistry {
    // Fire
    public static Spell FIREBALL = register("fireball", new FireballSpell(Element.FIRE, 10));
    public static Spell INCOMBUSTIBLE = register("incombustible", new EffectSpell(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200), Element.FIRE, 50));
    public static Spell SCORCH = register("scorch", new ScorchSpell(Element.FIRE, 40));

    public static void init() {
    }

    private static Spell register(String name, Spell spell) {
        tome(name + "_tome", spell);
        return Registry.register(Archon.SPELL, new Identifier(Archon.MODID, name), spell);
    }

    public static TomeItem tome(String name, Spell spell) {
        return ItemRegistry.register(name, new TomeItem(spell, ItemRegistry.simple().maxCount(1)));
    }
}
