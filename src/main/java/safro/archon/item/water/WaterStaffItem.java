package safro.archon.item.water;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.entity.projectile.WaterBoltEntity;
import safro.archon.item.ManaWeapon;

public class WaterStaffItem extends ManaWeapon {

    public WaterStaffItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public void activate(World world, PlayerEntity player, ItemStack stack) {
        Vec3d vec3d = player.getRotationVec(1.0F);
        WaterBoltEntity bolt = new WaterBoltEntity(world, player, vec3d.x * 50D, vec3d.y * 50, vec3d.z * 50);
        bolt.setPosition(player.getX() + vec3d.x * 4.0D, player.getBodyY(0.5D), bolt.getZ() + vec3d.z * 4.0D);
        bolt.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.5F + (float)0 * 0.5F, 1.0F);
        world.spawnEntity(bolt);
    }
}
