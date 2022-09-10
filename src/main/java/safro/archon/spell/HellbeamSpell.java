package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.entity.projectile.HitExecutor;
import safro.archon.entity.projectile.SpellProjectileEntity;
import safro.archon.entity.projectile.TickExecutor;

public class HellbeamSpell extends RaycastSpell {

    public HellbeamSpell(Element type, int manaCost) {
        super(type, manaCost, 20);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, ItemStack stack, LivingEntity target) {
        Vec3d vec3d = player.getRotationVec(1.0F);
        double f = target.getX() - (player.getX() + vec3d.x * 4.0D);
        double g = target.getBodyY(0.5D) - player.getEyeY();
        double h = target.getZ() - (player.getZ() + vec3d.z * 4.0D);

        TickExecutor tickExecutor = p -> world.addParticle(ParticleTypes.FLAME, p.getX(), p.getY(), p.getZ(), 0, 0, 0);
        SpellProjectileEntity projectile = new SpellProjectileEntity(world, player, f, g, h, HitExecutor.EMPTY, tickExecutor, new ItemStack(Items.AIR, 2));
        projectile.updatePosition(player.getX() + vec3d.x * 4.0D, player.getEyeY(), projectile.getZ() + vec3d.z * 4.0D);
//        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 4.0F, 1.0F);
        world.spawnEntity(projectile);

        target.damage(DamageSource.player(player), 5.0F);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return null;
    }
}
