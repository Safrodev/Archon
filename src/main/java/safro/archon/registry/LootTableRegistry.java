package safro.archon.registry;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;

import java.util.Arrays;
import java.util.List;

public class LootTableRegistry {
    public static final Identifier[] VILLAGE_CHESTS = new Identifier[]{LootTables.VILLAGE_WEAPONSMITH_CHEST, LootTables.VILLAGE_TOOLSMITH_CHEST, LootTables.VILLAGE_ARMORER_CHEST, LootTables.VILLAGE_CARTOGRAPHER_CHEST, LootTables.VILLAGE_MASON_CHEST, LootTables.VILLAGE_SHEPARD_CHEST, LootTables.VILLAGE_BUTCHER_CHEST, LootTables.VILLAGE_FLETCHER_CHEST, LootTables.VILLAGE_FISHER_CHEST, LootTables.VILLAGE_TANNERY_CHEST, LootTables.VILLAGE_TEMPLE_CHEST, LootTables.VILLAGE_DESERT_HOUSE_CHEST, LootTables.VILLAGE_PLAINS_CHEST, LootTables.VILLAGE_TAIGA_HOUSE_CHEST, LootTables.VILLAGE_SNOWY_HOUSE_CHEST, LootTables.VILLAGE_SAVANNA_HOUSE_CHEST};

    public static void init() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (LootTables.NETHER_BRIDGE_CHEST.equals(id) || LootTables.BASTION_TREASURE_CHEST.equals(id)) {
                LootPool pool = create(0.2F, SpellRegistry.SPELLS.get(Element.FIRE));
                tableBuilder.pool(pool);
            } else if (LootTables.FISHING_TREASURE_GAMEPLAY.equals(id) || LootTables.UNDERWATER_RUIN_BIG_CHEST.equals(id)) {
                LootPool pool = create(0.5F, SpellRegistry.SPELLS.get(Element.WATER));
                tableBuilder.pool(pool);
            } else if (Arrays.stream(VILLAGE_CHESTS).toList().contains(id)) {
                LootPool pool = create(0.06F, SpellRegistry.SPELLS.get(Element.SKY));
                tableBuilder.pool(pool);
            } else if (LootTables.ABANDONED_MINESHAFT_CHEST.equals(id) || LootTables.DESERT_PYRAMID_CHEST.equals(id)) {
                LootPool pool = create(0.22F, SpellRegistry.SPELLS.get(Element.EARTH));
                tableBuilder.pool(pool);
            } else if (LootTables.END_CITY_TREASURE_CHEST.equals(id) || LootTables.STRONGHOLD_LIBRARY_CHEST.equals(id)) {
                LootPool pool = create(0.2F, SpellRegistry.SPELLS.get(Element.END));
                tableBuilder.pool(pool);
            }
        });
    }

    private static LootPool create(float chance, List<Spell> spells) {
        LootPool.Builder builder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1)).conditionally(RandomChanceLootCondition.builder(chance));
        int weight = 100 / spells.size();
        for (Spell s : spells) {
            builder.with(ItemEntry.builder(SpellRegistry.getTome(s)).weight(weight).build());
        }
        return builder.build();
    }
}
