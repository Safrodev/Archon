package safro.archon.item.earth;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import safro.archon.item.ManaWeapon;
import safro.archon.registry.EffectRegistry;

public class FistOfFuryItem extends ManaWeapon {

    public FistOfFuryItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.RAGE, 100, 0));
        return true;
    }

    public int getManaCost() {
        return 40;
    }
}
