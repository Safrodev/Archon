package safro.archon.registry;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import safro.archon.Archon;
import safro.archon.mixin.StructurePoolAccessor;
import safro.archon.util.WizardEnchantBookFactory;

import java.util.ArrayList;
import java.util.List;

public class VillagerRegistry {
    // Workstations
    public static final PointOfInterestType WIZARD_POI = PointOfInterestType.register("wizard", PointOfInterestType.getAllStatesOf(BlockRegistry.MAGICAL_BOOKSHELF), 1, 1);

    // Professions
    public static final VillagerProfession WIZARD = VillagerProfession.register("wizard", WIZARD_POI, SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN);

    // Trades & Houses
    public static void init() {
        TradeOffers.Factory[] WIZARD_LVL1 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.EARTH_GEM, 3, 64, 1), new TradeOffers.SellItemFactory(ItemRegistry.MANA_BERRIES, 2, 2, 40, 2)};
        TradeOffers.Factory[] WIZARD_LVL2 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.SKY_GEM, 3, 64, 2), new WizardEnchantBookFactory(5)};
        TradeOffers.Factory[] WIZARD_LVL3 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.WATER_GEM, 3, 64, 3), new TradeOffers.SellItemFactory(ItemRegistry.LIGHTNING_BOTTLE, 13, 1, 16, 8)};
        TradeOffers.Factory[] WIZARD_LVL4 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.FIRE_GEM, 3, 64, 4), new TradeOffers.SellItemFactory(ItemRegistry.CAPACITY_SCROLL, 60, 1, 3, 15)};
        TradeOffers.Factory[] WIZARD_LVL5 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.END_GEM, 3, 64, 5), new TradeOffers.SellItemFactory(ItemRegistry.ACCELERATE_SCROLL, 60, 1, 3, 15)};
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(WIZARD, intMap(ImmutableMap.of(1, WIZARD_LVL1,2, WIZARD_LVL2,3, WIZARD_LVL3,4, WIZARD_LVL4,5, WIZARD_LVL5)));

        ServerLifecycleEvents.SERVER_STARTING.register((server -> {
            int weight = Archon.CONFIG.wizard_village_weight;

            Identifier plains = new Identifier("minecraft:village/plains/houses");
            Identifier desert = new Identifier("minecraft:village/desert/houses");
            Identifier savanna = new Identifier("minecraft:village/savanna/houses");
            Identifier snowy = new Identifier("minecraft:village/snowy/houses");
            Identifier taiga = new Identifier("minecraft:village/taiga/houses");

            registerJigsaw(server, plains, new Identifier("archon:village/plains/houses/wizard_plains"), weight);
            registerJigsaw(server, desert, new Identifier("archon:village/plains/houses/wizard_desert"), weight);
            registerJigsaw(server, savanna, new Identifier("archon:village/savanna/houses/wizard_savanna"), weight);
            registerJigsaw(server, snowy, new Identifier("archon:village/taiga/houses/wizard_snowy"), weight);
            registerJigsaw(server, taiga, new Identifier("archon:village/taiga/houses/wizard_taiga"), weight);
        }));
    }

    private static Int2ObjectMap<TradeOffers.Factory[]> intMap(ImmutableMap<Integer, TradeOffers.Factory[]> trades) {
        return new Int2ObjectOpenHashMap<>(trades);
    }

    public static void registerJigsaw(MinecraftServer server, Identifier poolLocation, Identifier nbtLocation, int weight) {
        DynamicRegistryManager manager = server.getRegistryManager();
        Registry<StructurePool> pools = manager.get(Registry.STRUCTURE_POOL_KEY);
        StructurePool pool = pools.get(poolLocation);

        StructureProcessorList processorList = manager.get(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getOrEmpty(poolLocation).orElse(StructureProcessorLists.EMPTY);
        List<StructurePoolElement> templates = ((StructurePoolAccessor) pool).getElements();

        StructurePoolElement template = StructurePoolElement.ofProcessedLegacySingle(nbtLocation.toString(), processorList).apply(StructurePool.Projection.RIGID);
        for (int i = 0; i < weight; i++) {
            templates.add(template);
        }

        List<Pair<StructurePoolElement, Integer>> rawTemplates = new ArrayList<>(((StructurePoolAccessor) pool).getElementCounts());

        templates.addAll(((StructurePoolAccessor) pool).getElements());
        rawTemplates.addAll(((StructurePoolAccessor) pool).getElementCounts());

        ((StructurePoolAccessor) pool).setElements(templates);
        ((StructurePoolAccessor) pool).setElementCounts(rawTemplates);
    }
}
