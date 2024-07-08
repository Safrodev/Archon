package safro.archon.spell;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;

public class CrushSpell extends Spell {

    public CrushSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public ActionResult castOnBlock(ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        if (isValid(world, pos)) {
            BlockState state = world.getBlockState(pos);
            state.getBlock().afterBreak(world, player, pos, state, world.getBlockEntity(pos), stack);
            world.syncWorldEvent(2001, pos, Block.getRawIdFromState(state));
            world.removeBlock(pos, false);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private boolean isValid(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isAir() || !state.getFluidState().isEmpty() || state.isIn(BlockTags.WITHER_IMMUNE)) {
            return false;
        }
        return !state.isIn(BlockTags.NEEDS_DIAMOND_TOOL) && state.isIn(BlockTags.PICKAXE_MINEABLE);
    }

    @Override
    public boolean isBlockCasted() {
        return true;
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return null;
    }
}
