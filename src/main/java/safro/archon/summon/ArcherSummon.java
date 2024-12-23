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
import net.minecraft.util.math.MathHelper;
import safro.archon.api.summon.Summon;
import safro.archon.api.summon.SummonedMob;
import safro.archon.util.SummonHelper;

import java.util.HashMap;
import java.util.Map;

public class ArcherSummon implements Summon {

    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        WitherSkeletonEntity entity = EntityType.WITHER_SKELETON.create(world);
        entity.setTarget(player.getAttacking());
        ((SummonedMob)entity).archon$setOwner(player.getUuidAsString());

        ItemStack bow = new ItemStack(Items.BOW);
        Map<Enchantment, Integer> enchants = new HashMap<>();
        enchants.put(Enchantments.POWER, Math.min(10, soulPower + 1));
        enchants.put(Enchantments.FLAME, 1);
        EnchantmentHelper.set(enchants, bow);
        entity.equipStack(EquipmentSlot.MAINHAND, bow);

        double x = player.getX() + MathHelper.nextDouble(player.getRandom(), -3.0, 3.0);
        double z = player.getZ() + MathHelper.nextDouble(player.getRandom(), -3.0, 3.0);
        entity.refreshPositionAndAngles(x, player.getY(), z, player.getYaw(), player.getPitch());
        SummonHelper.setScaledLife(entity, soulPower, 15);
        SummonHelper.addStatScaling(entity, soulPower);
        world.spawnEntity(entity);
        SummonHelper.createParticlesAround(entity, world);
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.archer";
    }
}
