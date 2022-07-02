package safro.archon.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import safro.archon.api.ManaAttributes;
import safro.archon.util.ArchonUtil;

public class ManaBoostStatusEffect extends CustomStatusEffect {

    public ManaBoostStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity player) {
            double amount = 50D * (amplifier + 1);
            ArchonUtil.get(player).addMaxModifier(ManaAttributes.MANA_BOOST_MODIFIER, "Mana Boost", amount, false);
        }
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity player) {
            ArchonUtil.get(player).removeMaxModifier(ManaAttributes.MANA_BOOST_MODIFIER);
            ArchonUtil.get(player).clampMana();
        }
    }
}
