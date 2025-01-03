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
import safro.archon.api.spell.Spell;
import safro.archon.item.SpellTomeItem;
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
    public static final Spell FIREBALL = register("fireball", new FireballSpell(Element.FIRE, 20));
    public static final Spell INCOMBUSTIBLE = register("incombustible", new EffectSpell(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 1), Element.FIRE, 50));
    public static final Spell SCORCH = register("scorch", new ScorchSpell(Element.FIRE, 30));
    public static final Spell HELLBEAM = register("hellbeam", new HellbeamSpell(Element.FIRE, 10));

    // Water
    public static final Spell AQUA_SHIELD = register("aqua_shield", new EffectSpell(new StatusEffectInstance(EffectRegistry.AQUA_SHIELD, 200, 0, false, false, true), Element.WATER, 100));
    public static final Spell FREEZE = register("freeze", new FreezeSpell(Element.WATER, 10));
    public static final Spell MEND = register("mend", new MendSpell(Element.WATER, 40));
    public static final Spell OVERCAST = register("overcast", new WeatherSpell(Element.WATER, 90, true));
    public static final Spell BUBBLE_BEAM = register("bubble_beam", new BubbleBeamSpell(Element.WATER, 10));

    // Sky
    public static final Spell PROPEL = register("propel", new PropelSpell(Element.SKY, 10));
    public static final Spell GUST = register("gust", new GustSpell(Element.SKY, 20));
    public static final Spell THUNDER_STRIKE = register("thunder_strike", new ThunderStrikeSpell(Element.SKY, 30));
    public static final Spell CLOUDSHOT = register("cloudshot", new CloudshotSpell(Element.SKY, 10));
    public static final Spell CLEARING_BREEZE = register("clearing_breeze", new WeatherSpell(Element.SKY, 90, false));
    public static final Spell VACUUM = register("vacuum", new VacuumSpell(Element.SKY, 50));

    // Earth
    public static final Spell RUMBLE = register("rumble", new RumbleSpell(Element.EARTH, 40));
    public static final Spell CRUSH = register("crush", new CrushSpell(Element.EARTH, 2));
    public static final Spell SPIKE = register("spike", new SpikeSpell(Element.EARTH, 20));
    public static final Spell TERRAIN_TOSS = register("terrain_toss", new TerrainTossSpell(Element.EARTH, 10));
    public static final Spell RAGE = register("rage", new EffectSpell(new StatusEffectInstance(EffectRegistry.RAGE, 140, 0, false, false, true), Element.EARTH, 40));

    // End
    public static final Spell DARKBALL = register("darkball", new DarkballSpell(Element.END, 10));
    public static final Spell SWAP = register("swap", new SwapSpell(Element.END, 40));
    public static final Spell ENDER = register("ender", new EnderSpell(Element.END, 20));
    public static final Spell SHADOW = register("shadow", new EffectSpell(new StatusEffectInstance(EffectRegistry.SHADOW, 200, 0, false, false, true), Element.END, 70));
    public static final Spell ASTROFALL = register("astrofall", new AstrofallSpell(Element.END, 80));
    public static final Spell WARP = register("warp", new WarpSpell(Element.END, 10));

    public static void init() {
    }

    private static Spell register(String name, Spell spell) {
        createTome(name + "_tome", spell);
        addToLists(spell);
        return Registry.register(REGISTRY, new Identifier(Archon.MODID, name), spell);
    }

    public static SpellTomeItem createTome(String name, Spell spell) {
        SpellTomeItem item = Registry.register(Registries.ITEM, new Identifier(Archon.MODID, name), new SpellTomeItem(spell, new FabricItemSettings().maxCount(1)));
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
