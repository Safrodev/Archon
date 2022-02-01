package safro.archon.item.earth;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Pair;
import safro.archon.item.HarvesterItem;

public class RockyHarvesterItem extends HarvesterItem {

    public RockyHarvesterItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup1() {
        return new Pair<>(EntityType.ZOMBIE, Items.ROTTEN_FLESH);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup2() {
        return new Pair<>(EntityType.SKELETON, Items.BONE);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup3() {
        return new Pair<>(EntityType.SLIME, Items.SLIME_BALL);
    }
}
