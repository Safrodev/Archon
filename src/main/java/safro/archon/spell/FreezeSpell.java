package safro.archon.spell;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.util.SpellUtil;

public class FreezeSpell extends Spell {

    public FreezeSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        int duration = 200 + 40 * (int)power.nonCriticalValue();
        SpellUtil.shoot(world, player, SpellParticleData.of(110, 224, 235, 0.25F), 0.8F, (target, owner, projectile) -> {
          target.setFrozenTicks(duration);
          target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 8, true, false, false));
        });
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_PLAYER_HURT_FREEZE;
    }
}
