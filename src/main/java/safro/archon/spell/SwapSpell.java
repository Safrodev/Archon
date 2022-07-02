package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;

public class SwapSpell extends RaycastSpell {

    public SwapSpell(Element type, int manaCost) {
        super(type, manaCost, 20);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, ItemStack stack, LivingEntity target) {
        BlockPos t = target.getBlockPos();
        BlockPos p = player.getBlockPos();

        target.teleport(p.getX(), p.getY(), p.getZ(), true);
        player.teleport(t.getX(), t.getY(), t.getZ(), true);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_ENDER_EYE_LAUNCH;
    }
}
