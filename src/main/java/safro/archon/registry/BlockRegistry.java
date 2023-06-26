package safro.archon.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import safro.archon.Archon;
import safro.archon.block.ManaBerryBushBlock;
import safro.archon.block.ManaCatalystBlock;
import safro.archon.block.ScriptureTableBlock;
import safro.archon.block.SummoningPedestalBlock;
import safro.archon.block.entity.ManaCatalystBlockEntity;
import safro.archon.block.entity.ScriptureTableBlockEntity;
import safro.archon.block.entity.SummoningPedestalBlockEntity;
import safro.saflib.registry.BaseBlockItemRegistry;

public class BlockRegistry extends BaseBlockItemRegistry {
    static { MODID = Archon.MODID; }

    // Core
    public static final Block SUMMONING_PEDESTAL = register("summoning_pedestal", new SummoningPedestalBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS).nonOpaque()), true);
    public static BlockEntityType<SummoningPedestalBlockEntity> SUMMONING_PEDESTAL_BE = register("summoning_pedestal", SummoningPedestalBlockEntity::new, SUMMONING_PEDESTAL);
    public static final Block SCRIPTURE_TABLE = register("scripture_table", new ScriptureTableBlock(FabricBlockSettings.create().mapColor(MapColor.STONE_GRAY).strength(2.5F, 8.0F).requiresTool().nonOpaque()), true);
    public static BlockEntityType<ScriptureTableBlockEntity> SCRIPTURE_TABLE_BE = register("scripture_table", ScriptureTableBlockEntity::new, SCRIPTURE_TABLE);

    // Misc
    public static final Block MANA_CATALYST = register("mana_catalyst", new ManaCatalystBlock(0, FabricBlockSettings.copyOf(Blocks.DEEPSLATE).luminance(8)), true);
    public static final Block DIAMOND_MANA_CATALYST = register("diamond_mana_catalyst", new ManaCatalystBlock(1, FabricBlockSettings.copyOf(Blocks.DIAMOND_BLOCK).luminance(8)), true);
    public static final Block NETHERITE_MANA_CATALYST = register("netherite_mana_catalyst", new ManaCatalystBlock(3, FabricBlockSettings.copyOf(Blocks.NETHERITE_BLOCK).luminance(8)), true);
    public static BlockEntityType<ManaCatalystBlockEntity> MANA_CATALYST_BE = register("mana_catalyst", ManaCatalystBlockEntity::new, MANA_CATALYST, DIAMOND_MANA_CATALYST, NETHERITE_MANA_CATALYST);
    public static final Block GLISTEEL_BLOCK = register("glisteel_block", new Block(FabricBlockSettings.copy(Blocks.GOLD_BLOCK)), true);
    public static final Block MAGICAL_BOOKSHELF = register("magical_bookshelf", new Block(FabricBlockSettings.create().mapColor(MapColor.OAK_TAN).burnable().strength(1.5F).sounds(BlockSoundGroup.WOOD)), true);
    public static final Block MANA_BERRY_BUSH = register("mana_berry_bush", new ManaBerryBushBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().notSolid().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)), false);

    // Ores
    public static final Block FIRE_NODE = register("fire_node", new ExperienceDroppingBlock(FabricBlockSettings.create().mapColor(MapColor.STONE_GRAY).mapColor(MapColor.DARK_RED).strength(3.0F, 3.0F).sounds(BlockSoundGroup.NETHER_ORE).requiresTool(), UniformIntProvider.create(2, 5)), true);
    public static final Block WATER_NODE = register("water_node", new ExperienceDroppingBlock(FabricBlockSettings.create().mapColor(MapColor.STONE_GRAY).strength(3.0F, 3.0F).requiresTool(), UniformIntProvider.create(2, 5)), true);
    public static final Block EARTH_NODE = register("earth_node", new ExperienceDroppingBlock(FabricBlockSettings.create().mapColor(MapColor.STONE_GRAY).strength(3.5F, 5.0F).requiresTool(), UniformIntProvider.create(2, 5)), true);
    public static final Block SKY_NODE = register("sky_node", new ExperienceDroppingBlock(FabricBlockSettings.create().mapColor(MapColor.WHITE_GRAY).strength(1.0F, 3.0F).requiresTool(), UniformIntProvider.create(2, 5)), true);
    public static final Block END_NODE = register("end_node", new ExperienceDroppingBlock(FabricBlockSettings.create().mapColor(MapColor.STONE_GRAY).mapColor(MapColor.PALE_YELLOW).strength(3.0F, 9.0F).requiresTool(), UniformIntProvider.create(2, 5)), true);

    // Cloud
    public static final Block SOLID_CLOUD = register("solid_cloud", new Block(FabricBlockSettings.create().strength(0.2F, 0.5F).sounds(BlockSoundGroup.WOOL).mapColor(MapColor.WHITE_GRAY)), true);
    public static final Block CLOUD_IRON = register("cloud_iron", new ExperienceDroppingBlock(FabricBlockSettings.create().mapColor(MapColor.WHITE_GRAY).strength(1.0F, 3.0F).requiresTool()), true);

    public static void init() {
    }
}
