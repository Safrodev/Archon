package safro.archon.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import safro.archon.registry.MiscRegistry;

import java.util.Random;

public class WizardEnchantBookFactory implements TradeOffers.Factory {
    private final int experience;

    public WizardEnchantBookFactory(int exp) {
        this.experience = exp;
    }

    public TradeOffer create(Entity entity, Random random) {
        Enchantment enchantment = MiscRegistry.ARCANE;
        int i = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i));
        int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
        if (enchantment.isTreasure()) {
            j *= 2;
        }
        if (j > 64) {
            j = 64;
        }
        return new TradeOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F);
    }
}
