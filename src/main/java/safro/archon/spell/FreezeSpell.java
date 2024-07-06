package safro.archon.spell;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.util.SpellUtil;

public class FreezeSpell extends Spell {

    public FreezeSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        SpellUtil.shoot(world, player, (target, owner, projectile) -> {
          target.setFrozenTicks(200);
          target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 4, true, false, false));
        }, 0.9F);
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_PLAYER_HURT_FREEZE;
    }
}
