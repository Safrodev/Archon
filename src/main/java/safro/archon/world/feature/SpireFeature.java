package safro.archon.world.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.PostPlacementProcessor;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import safro.archon.Archon;

import java.util.Optional;

public class SpireFeature extends StructureFeature<StructurePoolFeatureConfig> {

    public SpireFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec, SpireFeature::addPieces, PostPlacementProcessor.EMPTY);
    }

    private static <C extends FeatureConfig> boolean canGenerate(StructureGeneratorFactory.Context<C> context) {
        if (!context.isBiomeValid(Heightmap.Type.WORLD_SURFACE_WG)) {
            return false;
        } else {
            return context.getMinCornerHeight(13, 28) >= context.chunkGenerator().getSeaLevel();
        }
    }

    private static Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> addPieces(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
        BlockPos pos = new BlockPos(context.chunkPos().getStartX(), 0, context.chunkPos().getStartZ());
        if (canGenerate(context)) {
            return StructurePoolBasedGenerator.generate(context, PoolStructurePiece::new, pos, true, true);
        } else
            return Optional.empty();
    }

    public static class SpireGenerator {
        public static final StructurePool POOLS;

        public static void initPool() {
        }

        static {
            POOLS = StructurePools.register(new StructurePool(new Identifier(Archon.MODID, "spire"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofLegacySingle("archon:spire"), 1)), StructurePool.Projection.RIGID));
        }
    }
}
