package safro.archon.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import safro.archon.util.ArchonUtil;

public class ManaBoostStatusEffect extends CustomStatusEffect {

    public ManaBoostStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity instanceof PlayerEntity player) {
            ArchonUtil.get(player).clampMana();
        }
    }
}
