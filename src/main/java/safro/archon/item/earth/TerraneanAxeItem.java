package safro.archon.item.earth;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import safro.archon.item.ManaWeapon;
import safro.archon.registry.EffectRegistry;

public class TerraneanAxeItem extends ManaWeapon {

    public TerraneanAxeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public int getManaCost() {
        return 40;
    }

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 2, true, false));
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STURDY, 400, 0, true, false));
        player.swingHand(hand);
        return true;
    }
}
