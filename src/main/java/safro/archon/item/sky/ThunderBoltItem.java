package safro.archon.item.sky;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.item.ManaWeapon;
import safro.archon.util.LightningAccess;

public class ThunderBoltItem extends ManaWeapon {

    public ThunderBoltItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public int getManaCost() {
        return 30;
    }

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        BlockPos north = player.getBlockPos().north();
        BlockPos south = player.getBlockPos().south();
        BlockPos east = player.getBlockPos().east();
        BlockPos west = player.getBlockPos().west();

        LightningEntity lightningEntity = (LightningEntity) EntityType.LIGHTNING_BOLT.create(world);
        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(north));
        ((LightningAccess)lightningEntity).setFireSpawning(false);
        world.spawnEntity(lightningEntity);

        LightningEntity southE = (LightningEntity) EntityType.LIGHTNING_BOLT.create(world);
        ((LightningAccess)southE).setFireSpawning(false);
        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(south));
        world.spawnEntity(southE);

        LightningEntity eastE = (LightningEntity) EntityType.LIGHTNING_BOLT.create(world);
        ((LightningAccess)eastE).setFireSpawning(false);
        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(east));
        world.spawnEntity(eastE);

        LightningEntity westE = (LightningEntity) EntityType.LIGHTNING_BOLT.create(world);
        ((LightningAccess)westE).setFireSpawning(false);
        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(west));
        world.spawnEntity(westE);
        return true;
    }
}
