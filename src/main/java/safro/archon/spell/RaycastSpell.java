package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.util.SpellUtil;

public abstract class RaycastSpell extends Spell {
    private final double range;

    public RaycastSpell(Element type, int manaCost, double range) {
        super(type, manaCost);
        this.range = range;
    }

    public abstract void onRaycast(World world, PlayerEntity player, ItemStack stack, LivingEntity target);

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        if (SpellUtil.getTargeted(player, this.range) instanceof LivingEntity target) {
            this.onRaycast(world, player, stack, target);
        }
    }

    @Override
    public boolean canCast(World world, PlayerEntity player, ItemStack stack) {
        if (super.canCast(world, player, stack)) {
            return SpellUtil.getTargeted(player, this.range) instanceof LivingEntity;
        }
        return false;
    }
}
