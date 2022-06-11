package safro.archon.spell;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.util.ProjectileHelper;

public class FreezeSpell extends Spell {

    public FreezeSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        ProjectileHelper.create(world, player, ParticleTypes.SNOWFLAKE, Items.SNOW_BLOCK, (target, owner) -> {
          target.setFrozenTicks(200);
          target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 1, false, false, false));
        });
    }
}
