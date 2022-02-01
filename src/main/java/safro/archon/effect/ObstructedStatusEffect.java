package safro.archon.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ObstructedStatusEffect extends StatusEffect {

    public ObstructedStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x20156E);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
