package safro.archon.spell;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;

public class AirPrisonSpell extends RaycastSpell {

    public AirPrisonSpell(Element type, int manaCost) {
        super(type, manaCost, 5);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, ItemStack stack, LivingEntity target) {
        target.setMovementSpeed(0.0F);
        target.setVelocity(0.0D, target.getVelocity().getY(), 0.0D);
        target.slowMovement(Blocks.AIR.getDefaultState(), new Vec3d(1.0D, 1.0D, 1.0D));
    }

    @Override
    @Nullable
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_CHAIN_FALL;
    }
}
