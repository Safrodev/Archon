package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.ArchonSchools;
import safro.archon.api.Element;
import safro.archon.api.spell.HitExecutor;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.entity.projectile.SpellProjectileEntity;
import safro.archon.registry.EntityRegistry;
import safro.archon.util.SpellUtil;

public class AstrofallSpell extends RaycastSpell {

    public AstrofallSpell(Element type, int manaCost) {
        super(type, manaCost, 30);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack, LivingEntity target) {
        HitExecutor executor = (entity, owner, projectile) -> {
            world.createExplosion(null, SpellDamageSource.create(ArchonSchools.VOID, player), null, entity.getPos(), 4.0F, false, World.ExplosionSourceType.NONE);
            SpellUtil.damage(player, entity, projectile, Element.END, 2.0D, 0.0D);
        };
        SpellParticleData data = SpellParticleData.of(170, 103, 215, 2.0F);

        for (int i = 0; i < 4; i++) {
            SpellProjectileEntity projectile = new SpellProjectileEntity(EntityRegistry.SPELL_PROJECTILE, world, player, executor);
            projectile.setPosition(target.getX(), target.getY() + 12, target.getZ());
            projectile.setVelocity(player, 90.0F, 0.0F, 0.0F, 0.8F - (i * 0.15F), 0.8F);
            projectile.setParticle(data.red(), data.green(), data.blue(), data.size());
            projectile.setSecondParticle(ParticleTypes.LARGE_SMOKE);
            world.spawnEntity(projectile);
        }
    }

    @Override
    public @Nullable SoundEvent getCastSound() {
        return SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE;
    }
}
