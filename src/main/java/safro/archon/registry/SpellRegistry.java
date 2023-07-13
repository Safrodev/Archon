package safro.archon.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import safro.archon.Archon;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.item.TomeItem;
import safro.archon.spell.*;
import safro.saflib.SafLib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellRegistry {
    public static final Registry<Spell> REGISTRY = FabricRegistryBuilder.createSimple(Spell.class, new Identifier(Archon.MODID, "spell")).buildAndRegister();
    public static final RegistryKey<Registry<Spell>> REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier(Archon.MODID, "spell"));
    public static final Map<Element, List<Spell>> SPELLS = new HashMap<>();

    // Fire
    public static Spell FIREBALL = register("fireball", new FireballSpell(Element.FIRE, 20));
    public static Spell INCOMBUSTIBLE = register("incombustible", new EffectSpell(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200), Element.FIRE, 50));
    public static Spell SCORCH = register("scorch", new ScorchSpell(Element.FIRE, 30));
    public static Spell HELLBEAM = register("hellbeam", new HellbeamSpell(Element.FIRE, 10));

    // Water
    public static Spell AQUA_SHIELD = register("aqua_shield", new EffectSpell(new StatusEffectInstance(EffectRegistry.AQUA_SHIELD, 300, 0, false, false, true), Element.WATER, 50));
    public static Spell FREEZE = register("freeze", new FreezeSpell(Element.WATER, 10));
    public static Spell DROWN = register("drown", new DrownSpell(Element.WATER, 30));
    public static Spell MEND = register("mend", new MendSpell(Element.WATER, 40));

    // Sky
    public static Spell PROPEL = register("propel", new PropelSpell(Element.SKY, 10));
    public static Spell GUST = register("gust", new GustSpell(Element.SKY, 20));
    public static Spell THUNDER_STRIKE = register("thunder_strike", new ThunderStrikeSpell(Element.SKY, 30));
    public static Spell CLOUDSHOT = register("cloudshot", new CloudshotSpell(Element.SKY, 30));

    // Earth
    public static Spell RUMBLE = register("rumble", new RumbleSpell(Element.EARTH, 40));
    public static Spell CRUSH = register("crush", new CrushSpell(Element.EARTH, 2));
    public static Spell SPIKE = register("spike", new SpikeSpell(Element.EARTH, 20));
    public static Spell TERRAIN_TOSS = register("terrain_toss", new TerrainTossSpell(Element.EARTH, 15));

    // End
    public static Spell DARKBALL = register("darkball", new DarkballSpell(Element.END, 10));
    public static Spell SWAP = register("swap", new SwapSpell(Element.END, 40));
    public static Spell ENDER = register("ender", new EnderSpell(Element.END, 20));
    public static Spell SHADOW = register("shadow", new EffectSpell(new StatusEffectInstance(EffectRegistry.SHADOW, 200, 0, false, false, true), Element.END, 50));

    public static void init() {
    }

    private static Spell register(String name, Spell spell) {
        createTome(name + "_tome", spell);
        addToLists(spell);
        return Registry.register(REGISTRY, new Identifier(Archon.MODID, name), spell);
    }

    public static TomeItem createTome(String name, Spell spell) {
        TomeItem item = Registry.register(Registries.ITEM, new Identifier(Archon.MODID, name), new TomeItem(spell, new FabricItemSettings().maxCount(1)));
        SafLib.ITEMS.add(new ItemStack(item));
        return item;
    }

    public static Item getTome(Spell spell) {
        String s = REGISTRY.getId(spell).toString();
        Identifier tome = new Identifier(s + "_tome");
        return Registries.ITEM.get(tome);
    }

    private static void addToLists(Spell spell) {
        if (SPELLS.isEmpty()) {
            for (Element e : Element.values()) {
                SPELLS.put(e, new ArrayList<>());
            }
        }
        SPELLS.get(spell.getElement()).add(spell);
    }
}
