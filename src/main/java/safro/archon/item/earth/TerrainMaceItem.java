package safro.archon.item.earth;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import safro.archon.item.ManaWeapon;

public class TerrainMaceItem extends ManaWeapon {

    public TerrainMaceItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public void activate(World world, PlayerEntity player, ItemStack stack) {
        for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(16D), EntityPredicates.VALID_LIVING_ENTITY)) {
            if (!(e instanceof TameableEntity) && !(e == player)) {
                e.damage(DamageSource.player(player), 5);
                e.addVelocity(0, 2, 0);
            }
        }
        world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.PLAYERS, 0.5F, 1.0F);
    }

    public int getManaCost() {
        return 50;
    }
}
