package safro.archon.spell;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.entity.projectile.SpellProjectileEntity;
import safro.archon.util.SpellUtil;

import java.util.List;

public class DarkballSpell extends Spell {

    public DarkballSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        SpellUtil.shoot(world, player, SpellParticleData.of(197, 32, 230, 0.9F), 0.6F, ((target, owner, projectile) -> {
            this.spawnEffect(player, projectile, power);
        }));
    }

    private void spawnEffect(PlayerEntity player, SpellProjectileEntity projectile, SpellPower.Result power) {
        if (!projectile.getWorld().isClient()) {
            List<LivingEntity> list = projectile.getWorld().getNonSpectatingEntities(LivingEntity.class, projectile.getBoundingBox().expand(4.0, 2.0, 4.0));
            AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(projectile.getWorld(), projectile.getX(), projectile.getY(), projectile.getZ());
            areaEffectCloudEntity.setOwner(player);

            areaEffectCloudEntity.setParticleType(ParticleTypes.DRAGON_BREATH);
            areaEffectCloudEntity.setRadius(2.0F);
            areaEffectCloudEntity.setDuration(100);
            areaEffectCloudEntity.setRadiusGrowth((7.0F - areaEffectCloudEntity.getRadius()) / (float)areaEffectCloudEntity.getDuration());

            int damage = (int)MathHelper.clamp(power.nonCriticalValue(), 1.0D, 10.0D);
            areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, damage));
            if (!list.isEmpty()) {
                for (LivingEntity livingEntity : list) {
                    double d = projectile.squaredDistanceTo(livingEntity);
                    if (d < 16.0) {
                        areaEffectCloudEntity.setPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                        break;
                    }
                }
            }

            projectile.getWorld().syncWorldEvent(WorldEvents.DRAGON_BREATH_CLOUD_SPAWNS, projectile.getBlockPos(), projectile.isSilent() ? -1 : 1);
            projectile.getWorld().spawnEntity(areaEffectCloudEntity);
        }
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_ENDER_DRAGON_SHOOT;
    }
}
