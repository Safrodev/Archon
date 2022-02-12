package safro.archon.item.end;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import safro.archon.item.ManaWeapon;
import safro.archon.util.ArchonUtil;

public class VoidScepterItem extends ManaWeapon {

    public VoidScepterItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        HitResult hit = player.raycast(10, 0.0F, true);
        player.teleport(hit.getPos().x, hit.getPos().y, hit.getPos().z);
        world.createExplosion(player, player.getX(), player.getY(), player.getZ(), 3, false, Explosion.DestructionType.NONE);
        ArchonUtil.get(player).removeMana(20);
        world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 1.0F);
        return true;
    }

    public int getManaCost() {
        return 20;
    }
}
