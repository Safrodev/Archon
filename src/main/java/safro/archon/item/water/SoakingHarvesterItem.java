package safro.archon.item.water;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Pair;
import safro.archon.item.HarvesterItem;

public class SoakingHarvesterItem extends HarvesterItem {

    public SoakingHarvesterItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup1() {
        return new Pair<>(EntityType.SQUID, Items.ROTTEN_FLESH);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup2() {
        return new Pair<>(EntityType.DROWNED, Items.COPPER_INGOT);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup3() {
        return new Pair<>(EntityType.GUARDIAN, Items.PRISMARINE_SHARD);
    }
}
