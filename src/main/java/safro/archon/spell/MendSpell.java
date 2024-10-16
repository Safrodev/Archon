package safro.archon.spell;

import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.registry.ParticleRegistry;

import java.util.List;

public class MendSpell extends Spell {

    public MendSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        List<StatusEffect> list = player.getActiveStatusEffects().keySet().stream().filter(effect -> effect.getCategory().equals(StatusEffectCategory.HARMFUL)).toList();
        list.forEach(player::removeStatusEffect);

        player.heal(6.0F * (float)power.nonCriticalValue());
        if (world instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 3; i++) {
                this.displayParticles(serverWorld, player.getBlockPos().up(), player.getRandom());
            }
        }
    }

    private void displayParticles(ServerWorld world, BlockPos pos, Random random) {
        for (BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
            if (random.nextInt(16) == 0) {
                world.spawnParticles(ParticleRegistry.WATER_BALL, (double) pos.getX() + 0.5D, (double) pos.getY() + 2.0D, (double) pos.getZ() + 0.5D, 1, (double) ((float) blockPos.getX() + random.nextFloat()) - 0.5D, (float) blockPos.getY() - random.nextFloat() - 1.0F, (double) ((float) blockPos.getZ() + random.nextFloat()) - 0.5D, 1.0);
            }
        }
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.AMBIENT_UNDERWATER_EXIT;
    }
}
