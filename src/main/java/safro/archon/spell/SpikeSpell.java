package safro.archon.spell;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;

public class SpikeSpell extends RaycastSpell {

    public SpikeSpell(Element type, int manaCost) {
        super(type, manaCost, 10);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack, LivingEntity target) {
        BlockPos pos = target.getBlockPos();
        target.addVelocity(0D, 1.5D, 0D);
        if (world.getBlockState(pos).isAir() || world.getBlockState(pos).isReplaceable()) {
            world.setBlockState(pos, Blocks.POINTED_DRIPSTONE.getDefaultState());
        }
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_POINTED_DRIPSTONE_LAND;
    }
}
