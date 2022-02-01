package safro.archon.item.fire;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Pair;
import safro.archon.item.HarvesterItem;

public class FlamingHarvesterItem extends HarvesterItem {

    public FlamingHarvesterItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup1() {
        return new Pair<>(EntityType.BLAZE, Items.BLAZE_ROD);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup2() {
        return new Pair<>(EntityType.MAGMA_CUBE, Items.MAGMA_CREAM);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup3() {
        return new Pair<>(EntityType.ZOMBIFIED_PIGLIN, Items.GOLD_INGOT);
    }
}
