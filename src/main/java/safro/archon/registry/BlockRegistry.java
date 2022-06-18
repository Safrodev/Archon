package safro.archon.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;
import safro.archon.block.ManaBerryBushBlock;
import safro.archon.block.ScriptureTableBlock;
import safro.archon.block.SummoningPedestalBlock;
import safro.archon.block.entity.ScriptureTableBlockEntity;
import safro.archon.block.entity.SummoningPedestalBlockEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockRegistry {
    private static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();

    // Core
    public static final Block SUMMONING_PEDESTAL = register("summoning_pedestal", new SummoningPedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS).nonOpaque()), true);
    public static BlockEntityType<SummoningPedestalBlockEntity> SUMMONING_PEDESTAL_BE;
    public static final Block SCRIPTURE_TABLE = register("scripture_table", new ScriptureTableBlock(FabricBlockSettings.of(Material.STONE).strength(2.5F, 8.0F).requiresTool().nonOpaque()), true);
    public static BlockEntityType<ScriptureTableBlockEntity> SCRIPTURE_TABLE_BE;

    // Misc + Building
    public static final Block GLISTEEL_BLOCK = register("glisteel_block", new Block(FabricBlockSettings.copy(Blocks.GOLD_BLOCK)), true);
    public static final Block MAGICAL_BOOKSHELF = register("magical_bookshelf", new Block(FabricBlockSettings.of(Material.WOOD).strength(1.5F).sounds(BlockSoundGroup.WOOD)), true);
    public static final Block MANA_BERRY_BUSH = register("mana_berry_bush", new ManaBerryBushBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)), false);

    // Ores
    public static final Block FIRE_NODE = register("fire_node", new OreBlock(FabricBlockSettings.of(Material.STONE).mapColor(MapColor.DARK_RED).strength(3.0F, 3.0F).sounds(BlockSoundGroup.NETHER_ORE).requiresTool(), UniformIntProvider.create(2, 5)), true);
    public static final Block WATER_NODE = register("water_node", new OreBlock(FabricBlockSettings.of(Material.STONE).strength(3.0F, 3.0F).requiresTool(), UniformIntProvider.create(2, 5)), true);
    public static final Block EARTH_NODE = register("earth_node", new OreBlock(FabricBlockSettings.of(Material.STONE).strength(3.5F, 5.0F).requiresTool(), UniformIntProvider.create(2, 5)), true);
    public static final Block SKY_NODE = register("sky_node", new OreBlock(FabricBlockSettings.of(Material.GLASS).mapColor(MapColor.WHITE_GRAY).strength(1.0F, 3.0F).requiresTool(), UniformIntProvider.create(2, 5)), true);
    public static final Block END_NODE = register("end_node", new OreBlock(FabricBlockSettings.of(Material.STONE).mapColor(MapColor.PALE_YELLOW).strength(3.0F, 9.0F).requiresTool(), UniformIntProvider.create(2, 5)), true);

    // Cloud
    public static final Block SOLID_CLOUD = register("solid_cloud", new Block(FabricBlockSettings.of(Material.GLASS).strength(0.2F, 0.5F).sounds(BlockSoundGroup.WOOL).mapColor(MapColor.WHITE_GRAY)), true);
    public static final Block CLOUD_IRON = register("cloud_iron", new OreBlock(FabricBlockSettings.of(Material.GLASS).mapColor(MapColor.WHITE_GRAY).strength(1.0F, 3.0F).requiresTool()), true);

    private static <T extends Block> T register(String name, T block, boolean createItem) {
        BLOCKS.put(block, new Identifier(Archon.MODID, name));
        if (createItem) {
            ItemRegistry.register(name, new BlockItem(block, new Item.Settings().group(Archon.ITEMGROUP)));
        }
        return block;
    }

    public static void init() {
        SUMMONING_PEDESTAL_BE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Archon.MODID, "summoning_pedestal"), FabricBlockEntityTypeBuilder.create(SummoningPedestalBlockEntity::new, SUMMONING_PEDESTAL).build(null));
        SCRIPTURE_TABLE_BE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Archon.MODID, "scripture_table"), FabricBlockEntityTypeBuilder.create(ScriptureTableBlockEntity::new, SCRIPTURE_TABLE).build(null));

        BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
    }
}
