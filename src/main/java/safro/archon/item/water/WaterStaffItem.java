package safro.archon.item.water;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
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
    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        WaterBoltEntity bolt = new WaterBoltEntity(world, player, 5.0F, 5.0F, 5.0F);
        bolt.setPosition(player.getX(), player.getBodyY(0.5D), bolt.getZ());
        bolt.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.5F, 1.0F);
        world.spawnEntity(bolt);
        return true;
    }
}
