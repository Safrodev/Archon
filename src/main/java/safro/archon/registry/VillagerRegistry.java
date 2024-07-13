package safro.archon.registry;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import safro.archon.Archon;
import safro.archon.util.WizardEnchantBookFactory;

public class VillagerRegistry {
    // Workstations
    public static final RegistryKey<PointOfInterestType> WIZARD_POI_KEY = RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(Archon.MODID, "wizard"));

    // Professions
    public static final VillagerProfession WIZARD = VillagerProfession.register("wizard", WIZARD_POI_KEY, SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN);

    // Trades
    public static void init() {
        PointOfInterestHelper.register(new Identifier(Archon.MODID, "wizard"), 1, 1, BlockRegistry.MAGICAL_BOOKSHELF);

        TradeOffers.Factory[] WIZARD_LVL1 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.EARTH_ESSENCE, 3, 64, 1), new TradeOffers.SellItemFactory(ItemRegistry.MANA_BERRIES, 2, 2, 40, 2)};
        TradeOffers.Factory[] WIZARD_LVL2 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.SKY_ESSENCE, 3, 64, 2), new WizardEnchantBookFactory(5)};
        TradeOffers.Factory[] WIZARD_LVL3 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.WATER_ESSENCE, 3, 64, 3), new TradeOffers.SellItemFactory(ItemRegistry.LIGHTNING_BOTTLE, 13, 1, 16, 8)};
        TradeOffers.Factory[] WIZARD_LVL4 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.FIRE_ESSENCE, 3, 64, 4), new TradeOffers.SellItemFactory(ItemRegistry.CAPACITY_SCROLL, 60, 1, 3, 15)};
        TradeOffers.Factory[] WIZARD_LVL5 = new TradeOffers.Factory[]{new TradeOffers.BuyForOneEmeraldFactory(ItemRegistry.END_ESSENCE, 3, 64, 5), new TradeOffers.SellItemFactory(ItemRegistry.ACCELERATE_SCROLL, 60, 1, 3, 15)};
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(WIZARD, intMap(ImmutableMap.of(1, WIZARD_LVL1,2, WIZARD_LVL2,3, WIZARD_LVL3,4, WIZARD_LVL4,5, WIZARD_LVL5)));
    }

    private static Int2ObjectMap<TradeOffers.Factory[]> intMap(ImmutableMap<Integer, TradeOffers.Factory[]> trades) {
        return new Int2ObjectOpenHashMap<>(trades);
    }
}
