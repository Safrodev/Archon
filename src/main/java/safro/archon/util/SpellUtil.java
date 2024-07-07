package safro.archon.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.HitExecutor;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.entity.projectile.spell.SpellProjectileEntity;
import safro.archon.registry.EntityRegistry;

public class SpellUtil {

    public static void shoot(World world, PlayerEntity player, SpellParticleData data, HitExecutor hitExecutor, float speed) {
        shoot(world, player, data, new SpellProjectileEntity(EntityRegistry.SPELL_PROJECTILE, world, player, hitExecutor), speed);
    }

    public static void shoot(World world, PlayerEntity player, SpellParticleData data, SpellProjectileEntity projectile, float speed) {
        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, speed, 0.8F);
        projectile.setParticle(data.red(), data.green(), data.blue(), data.size());
        world.spawnEntity(projectile);
    }

    public static void damage(PlayerEntity caster, LivingEntity target, SpellProjectileEntity projectile, Element element, double multiplier, double knockback) {
        SpellPower.Result result = SpellPower.getSpellPower(element.getSchool(), caster);
        SpellPower.Vulnerability vulnerability = SpellPower.getVulnerability(target, element.getSchool());
        double damage = result.randomValue(vulnerability);
        damage *= multiplier;

        caster.onAttacking(target);
        if (target.damage(SpellDamageSource.create(element.getSchool(), caster), (float) damage)) {
            if (knockback > 0.0D) {
                double resistance = Math.max(0.0D, 1.0D - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                Vec3d vec3d = projectile.getVelocity().multiply(1.0D, 0.0D, 1.0D).normalize().multiply(knockback * 0.6D * resistance);
                if (vec3d.lengthSquared() > 0.0D) {
                    target.addVelocity(vec3d.x, 0.1D, vec3d.z);
                }
            }

            EnchantmentHelper.onUserDamaged(target, caster);
            EnchantmentHelper.onTargetDamaged(caster, target);
        }
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
}
