package safro.archon.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.block.entity.SummoningPedestalBlockEntity;
import safro.archon.entity.boss.TarEntity;
import safro.archon.registry.EntityRegistry;
import safro.archon.registry.ItemRegistry;

public class SummonUtil {
    public static boolean canSummonTar(SummoningPedestalBlockEntity be) {
        return be.hasItem(Items.SANDSTONE) && be.hasItem(ItemRegistry.EARTH_GEM) && be.hasItem(Items.DEEPSLATE) && be.hasItem(Items.EMERALD);
    }

    public static void summonTar(World world, BlockPos pos) {
        TarEntity tar = EntityRegistry.TAR.create(world);
        tar.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        tar.onSummoned();
        world.spawnEntity(tar);
    }

    public static void addLightning(World world, BlockPos pos) {
        LightningEntity lightningEntity = (LightningEntity) EntityType.LIGHTNING_BOLT.create(world);
        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos.up()));
        ((LightningAccess)lightningEntity).setFireSpawning(false);
        lightningEntity.setCosmetic(true);
        world.spawnEntity(lightningEntity);
    }
}
