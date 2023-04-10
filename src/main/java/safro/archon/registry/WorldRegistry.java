package safro.archon.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import safro.archon.Archon;
import safro.archon.world.feature.CloudFeature;

import java.util.function.Predicate;

public class WorldRegistry {
    public static final Feature<DefaultFeatureConfig> SKY_NODE = new CloudFeature(DefaultFeatureConfig.CODEC);

    public static void init() {
        Registry.register(Registries.FEATURE, new Identifier(Archon.MODID, "sky_node"), SKY_NODE);

        add(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, "earth_node");
        add(BiomeSelectors.tag(BiomeTags.IS_OCEAN), GenerationStep.Feature.UNDERGROUND_ORES, "water_node");
        add(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, "fire_node");
        add(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, "end_node");
        add(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.TOP_LAYER_MODIFICATION, "sky_node");
        add(BiomeSelectors.tag(BiomeTags.IS_TAIGA), GenerationStep.Feature.VEGETAL_DECORATION, "patch_mana_berry");
    }

    private static void add(Predicate<BiomeSelectionContext> ctx, GenerationStep.Feature step, String name) {
        BiomeModifications.addFeature(ctx, step, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(Archon.MODID, name)));
    }
}
