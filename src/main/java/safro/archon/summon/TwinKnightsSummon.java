package safro.archon.summon;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import safro.archon.api.summon.Summon;
import safro.archon.util.SummonHelper;

public class TwinKnightsSummon implements Summon {
    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        LocalDifficulty localDifficulty = world.getLocalDifficulty(player.getBlockPos());
        SkeletonEntity first = EntityType.SKELETON.create(world);
        giveFullEquipment(first, localDifficulty);
        SkeletonEntity second = EntityType.SKELETON.create(world);
        giveFullEquipment(second, localDifficulty);

        SummonHelper.spawnAndScale(world, player, first, soulPower, 20);
        SummonHelper.spawnAndScale(world, player, second, soulPower, 20);
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.twin_knights";
    }

    private static void giveFullEquipment(MobEntity entity, LocalDifficulty localDifficulty) {
        entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));

        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        entity.equipStack(EquipmentSlot.MAINHAND, EnchantmentHelper.enchant(entity.getRandom(), sword, (int)(5.0F + localDifficulty.getClampedLocalDifficulty() * (float)entity.getRandom().nextInt(18)), false));
        entity.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            entity.setEquipmentDropChance(slot, 0.0F);
        }
    }
}
