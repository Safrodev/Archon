package safro.archon.summon;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import safro.archon.api.summon.Summon;
import safro.archon.util.SummonHelper;

import java.util.HashMap;
import java.util.Map;

public class ArcherSummon implements Summon {

    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        WitherSkeletonEntity entity = EntityType.WITHER_SKELETON.create(world);

        if (entity != null) {
            ItemStack bow = new ItemStack(Items.BOW);
            Map<Enchantment, Integer> enchants = new HashMap<>();
            enchants.put(Enchantments.POWER, Math.min(30, soulPower + 1));
            enchants.put(Enchantments.FLAME, 1);
            EnchantmentHelper.set(enchants, bow);
            entity.equipStack(EquipmentSlot.MAINHAND, bow);
            entity.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0F);

            SummonHelper.spawnAndScale(world, player, entity, soulPower, 15);
        }
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.archer";
    }
}
