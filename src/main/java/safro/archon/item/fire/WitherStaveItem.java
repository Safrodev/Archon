package safro.archon.item.fire;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.item.ManaWeapon;

public class WitherStaveItem extends ManaWeapon {

    public WitherStaveItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public void activate(World world, PlayerEntity player, ItemStack stack) {
        Vec3d v = player.getRotationVec(1);
        WitherSkullEntity witherSkullEntity = new WitherSkullEntity(world, player, v.x * 50, v.y * 50, v.z * 50);
        witherSkullEntity.setOwner(player);
        witherSkullEntity.setPos(player.getEyePos().getX(), player.getEyePos().getY(), player.getEyePos().getZ());
        world.spawnEntity(witherSkullEntity);
        world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.PLAYERS, 0.5F, 1.0F);
    }

    public int getManaCost() {
        return 10;
    }
}
