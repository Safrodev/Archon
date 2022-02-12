package safro.archon.item.water;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import safro.archon.entity.projectile.IceBallEntity;
import safro.archon.item.ManaWeapon;

public class FrostSwordItem extends ManaWeapon {

    public FrostSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public int getManaCost() {
        return 20;
    }

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        if (!world.isClient) {
            IceBallEntity iceball = new IceBallEntity(world, player);
            iceball.setItem(new ItemStack(Items.ICE));
            iceball.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(iceball);
        }
        return true;
    }
}
