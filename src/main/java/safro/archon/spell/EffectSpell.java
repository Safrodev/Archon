package safro.archon.spell;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;

public class EffectSpell extends Spell {
    private final StatusEffectInstance instance;

    public EffectSpell(StatusEffectInstance instance, Element type, int manaCost) {
        super(type, manaCost);
        this.instance = instance;
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        StatusEffectInstance effect = new StatusEffectInstance(instance.getEffectType(), instance.getDuration(), instance.getAmplifier(), instance.isAmbient(), instance.shouldShowParticles(), instance.shouldShowIcon());
        player.addStatusEffect(effect);
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL;
    }
}
