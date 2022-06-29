package safro.archon.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import safro.archon.block.ManaCatalystBlock;
import safro.archon.registry.BlockRegistry;
import safro.archon.registry.EffectRegistry;

import java.util.List;

public class ManaCatalystBlockEntity extends BlockEntity {

    public ManaCatalystBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.MANA_CATALYST_BE, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, ManaCatalystBlockEntity blockEntity) {
        if (world.getBlockState(pos).getBlock() instanceof ManaCatalystBlock block) {
            int mana = block.getManaBoost();
            int range = 16 + (mana * 6);

            if (arePlayersNearby(world, pos, range)) {
                List<PlayerEntity> list = getPlayersNearby(world, pos, range);

                for (PlayerEntity player : list) {
                    if (pos.isWithinDistance(player.getBlockPos(), range)) {
                        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.MANA_BOOST, 40, mana, true, false, true));
                    }
                }
            }
        }
    }

    public static List<PlayerEntity> getPlayersNearby(World world, BlockPos pos, int range) {
        int k = pos.getX();
        int l = pos.getY();
        int m = pos.getZ();
        Box box = (new Box(k, l, m, k + 1, l + 1, m + 1)).expand(range);
        return world.getNonSpectatingEntities(PlayerEntity.class, box);
    }

    public static boolean arePlayersNearby(World world, BlockPos pos, int r) {
        return !getPlayersNearby(world, pos, r).isEmpty();
    }
}
