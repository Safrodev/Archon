package safro.archon.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import safro.archon.registry.BlockRegistry;
import safro.saflib.block.entity.BasicBlockEntity;

public class NecroticInfuserBlockEntity extends BasicBlockEntity {

    public NecroticInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.MANA_CATALYST_BE, pos, state);
    }
}
