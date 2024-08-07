package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.util.SpellUtil;

public abstract class RaycastSpell extends Spell {
    private final double range;

    public RaycastSpell(Element type, int manaCost, double range) {
        super(type, manaCost);
        this.range = range;
    }

    public abstract void onRaycast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack, LivingEntity target);

    public boolean canTarget(PlayerEntity caster, LivingEntity target) {
        return true;
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        if (SpellUtil.getTargeted(player, this.range) instanceof LivingEntity target) {
            this.onRaycast(world, player, power, stack, target);
        }
    }

    @Override
    public boolean canCast(World world, PlayerEntity player, ItemStack stack) {
        if (super.canCast(world, player, stack) && SpellUtil.getTargeted(player, this.range) instanceof LivingEntity target) {
            return this.canTarget(player, target);
        }
        return false;
    }
}
