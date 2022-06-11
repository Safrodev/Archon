package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.api.Element;
import safro.archon.api.Spell;

public class FireballSpell extends Spell {

    public FireballSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        Vec3d vec3d = player.getRotationVec(0.0F);
        double vX = (player.getX() + vec3d.x * 4.0D) - player.getX();
        double vY = (player.getY() + vec3d.y * 4.0D) - player.getY();
        double vZ = (player.getZ() + vec3d.z * 4.0D) - player.getZ();

        FireballEntity fireball = new FireballEntity(world, player, vX, vY, vZ, 2);
        fireball.updatePosition(player.getX(), player.getEyeY(), player.getZ() + vec3d.z * 2.0D);
        fireball.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.0F, 1.0F);
        world.spawnEntity(fireball);
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_BLAZE_SHOOT;
    }
}
