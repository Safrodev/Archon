package safro.archon.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import safro.archon.registry.ItemRegistry;
import safro.archon.util.LightningAccess;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    public ItemEntityMixin(EntityType<? extends ItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (!((LightningAccess)lightning).isCosmetic()) {
            if (entity.getStack().isOf(Items.GLASS_BOTTLE)) {
                ItemEntity bottle = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ItemRegistry.LIGHTNING_BOTTLE));
                bottle.setToDefaultPickupDelay();
                world.spawnEntity(bottle);
                entity.getStack().decrement(1);
            }
        }
        super.onStruckByLightning(world, lightning);
    }
}
