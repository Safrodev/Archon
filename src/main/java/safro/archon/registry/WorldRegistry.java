package safro.archon.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockStateMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import safro.archon.Archon;
import safro.archon.block.ManaBerryBushBlock;
import safro.archon.util.StructureHandler;
import safro.archon.world.feature.CloudFeature;
import safro.archon.world.feature.SpireFeature;

import java.util.List;

public class WorldRegistry {
    private static final int size = Archon.CONFIG.nodeVeinSize;
    private static final int chunk = Archon.CONFIG.nodeChunkRate;

    // Configured Features
    public static final ConfiguredFeature<?, ?> EARTH_NODE_CONFIGURED = Feature.ORE.configure(new OreFeatureConfig(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockRegistry.EARTH_NODE.getDefaultState(), size));
    public static final ConfiguredFeature<?, ?> WATER_NODE_CONFIGURED = Feature.ORE.configure(new OreFeatureConfig(OreConfiguredFeatures.BASE_STONE_OVERWORLD, BlockRegistry.WATER_NODE.getDefaultState(), size));
    public static final ConfiguredFeature<?, ?> FIRE_NODE_CONFIGURED = Feature.ORE.configure(new OreFeatureConfig(OreConfiguredFeatures.NETHERRACK, BlockRegistry.FIRE_NODE.getDefaultState(), size));
    public static final ConfiguredFeature<?, ?> END_NODE_CONFIGURED = Feature.ORE.configure(new OreFeatureConfig(new BlockStateMatchRuleTest(Blocks.END_STONE.getDefaultState()), BlockRegistry.END_NODE.getDefaultState(), size));

    public static final Feature<DefaultFeatureConfig> SKY_NODE = new CloudFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredFeature<?, ?> SKY_NODE_CONFIGURED = SKY_NODE.configure(FeatureConfig.DEFAULT);

