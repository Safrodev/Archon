package safro.archon.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockStateMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import safro.archon.Archon;
import safro.archon.block.ManaBerryBushBlock;
import safro.archon.mixin.StructureFeatureAccessor;
import safro.archon.world.feature.CloudFeature;
import safro.archon.world.feature.SpireFeature;

import java.util.List;
import java.util.function.Predicate;

public class WorldRegistry {
    private static final int size = Archon.CONFIG.nodeVeinSize;

    // Configured Features
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> EARTH_NODE_CONFIGURED = registerOre("earth_node", new OreFeatureConfig(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockRegistry.EARTH_NODE.getDefaultState(), size));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> WATER_NODE_CONFIGURED = registerOre("water_node", new OreFeatureConfig(OreConfiguredFeatures.BASE_STONE_OVERWORLD, BlockRegistry.WATER_NODE.getDefaultState(), size));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> FIRE_NODE_CONFIGURED = registerOre("fire_node", new OreFeatureConfig(OreConfiguredFeatures.NETHERRACK, BlockRegistry.FIRE_NODE.getDefaultState(), size));
    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> END_NODE_CONFIGURED = registerOre("end_node", new OreFeatureConfig(new BlockStateMatchRuleTest(Blocks.END_STONE.getDefaultState()), BlockRegistry.END_NODE.getDefaultState(), size));

    public static final Feature<DefaultFeatureConfig> SKY_NODE = new CloudFeature(DefaultFeatureConfig.CODEC);
    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> SKY_NODE_CONFIGURED = register("sky_node", SKY_NODE, FeatureConfig.DEFAULT);

    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> MBB_PATCH_CONFIGURED = register("mana_berry_bush_patch", Feature.RANDOM_PATCH, createPatch(BlockStateProvider.of(BlockRegistry.MANA_BERRY_BUSH.getDefaultState().with(ManaBerryBushBlock.AGE, 3)), 2));

    // Placed Features
    public static final RegistryEntry<PlacedFeature> EARTH_NODE_PLACED = register("earth_node", EARTH_NODE_CONFIGURED, modifiersWithCount(HeightRangePlacementModifier.uniform(YOffset.fixed(Archon.CONFIG.earthNodeMin), YOffset.fixed(Archon.CONFIG.earthNodeMax))));
    public static final RegistryEntry<PlacedFeature> WATER_NODE_PLACED = register("water_node", WATER_NODE_CONFIGURED, modifiersWithCount(HeightRangePlacementModifier.uniform(YOffset.fixed(Archon.CONFIG.waterNodeMin), YOffset.fixed(Archon.CONFIG.waterNodeMax))));
    public static final RegistryEntry<PlacedFeature> FIRE_NODE_PLACED = register("fire_node", FIRE_NODE_CONFIGURED, List.of(PlacedFeatures.TEN_ABOVE_AND_BELOW_RANGE));
    public static final RegistryEntry<PlacedFeature> END_NODE_PLACED = register("end_node", END_NODE_CONFIGURED, modifiersWithCount(HeightRangePlacementModifier.uniform(YOffset.fixed(Archon.CONFIG.endNodeMin), YOffset.fixed(Archon.CONFIG.endNodeMax))));
    public static final RegistryEntry<PlacedFeature> SKY_NODE_PLACED = register("sky_node", SKY_NODE_CONFIGURED, List.of(RarityFilterPlacementModifier.of(Archon.CONFIG.skyNodeChance), PlacedFeatures.createCountExtraModifier(1, 0.25F, 1), SquarePlacementModifier.of(), HeightRangePlacementModifier.uniform(YOffset.fixed(192), YOffset.fixed(196)), BiomePlacementModifier.of()));

    public static final RegistryEntry<PlacedFeature> MBB_PATCH_PLACED = register("mana_berry_bush_patch", MBB_PATCH_CONFIGURED, List.of(new PlacementModifier[]{RarityFilterPlacementModifier.of(Archon.CONFIG.manaBerryBushChance), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of()}));

    // Structure Features
    public static final StructureFeature<StructurePoolFeatureConfig> SPIRE = new SpireFeature(StructurePoolFeatureConfig.CODEC);

    private static <C extends FeatureConfig, F extends Feature<C>> RegistryEntry<ConfiguredFeature<C, ?>> registerOre(String id, C config) {
        return BuiltinRegistries.method_40360(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Archon.MODID, id).toString(), new ConfiguredFeature(Feature.ORE, config));
    }

    private static RandomPatchFeatureConfig createPatch(BlockStateProvider block, int tries) {
        return ConfiguredFeatures.createRandomPatchFeatureConfig(tries, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(block)));
    }

    private static <C extends FeatureConfig, F extends Feature<C>> RegistryEntry<ConfiguredFeature<C, ?>> register(String id, F feature, C config) {
        return BuiltinRegistries.method_40360(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Archon.MODID, id).toString(), new ConfiguredFeature<>(feature, config));
    }

    private static RegistryEntry<PlacedFeature> register(String id, RegistryEntry<? extends ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
        return PlacedFeatures.register(new Identifier(Archon.MODID, id).toString(), feature, modifiers);
    }

    private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }

    private static List<PlacementModifier> modifiersWithCount(PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(Archon.CONFIG.nodeChunkRate), heightModifier);
    }

    public static void init() {
        Registry.register(Registry.FEATURE, new Identifier(Archon.MODID, "sky_node"), SKY_NODE);

        structure("spire", SPIRE, GenerationStep.Feature.SURFACE_STRUCTURES);

        add(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, EARTH_NODE_PLACED);
        add(BiomeSelectors.categories(Biome.Category.OCEAN), GenerationStep.Feature.UNDERGROUND_ORES, WATER_NODE_PLACED);
        add(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, FIRE_NODE_PLACED);
        add(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, END_NODE_PLACED);
        add(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.TOP_LAYER_MODIFICATION, SKY_NODE_PLACED);
        add(BiomeSelectors.categories(Biome.Category.TAIGA), GenerationStep.Feature.VEGETAL_DECORATION, MBB_PATCH_PLACED);
    }

    private static void add(Predicate<BiomeSelectionContext> ctx, GenerationStep.Feature step, RegistryEntry<PlacedFeature> feature) {
        feature.getKey().ifPresent(key -> BiomeModifications.addFeature(ctx, step, key));
    }

    private static void structure(String name, StructureFeature feature, GenerationStep.Feature step) {
        Registry.register(Registry.STRUCTURE_FEATURE, new Identifier(Archon.MODID, name), feature);
        StructureFeatureAccessor.getStep().put(feature, step);
    }
}
