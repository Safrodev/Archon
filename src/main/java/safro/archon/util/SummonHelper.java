package safro.archon.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import safro.archon.api.summon.SummonedMob;

import java.util.UUID;

public class SummonHelper {
    private static final UUID HEALTH_MOD = UUID.fromString("49943a65-9b4b-4063-a11f-b62644059a42");
    private static final UUID SPEED_MOD = UUID.fromString("85925d42-83c9-489c-a788-3e50153cce87");
    private static final UUID DAMAGE_MOD = UUID.fromString("c16af9de-4f50-4763-afa5-074573661af3");

    public static void spawnAndScale(ServerWorld world, PlayerEntity player, MobEntity entity, int soulPower, int baseLife) {
        entity.setTarget(player.getAttacking());
        ((SummonedMob)entity).archon$setOwner(player.getUuidAsString());
        double x = player.getX() + MathHelper.nextDouble(player.getRandom(), -3.0, 3.0);
        double z = player.getZ() + MathHelper.nextDouble(player.getRandom(), -3.0, 3.0);
        entity.refreshPositionAndAngles(x, player.getY(), z, player.getYaw(), player.getPitch());
        SummonHelper.setScaledLife(entity, soulPower, baseLife);
        SummonHelper.addStatScaling(entity, soulPower);
        world.spawnEntity(entity);
        SummonHelper.createParticlesAround(entity, world);
    }

    public static void createParticlesAround(LivingEntity entity, ServerWorld world) {
        for (int i = 0; i < 80; i++) {
            world.spawnParticles(ParticleTypes.LARGE_SMOKE, entity.getParticleX(0.5), entity.getRandomBodyY(), entity.getParticleZ(0.5), 1, 0.0, 0.0, 0.0, 0.15);
        }
    }

    public static void setScaledLife(LivingEntity entity, int soulPower, int base) {
        double scaled = (double)base * Math.pow(1.018, soulPower);
        int capped = (int)Math.min(scaled, base * 10.0);
        ((SummonedMob)entity).archon$setLifetime(capped);
    }

    public static void addStatScaling(LivingEntity entity, int soulPower) {
        double scaled = ((double)soulPower + 1.0) / 250.0;
        entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addTemporaryModifier(new EntityAttributeModifier(HEALTH_MOD, "Bonus Health", scaled, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addTemporaryModifier(new EntityAttributeModifier(SPEED_MOD, "Bonus Speed", scaled, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        entity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addTemporaryModifier(new EntityAttributeModifier(DAMAGE_MOD, "Bonus Damage", scaled, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    }
}