    public static final ConfiguredFeature<?, ?> MBB_PATCH_CONFIGURED = Feature.RANDOM_PATCH.configure(ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(BlockStateProvider.of(BlockRegistry.MANA_BERRY_BUSH.getDefaultState().with(ManaBerryBushBlock.AGE, 3)))), List.of(Blocks.GRASS_BLOCK)));

    // Placed Features
    public static final PlacedFeature EARTH_NODE_PLACED = EARTH_NODE_CONFIGURED.withPlacement(modifiersWithCount(chunk, HeightRangePlacementModifier.uniform(YOffset.fixed(Archon.CONFIG.earthNodeMin),YOffset.fixed(Archon.CONFIG.earthNodeMax))));
    public static final PlacedFeature WATER_NODE_PLACED = WATER_NODE_CONFIGURED.withPlacement(modifiersWithCount(chunk,  HeightRangePlacementModifier.uniform(YOffset.fixed(Archon.CONFIG.waterNodeMin), YOffset.fixed(Archon.CONFIG.waterNodeMax))));
    public static final PlacedFeature FIRE_NODE_PLACED = FIRE_NODE_CONFIGURED.withPlacement(modifiersWithCount(chunk, PlacedFeatures.TEN_ABOVE_AND_BELOW_RANGE));
    public static final PlacedFeature END_NODE_PLACED = END_NODE_CONFIGURED.withPlacement(modifiersWithCount(chunk, HeightRangePlacementModifier.uniform(YOffset.fixed(Archon.CONFIG.endNodeMin),YOffset.fixed(Archon.CONFIG.endNodeMax))));
    public static final PlacedFeature SKY_NODE_PLACED = SKY_NODE_CONFIGURED.withPlacement(RarityFilterPlacementModifier.of(Archon.CONFIG.skyNodeChance), PlacedFeatures.createCountExtraModifier(1, 0.1F, 1), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.fixed(192), YOffset.fixed(196)), BiomePlacementModifier.of());

    public static final PlacedFeature MBB_PATCH_PLACED = MBB_PATCH_CONFIGURED.withPlacement(RarityFilterPlacementModifier.of(Archon.CONFIG.manaBerryBushChance), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());

    // Structure Features
    public static final StructureFeature<StructurePoolFeatureConfig> SPIRE = new SpireFeature(StructurePoolFeatureConfig.CODEC);
    public static final ConfiguredStructureFeature<StructurePoolFeatureConfig, ? extends StructureFeature<StructurePoolFeatureConfig>> SPIRE_CONFIGURED = SPIRE.configure(new StructurePoolFeatureConfig(() -> SpireFeature.SpireGenerator.POOLS, 1));

    private static List<PlacementModifier> modifiers(PlacementModifier first, PlacementModifier second) {
        return List.of(first, SquarePlacementModifier.of(), second, BiomePlacementModifier.of());
    }

    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier modifier) {
        return modifiers(CountPlacementModifier.of(count), modifier);
    }

    public static void init() {
        RegistryKey<ConfiguredFeature<?, ?>> earth_node_key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(Archon.MODID, "earth_node"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, earth_node_key.getValue(), EARTH_NODE_CONFIGURED);
        RegistryKey<PlacedFeature> earth_node_placed_key = RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(Archon.MODID, "earth_node"));
        Registry.register(BuiltinRegistries.PLACED_FEATURE, earth_node_placed_key.getValue(), EARTH_NODE_PLACED);

        RegistryKey<ConfiguredFeature<?, ?>> water_node_key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(Archon.MODID, "water_node"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, water_node_key.getValue(), WATER_NODE_CONFIGURED);
        RegistryKey<PlacedFeature> water_node_placed_key = RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(Archon.MODID, "water_node"));
        Registry.register(BuiltinRegistries.PLACED_FEATURE, water_node_placed_key.getValue(), WATER_NODE_PLACED);

        RegistryKey<ConfiguredFeature<?, ?>> fire_node_key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(Archon.MODID, "sky_node"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, fire_node_key.getValue(), FIRE_NODE_CONFIGURED);
        RegistryKey<PlacedFeature> fire_node_placed_key = RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(Archon.MODID, "fire_node"));
        Registry.register(BuiltinRegistries.PLACED_FEATURE, fire_node_placed_key.getValue(), FIRE_NODE_PLACED);

        RegistryKey<ConfiguredFeature<?, ?>> end_node_key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(Archon.MODID, "end_node"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, end_node_key.getValue(), END_NODE_CONFIGURED);
        RegistryKey<PlacedFeature> end_node_placed_key = RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(Archon.MODID, "end_node"));
        Registry.register(BuiltinRegistries.PLACED_FEATURE, end_node_placed_key.getValue(), END_NODE_PLACED);

        Registry.register(Registry.FEATURE, new Identifier(Archon.MODID, "sky_node"), SKY_NODE);
        RegistryKey<ConfiguredFeature<?, ?>> sky_node_key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(Archon.MODID, "cloud"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, sky_node_key.getValue(), SKY_NODE_CONFIGURED);
        RegistryKey<PlacedFeature> sky_node_placed_key = RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(Archon.MODID, "sky_node"));
        Registry.register(BuiltinRegistries.PLACED_FEATURE, sky_node_placed_key.getValue(), SKY_NODE_PLACED);

        RegistryKey<ConfiguredFeature<?, ?>> mbb_patch_key = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(Archon.MODID, "mana_berry_bush_patch"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, mbb_patch_key.getValue(), MBB_PATCH_CONFIGURED);
        RegistryKey<PlacedFeature> mbb_patch_placed_key = RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(Archon.MODID, "mana_berry_bush_patch"));
        Registry.register(BuiltinRegistries.PLACED_FEATURE, mbb_patch_placed_key.getValue(), MBB_PATCH_PLACED);

        StructureHandler.createSpire();
        RegistryKey<ConfiguredStructureFeature<?, ?>> spire_key = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(Archon.MODID, "spire"));
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, spire_key, SPIRE_CONFIGURED);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, earth_node_placed_key);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.OCEAN), GenerationStep.Feature.UNDERGROUND_ORES, water_node_placed_key);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, fire_node_placed_key);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, end_node_placed_key);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.TOP_LAYER_MODIFICATION, sky_node_placed_key);
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.TAIGA), GenerationStep.Feature.VEGETAL_DECORATION, mbb_patch_placed_key);
        BiomeModifications.create(new Identifier(Archon.MODID, "spire")).add(ModificationPhase.ADDITIONS, BiomeSelectors.categories(Biome.Category.FOREST, Biome.Category.MOUNTAIN), (context) -> context.getGenerationSettings().addBuiltInStructure(SPIRE_CONFIGURED));
    }
}
