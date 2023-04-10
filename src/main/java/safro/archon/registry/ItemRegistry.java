package safro.archon.registry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.registry.Registry;
import safro.archon.Archon;
import safro.archon.api.Element;
import safro.archon.api.SoulType;
import safro.archon.item.*;
import safro.archon.item.earth.FistOfFuryItem;
import safro.archon.item.earth.RockyHarvesterItem;
import safro.archon.item.earth.TerrainMaceItem;
import safro.archon.item.earth.TerraneanAxeItem;
import safro.archon.item.end.EnderBladeItem;
import safro.archon.item.end.VoidScepterItem;
import safro.archon.item.end.WarpingHarvesterItem;
import safro.archon.item.fire.FlamingHarvesterItem;
import safro.archon.item.fire.HeatRangerItem;
import safro.archon.item.fire.InfernalCoatItem;
import safro.archon.item.fire.WitherStaveItem;
import safro.archon.item.UndeadStaffItem;
import safro.archon.item.sky.BreezyHarvesterItem;
import safro.archon.item.sky.HeavenDialItem;
import safro.archon.item.sky.ThunderBoltItem;
import safro.archon.item.sky.VacuumCleaverItem;
import safro.archon.item.water.FrostSwordItem;
import safro.archon.item.water.SeaMasterCharmItem;
import safro.archon.item.water.SoakingHarvesterItem;
import safro.archon.item.water.WaterStaffItem;

