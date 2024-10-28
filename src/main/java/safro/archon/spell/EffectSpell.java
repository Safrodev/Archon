package safro.archon.spell;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.registry.SpellRegistry;

public class EffectSpell extends Spell {
    private final StatusEffectInstance instance;

    public EffectSpell(StatusEffectInstance instance, Element type, int manaCost) {
        super(type, manaCost);
        this.instance = instance;
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        int amplifier = instance.getAmplifier() == 0 ? 0 : (int) MathHelper.clamp(power.nonCriticalValue(), 1.0D, 10.0D) - 1;
        int duration = instance.getDuration() + ((int)power.nonCriticalValue() * 40);
        StatusEffectInstance effect = new StatusEffectInstance(instance.getEffectType(), instance.getAmplifier() == 0 ? duration : instance.getDuration(), amplifier, instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon());
        player.addStatusEffect(effect);

        // Hard-coded for Aqua Shield
        if (this == SpellRegistry.AQUA_SHIELD) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, duration, 3, false, false, true));
        }
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL;
    }
}
