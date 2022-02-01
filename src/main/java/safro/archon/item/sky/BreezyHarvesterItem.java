package safro.archon.item.sky;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Pair;
import safro.archon.item.HarvesterItem;

public class BreezyHarvesterItem extends HarvesterItem {

    public BreezyHarvesterItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup1() {
        return new Pair<>(EntityType.GHAST, Items.GHAST_TEAR);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup2() {
        return new Pair<>(EntityType.PHANTOM, Items.PHANTOM_MEMBRANE);
    }

    @Override
    public Pair<EntityType<? extends Entity>, Item> getGroup3() {
        return new Pair<>(EntityType.VEX, Items.EMERALD);
    }
}
