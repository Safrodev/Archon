package safro.archon.util;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import safro.archon.Archon;
import safro.archon.registry.WorldRegistry;
import safro.archon.world.feature.SpireFeature;

public class StructureHandler {
    public static void createSpire() {
        FabricStructureBuilder.create(new Identifier(Archon.MODID, "spire"), WorldRegistry.SPIRE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES).defaultConfig(spireConfig()).adjustsSurface().register();

        SpireFeature.SpireGenerator.initPool();
    }

    private static StructureConfig spireConfig() {
        return new StructureConfig(Archon.CONFIG.spire_spacing, Archon.CONFIG.spire_separation, 10387338);
    }
}
