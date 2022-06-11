package safro.archon.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.entity.projectile.HitExecutor;
import safro.archon.entity.projectile.SpellProjectileEntity;

public class ProjectileHelper {

    public static SpellProjectileEntity create(World world, PlayerEntity player, ParticleEffect particle, ItemConvertible item, HitExecutor executor) {
        Vec3d vec3d = player.getRotationVec(0.0F);
        double vX = (player.getX() + vec3d.x * 4.0D) - player.getX();
        double vY = (player.getY() + vec3d.y * 4.0D) - player.getY();
        double vZ = (player.getZ() + vec3d.z * 4.0D) - player.getZ();

        SpellProjectileEntity projectile = new SpellProjectileEntity(world, player, vX, vY, vZ, executor, particle, new ItemStack(item));
        projectile.updatePosition(player.getX(), player.getEyeY(), player.getZ());
        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.0F, 1.0F);

        world.spawnEntity(projectile);
        return projectile;
    }
}
