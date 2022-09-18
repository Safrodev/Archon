package safro.archon.spell;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        SpellUtil.create(world, player, Items.SNOW_BLOCK, (target, owner, projectile) -> {
          target.setFrozenTicks(200);
          target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1, false, false, false));
        });
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_PLAYER_HURT_FREEZE;
    }
}