public class ItemRegistry {
    // Core
    public static final Item GRIMOIRE = register("grimoire", new GrimoireItem(simple().maxCount(1)));
    public static final Item CHANNELER = register("channeler", new ChannelerItem(simple().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item FIRE_WAND = register("fire_wand", wand(Element.FIRE));
    public static final Item WATER_WAND = register("water_wand", wand(Element.WATER));
    public static final Item SKY_WAND = register("sky_wand", wand(Element.SKY));
    public static final Item EARTH_WAND = register("earth_wand", wand(Element.EARTH));
    public static final Item END_WAND = register("end_wand", wand(Element.END));

    // Weapons // Gear
    public static final Item ENDER_BLADE = register("ender_blade", new EnderBladeItem(ToolMaterials.DIAMOND, 3, -2.4F, simple()));
    public static final Item VOID_SCEPTER = register("void_scepter", new VoidScepterItem(ToolMaterials.DIAMOND, 1, -2.4F, simple()));
    public static final Item TERRAIN_MACE = register("terrain_mace", new TerrainMaceItem(ToolMaterials.IRON, 4, -3.1F, simple()));
    public static final Item WITHER_STAVE = register("wither_stave", new WitherStaveItem(ToolMaterials.DIAMOND, 1, -2.4F, simple()));
    public static final Item THUNDER_BOLT = register("thunder_bolt", new ThunderBoltItem(ToolMaterials.IRON, 1, -1.1F, simple()));
    public static final Item WATER_STAFF = register("water_staff", new WaterStaffItem(ToolMaterials.DIAMOND, 3, -2.4F, simple()));
    public static final Item HEAT_RANGER = register("heat_ranger", new HeatRangerItem(simple().maxDamage(384)));
    public static final Item VACUUM_CLEAVER = register("vacuum_cleaver", new VacuumCleaverItem(ToolMaterials.IRON, 7, -3.0F, simple()));
    public static final Item FIST_OF_FURY = register("fist_of_fury", new FistOfFuryItem(ToolMaterials.IRON, 3, -1.8F, simple()));
    public static final Item FROST_SWORD = register("frost_sword", new FrostSwordItem(ToolMaterials.DIAMOND, 3, -2.4F, simple()));

    public static final Item TERRANEAN_AXE = register("terranean_axe", new TerraneanAxeItem(ToolMaterials.IRON, 6, -3.1F, simple().rarity(Rarity.RARE)));
    public static final Item HEAVEN_DIAL = register("heaven_dial", new HeavenDialItem(simple().maxCount(1).rarity(Rarity.RARE)));
    public static final Item SEA_MASTER_CHARM = register("sea_master_charm", new SeaMasterCharmItem(simple().maxCount(1).rarity(Rarity.RARE)));
    public static final Item INFERNAL_COAT = register("infernal_coat", new InfernalCoatItem(MaterialRegistry.INFERNAL_COAT, EquipmentSlot.CHEST, simple().fireproof().rarity(Rarity.RARE)));

    // Necromancy
    public static final Item SOUL_CRUSHER = register("soul_crusher", new SwordItem(ToolMaterials.NETHERITE, 3, -2.4F, simple()));
    public static final Item SOUL_SCYTHE = register("soul_scythe", new SwordItem(ToolMaterials.NETHERITE, 3, -2.6F, simple()));
    public static final Item UNDEAD_STAFF = register("undead_staff", new UndeadStaffItem(simple().maxCount(1).rarity(Rarity.UNCOMMON)));

    // Harvesters
    public static final Item ROCKY_HARVESTER = register("rocky_harvester", new RockyHarvesterItem(ToolMaterials.STONE, 7, -3.2F, simple()));
    public static final Item FLAMING_HARVESTER = register("flaming_harvester", new FlamingHarvesterItem(ToolMaterials.STONE, 7, -3.2F, simple()));
    public static final Item WARPING_HARVESTER = register("warping_harvester", new WarpingHarvesterItem(ToolMaterials.STONE, 7, -3.2F, simple()));
    public static final Item BREEZY_HARVESTER = register("breezy_harvester", new BreezyHarvesterItem(ToolMaterials.STONE, 7, -3.2F, simple()));
    public static final Item SOAKING_HARVESTER = register("soaking_harvester", new SoakingHarvesterItem(ToolMaterials.STONE, 7, -3.2F, simple()));

    // Armor
    public static final Item MASK_OF_POWER = register("mask_of_power", new ArmorItem(MaterialRegistry.MASK_OF_POWER, EquipmentSlot.HEAD, simple()));
    public static final Item DRUID_BOOTS = register("druid_boots", new ArmorItem(MaterialRegistry.DRUID_BOOTS, EquipmentSlot.FEET, simple()));

    // Scrolls + Souls
    public static final Item CAPACITY_SCROLL = register("capacity_scroll", new ScrollItem("capacity", simple().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item ACCELERATE_SCROLL = register("accelerate_scroll", new ScrollItem("accelerate", simple().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item REMOVAL_SCROLL = register("removal_scroll", new RemovalScrollItem(simple().maxCount(1).rarity(Rarity.UNCOMMON)));

    public static final Item PLAYER_SOUL = register("player_soul", new SoulItem(SoulType.PLAYER, soul()));
    public static final Item CREATURE_SOUL = register("creature_soul", new SoulItem(SoulType.CREATURE, soul()));
    public static final Item BOSS_SOUL = register("boss_soul", new SoulItem(SoulType.BOSS, soul()));
    public static final Item SOUL_CORE_CREATURE = register("soul_core_creature", new SoulItem(SoulType.CREATURE, simple()));
    public static final Item SOUL_CORE_PLAYER = register("soul_core_player", new SoulItem(SoulType.PLAYER, simple()));
    public static final Item SOUL_CORE_BOSS = register("soul_core_boss", new SoulItem(SoulType.BOSS, simple()));

    // Resources + Misc
    public static final Item EXPERIENCE_POUCH = register("experience_pouch", new ExperiencePouchItem(200, simple().maxCount(1)));
    public static final Item SUPER_EXPERIENCE_POUCH = register("super_experience_pouch", new ExperiencePouchItem(400, simple().maxCount(1)));
    public static final Item GLISTEEL_INGOT = register("glisteel_ingot", new Item(simple()));
    public static final Item FIRE_GEM = register("fire_gem", new Item(simple()));
    public static final Item WATER_GEM = register("water_gem", new Item(simple()));
    public static final Item EARTH_GEM = register("earth_gem", new Item(simple()));
    public static final Item SKY_GEM = register("sky_gem", new Item(simple()));
    public static final Item END_GEM = register("end_gem", new Item(simple()));
    public static final Item FIRE_ESSENCE = register("fire_essence", new Item(simple()));
    public static final Item WATER_ESSENCE = register("water_essence", new Item(simple()));
    public static final Item EARTH_ESSENCE = register("earth_essence", new Item(simple()));
    public static final Item SKY_ESSENCE = register("sky_essence", new Item(simple()));
    public static final Item END_ESSENCE = register("end_essence", new Item(simple()));
    public static final Item COMBUSTION_CHARGE = register("combustion_charge", new CombustionChargeItem(simple()));
    public static final Item MANA_BERRIES = register("mana_berries", new ManaBerriesItem(BlockRegistry.MANA_BERRY_BUSH, simple().food(FoodComponents.GLOW_BERRIES)));
    public static final Item LIGHTNING_BOTTLE = register("lightning_bottle", new LightningBottleItem(simple().maxCount(1)));
    public static final Item PIXIE_LEAVES = register("pixie_leaves", new Item(simple()));
    public static final Item SPRY_DUST = register("spry_dust", new Item(simple()));
    public static final Item TERRANITE_STONE = register("terranite_stone", new Item(simple()));
    public static final Item ANGELIC_STAR = register("angelic_star", new Item(simple()));
    public static final Item WAVE_CRYSTAL = register("wave_crystal", new Item(simple()));
    public static final Item CHARRED_EYE = register("charred_eye", new Item(simple()));

    public static final Item MANA_LEECH_SPAWN_EGG = register("mana_leech_spawn_egg", new SpawnEggItem(EntityRegistry.MANA_LEECH, 0x043C99, 0x1D75B1, simple()));

    protected static Item.Settings simple() {
        return new Item.Settings();
    }

    private static Item.Settings soul() {
        return new Item.Settings().maxCount(1).fireproof();
    }

    private static WandItem wand(Element element) {
        return new WandItem(element, simple().maxCount(1));
    }

    protected static <T extends Item> T register(String name, T item) {
        T registered = Registry.register(Registries.ITEM, new Identifier(Archon.MODID, name), item);
        Archon.ITEMS.add(new ItemStack(registered));
        return registered;
    }

    public static void init() {
    }
}
