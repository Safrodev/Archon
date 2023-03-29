package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.entity.projectile.spell.HellbeamEntity;
import safro.archon.util.ArchonDamageSource;
import safro.archon.util.SpellUtil;

public class HellbeamSpell extends RaycastSpell {

    public HellbeamSpell(Element type, int manaCost) {
        super(type, manaCost, 25);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, ItemStack stack, LivingEntity target) {
        Vec3d vec3d = player.getRotationVec(1.0F);
        double f = target.getX() - (player.getX() + vec3d.x * 4.0D);
        double g = target.getBodyY(0.5D) - player.getEyeY() - 0.5D;
        double h = target.getZ() - (player.getZ() + vec3d.z * 4.0D);
        HellbeamEntity beam = new HellbeamEntity(world, player, f, g, h, new ItemStack(Items.BLAZE_POWDER), ((target1, owner, projectile) -> {
            SpellUtil.damage(player, target1, this, 5.0F, ArchonDamageSource.HELLBEAM);
        }));
        SpellUtil.spawn(world, player, beam, 5.0F);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return null;
    }
}
