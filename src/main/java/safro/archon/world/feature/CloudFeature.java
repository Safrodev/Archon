package safro.archon.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import safro.archon.registry.BlockRegistry;

import java.util.Random;

public class CloudFeature extends Feature<DefaultFeatureConfig> {

    public CloudFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        float f = (float)(random.nextInt(3) + 4);
        for(int i = 0; f > 0.5F; --i) {
            for(int j = MathHelper.floor(-f); j <= MathHelper.ceil(f); ++j) {
                for(int k = MathHelper.floor(-f); k <= MathHelper.ceil(f); ++k) {
                    if ((float)(j * j + k * k) <= (f + 1.0F) * (f + 1.0F)) {
                        this.setBlockState(world, pos.add(j, i, k), BlockRegistry.SOLID_CLOUD.getDefaultState());

                        if (world.getBlockState(pos.add(j, i, k)).isOf(BlockRegistry.SOLID_CLOUD) && random.nextInt(50) <= 10) {
                            BlockState state;
                            if (random.nextBoolean()) {
                                state = BlockRegistry.CLOUD_IRON.getDefaultState();
                            } else {
                                state = BlockRegistry.SKY_NODE.getDefaultState();
                            }
                            this.setBlockState(world, pos.add(j, i, k), state);
                        }
                    }
                }
            }

            f = (float)((double)f - ((double)random.nextInt(2) + 0.5D));
        }
        return true;
    }
}
