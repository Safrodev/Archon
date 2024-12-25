package safro.archon.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.jetbrains.annotations.Nullable;
import safro.archon.registry.MiscRegistry;

public class WizardReapingBookFactory implements TradeOffers.Factory {
    private final int experience;

    public WizardReapingBookFactory(int exp) {
        this.experience = exp;
    }

    @Nullable
    @Override
    public TradeOffer create(Entity entity, Random random) {
        Enchantment enchantment = MiscRegistry.REAPING;
        ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, 1));
        int j = 10 + random.nextInt(15);
        j *= 2;

        return new TradeOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 6, this.experience, 0.2F);
    }
}
