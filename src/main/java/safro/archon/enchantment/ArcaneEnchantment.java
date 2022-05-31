package safro.archon.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import safro.archon.item.ManaWeapon;
import safro.archon.registry.MiscRegistry;
import safro.archon.util.ArchonUtil;

public class ArcaneEnchantment extends Enchantment {

    public ArcaneEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    public int getMinPower(int level) {
        return level * 25;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 50;
    }

    public boolean isTreasure() {
        return true;
    }

    public int getMaxLevel() {
        return 1;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ManaWeapon;
    }

    /**
     * Checks for the arcane enchantment and removes mana accordingly. If arcane is not present, the normal mana amount will be removed.
     * @param player Player using the item
     * @param stack Stack of the item
     * @param manaCost Mana to be removed
     */
    public static void applyArcane(PlayerEntity player, ItemStack stack, int manaCost) {
        if (EnchantmentHelper.getLevel(MiscRegistry.ARCANE, stack) >= 1) {
            int removed = (int) (manaCost * 0.2);
            ArchonUtil.get(player).removeMana(manaCost - removed);
        } else {
            ArchonUtil.get(player).removeMana(manaCost);
        }
    }
}
