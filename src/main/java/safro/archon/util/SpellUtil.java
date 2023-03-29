package safro.archon.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.Archon;
import safro.archon.api.Spell;
import safro.archon.compat.SpellPowerCompat;
import safro.archon.entity.projectile.spell.HitExecutor;
import safro.archon.entity.projectile.spell.SpellProjectileEntity;
import safro.archon.registry.EntityRegistry;

import javax.annotation.Nullable;

public class SpellUtil {

    public static SpellProjectileEntity create(World world, PlayerEntity player, ItemConvertible item, HitExecutor executor) {
        return create(world, player, item, executor, 2.0F);
    }

    public static SpellProjectileEntity create(World world, PlayerEntity player, ItemConvertible item, HitExecutor hitExecutor, float speed) {
        return spawn(world, player, new SpellProjectileEntity(EntityRegistry.SPELL_PROJECTILE, world, player, hitExecutor, new ItemStack(item)), speed);
    }

    public static SpellProjectileEntity spawn(World world, LivingEntity player, SpellProjectileEntity projectile, float speed) {
        projectile.updatePosition(player.getX(), player.getEyeY(), player.getZ());
        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, speed, 1.0F);
        world.spawnEntity(projectile);
        return projectile;
    }

    public static boolean damage(PlayerEntity caster, LivingEntity target, Spell spell, float damage) {
        return damage(caster, target, spell, damage, DamageSource.MAGIC.setProjectile());
    }

    public static boolean damage(PlayerEntity caster, LivingEntity target, Spell spell, float damage, DamageSource source) {
        if (CompatUtil.isSpellPowerInstalled() && Archon.CONFIG.enableSpellPowerCompat) {
            double i = SpellPowerCompat.getBonusDamage(caster, target, spell);
            float bonus = damage + Double.valueOf(i).floatValue();
            return SpellPowerCompat.damage(caster, target, spell, bonus);
        }
        return target.damage(source, damage);
    }

    @Nullable
    public static Entity getTargeted(PlayerEntity player, double range) {
        double e = range * range;
        Vec3d vec = player.getCameraPosVec(1.0F);
        Vec3d vec3d2 = player.getRotationVec(1.0F);
        Vec3d vec3d3 = vec.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        Box box = player.getBoundingBox().stretch(vec3d2.multiply(range)).expand(1.0D, 1.0D, 1.0D);

        EntityHitResult hit = ProjectileUtil.raycast(player, vec, vec3d3, box, (entityx) -> !entityx.isSpectator() && entityx.canHit(), e);
        if (hit == null || hit.getEntity() == null) {
            return null;
        }
        return hit.getEntity();
    }

    public static double getRotationX(PlayerEntity player) {
        return (player.getX() + player.getRotationVec(0.0F).x * 4.0D) - player.getX();
    }

    public static double getRotationY(PlayerEntity player) {
        return (player.getY() + player.getRotationVec(0.0F).y * 4.0D) - player.getY();
    }

    public static double getRotationZ(PlayerEntity player) {
        return (player.getZ() + player.getRotationVec(0.0F).z * 4.0D) - player.getZ();
    }
}
