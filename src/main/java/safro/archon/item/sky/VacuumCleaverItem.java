package safro.archon.item.sky;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import safro.archon.item.ManaWeapon;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class VacuumCleaverItem extends ManaWeapon {

    public VacuumCleaverItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(20D));
        if (list.size() > 0) {
            for (LivingEntity entity : list) {
                if (!(entity == player) || !canPull(player, entity)) {
                    double d = player.getX() - entity.getX();
                    double e = player.getY() - entity.getY();
                    double f = player.getZ() - entity.getZ();
                    entity.setVelocity(d * 0.3D, e * 0.1D + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * 0.3D);
                }
            }
            return true;
        }
        return false;
    }

    private boolean canPull(PlayerEntity player, LivingEntity entity) {
        if (entity instanceof PlayerEntity p) {
            if (p.getAbilities().flying) {
                return false;
            }
        }
        if (!ArchonUtil.isOwnedBy(player, entity)) {
            return entity.isAlive();
        }
        return false;
    }
}
