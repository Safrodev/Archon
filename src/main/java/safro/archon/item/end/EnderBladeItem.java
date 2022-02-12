package safro.archon.item.end;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import safro.archon.item.ManaWeapon;

public class EnderBladeItem extends ManaWeapon {

    public EnderBladeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        HitResult hit = player.raycast(8, 0.0F, true);
        player.teleport(hit.getPos().x, hit.getPos().y, hit.getPos().z);
        world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5F, 1.0F);
        return true;
    }

    public int getManaCost() {
        return 10;
    }
}
