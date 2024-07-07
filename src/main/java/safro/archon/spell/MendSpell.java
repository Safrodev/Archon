package safro.archon.spell;

import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.registry.ParticleRegistry;
import safro.saflib.network.ParticlePacket;

import java.util.List;

public class MendSpell extends Spell {

    public MendSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        List<StatusEffect> list = player.getActiveStatusEffects().keySet().stream().filter(effect -> effect.getCategory().equals(StatusEffectCategory.HARMFUL)).toList();
        list.forEach(player::removeStatusEffect);

        player.heal(8.0F);
        if (player instanceof ServerPlayerEntity serverPlayer) {
            for (int i = 0; i < 3; i++) {
                this.displayParticles(serverPlayer, player.getBlockPos().up(), player.getRandom());
            }
        }
    }

    private void displayParticles(ServerPlayerEntity player, BlockPos pos, Random random) {
        for (BlockPos blockPos : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
            if (random.nextInt(16) == 0) {
                ParticlePacket.send(player, ParticleRegistry.WATER_BALL, (double) pos.getX() + 0.5D, (double) pos.getY() + 2.0D, (double) pos.getZ() + 0.5D, (double) ((float) blockPos.getX() + random.nextFloat()) - 0.5D, (float) blockPos.getY() - random.nextFloat() - 1.0F, (double) ((float) blockPos.getZ() + random.nextFloat()) - 0.5D);
            }
        }
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.AMBIENT_UNDERWATER_EXIT;
    }
}
