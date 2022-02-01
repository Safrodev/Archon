package safro.archon.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.archon.Archon;

public abstract class HarvesterItem extends SwordItem {

    public HarvesterItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public abstract Pair<EntityType<? extends Entity>, Item> getGroup1();
    public abstract Pair<EntityType<? extends Entity>, Item> getGroup2();
    public abstract Pair<EntityType<? extends Entity>, Item> getGroup3();

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        EntityType<? extends Entity> entity1 = getGroup1().getLeft();
        EntityType<? extends Entity> entity2 = getGroup2().getLeft();
        EntityType<? extends Entity> entity3 = getGroup3().getLeft();

        if (target.isDead()) {
            if (target.getType() == entity1) {
                dropWithChance(target, getGroup1().getRight());
            } else if (target.getType() == entity2) {
                dropWithChance(target, getGroup2().getRight());
            } else if (target.getType() == entity3) {
                dropWithChance(target, getGroup3().getRight());
            }
        }
        return super.postHit(stack, target, attacker);
    }

    private void dropWithChance(LivingEntity target, Item item) {
        World world = target.world;
        BlockPos pos = target.getBlockPos();

        if (target.getRandom().nextFloat() <= Archon.CONFIG.harvester_chance) {
            ItemStack dropStack = new ItemStack(item);
            ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), dropStack);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
    }
}
