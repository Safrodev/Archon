package safro.archon.item.sky;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import safro.archon.item.ManaWeapon;

public class VacuumCleaverItem extends ManaWeapon {

    public VacuumCleaverItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public void activate(World world, PlayerEntity player, ItemStack stack) {
        for (LivingEntity entity : world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(20D))) {
            if (!(entity == player) || !canPull(entity)) {
                double d = player.getX() - entity.getX();
                double e = player.getY() - entity.getY();
                double f = player.getZ() - entity.getZ();
                entity.setVelocity(d * 0.3D, e * 0.1D + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * 0.3D);
            }
        }
    }

    private boolean canPull(LivingEntity entity) {
        if (entity instanceof PlayerEntity p) {
            if (p.getAbilities().flying) {
                return false;
            }
        }
        return entity.isAlive();
    }
}
