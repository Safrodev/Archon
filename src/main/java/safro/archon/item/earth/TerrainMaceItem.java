package safro.archon.item.earth;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import safro.archon.item.SpellWeaponItem;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class TerrainMaceItem extends SpellWeaponItem {

    public TerrainMaceItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        List<LivingEntity> list = world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(16D), EntityPredicates.VALID_LIVING_ENTITY);
        list.remove(player);
        if (list.size() > 0) {
            for (LivingEntity e : list) {
                if (!ArchonUtil.isOwnedBy(player, e) && !(e == player)) {
                    e.damage(world.getDamageSources().playerAttack(player), 5);
                    e.addVelocity(0, 2, 0);
                }
            }
            world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.PLAYERS, 0.5F, 1.0F);
            return true;
        }
        return false;
    }

    public int getManaCost() {
        return 50;
    }
}
