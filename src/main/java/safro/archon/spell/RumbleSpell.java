package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.network.ShakePacket;

public class RumbleSpell extends RaycastSpell {

    public RumbleSpell(Element type, int manaCost) {
        super(type, manaCost, 20);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack, LivingEntity target) {
        world.getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(10)).forEach(entity -> {
            if (entity != player) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 3, true, false, false));
                if (entity instanceof ServerPlayerEntity p) {
                    ShakePacket.send(p);
                }
                BlockPos blockPos = entity.getBlockPos();
                ((ServerWorld) world).spawnParticles(ParticleTypes.LARGE_SMOKE, (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.25D, (double)blockPos.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
                ((ServerWorld) world).spawnParticles(ParticleTypes.LARGE_SMOKE, (double)blockPos.getX() + 0.3D, (double)blockPos.getY() + 0.25D, (double)blockPos.getZ() + 0.3D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
                ((ServerWorld) world).spawnParticles(ParticleTypes.LARGE_SMOKE, (double)blockPos.getX() + 0.8D, (double)blockPos.getY() + 0.1D, (double)blockPos.getZ() + 0.8D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
            }
        });
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM.value();
    }
}
