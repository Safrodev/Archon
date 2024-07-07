package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;

public class DarkballSpell extends Spell {

    public DarkballSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        Vec3d vec3d = player.getRotationVec(0.0F);
        double vX = (player.getX() + vec3d.x * 4.0D) - player.getX();
        double vY = (player.getY() + vec3d.y * 4.0D) - player.getY();
        double vZ = (player.getZ() + vec3d.z * 4.0D) - player.getZ();

        DragonFireballEntity fireball = new DragonFireballEntity(world, player, vX, vY, vZ);
        fireball.updatePosition(player.getX(), player.getEyeY(), player.getZ() + vec3d.z);
        fireball.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.0F, 1.0F);
        world.spawnEntity(fireball);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_ENDER_DRAGON_SHOOT;
    }
}
