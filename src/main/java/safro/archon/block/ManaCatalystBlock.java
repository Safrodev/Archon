package safro.archon.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.block.entity.ManaCatalystBlockEntity;
import safro.archon.registry.BlockRegistry;

public class ManaCatalystBlock extends BlockWithEntity {
    private final int manaBoost;

    public ManaCatalystBlock(int boost, Settings settings) {
        super(settings);
        this.manaBoost = boost;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ManaCatalystBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockRegistry.MANA_CATALYST_BE, ManaCatalystBlockEntity::serverTick);
    }

    public int getManaBoost() {
        return this.manaBoost;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
