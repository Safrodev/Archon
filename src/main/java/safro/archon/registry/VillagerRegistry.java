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
import net.minecraft.util.registry.RegistryEntry;
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
    }

    private static Int2ObjectMap<TradeOffers.Factory[]> intMap(ImmutableMap<Integer, TradeOffers.Factory[]> trades) {
        return new Int2ObjectOpenHashMap<>(trades);
    }
}
