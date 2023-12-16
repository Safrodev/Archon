package safro.archon.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.block.entity.SummoningPedestalBlockEntity;
import safro.archon.entity.boss.*;
import safro.archon.registry.EntityRegistry;
import safro.archon.registry.ItemRegistry;

public class SummonUtil {
    public static boolean canSummonTar(SummoningPedestalBlockEntity be) {
        return be.hasItem(ItemRegistry.EARTH_ESSENCE) && be.hasItem(ItemRegistry.EARTH_GEM) && be.hasItem(Items.DEEPSLATE) && be.hasItem(Items.EMERALD);
    }

    public static void summonTar(World world, BlockPos pos) {
        TarEntity tar = EntityRegistry.TAR.create(world);
        tar.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        tar.onSummoned();
        world.spawnEntity(tar);
    }

    public static boolean canSummonAlya(SummoningPedestalBlockEntity be) {
        return be.hasItem(Items.GOLD_INGOT) && be.hasItem(ItemRegistry.SKY_GEM) && be.hasItem(Items.FEATHER) && be.hasItem(ItemRegistry.SKY_ESSENCE);
    }

    public static void summonAlya(World world, BlockPos pos) {
        AlyaEntity alya = EntityRegistry.ALYA.create(world);
        alya.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        alya.onSummoned();
        world.spawnEntity(alya);
    }

    public static boolean canSummonLeven(SummoningPedestalBlockEntity be) {
        return be.hasItem(Items.KELP) && be.hasItem(ItemRegistry.WATER_GEM) && be.hasItem(Items.PUFFERFISH) && be.hasItem(ItemRegistry.WATER_ESSENCE);
    }

    public static void summonLeven(World world, BlockPos pos) {
        LevenEntity leven = EntityRegistry.LEVEN.create(world);
        leven.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        leven.onSummoned();
        world.spawnEntity(leven);
    }

    public static boolean canSummonInigo(SummoningPedestalBlockEntity be) {
        return be.hasItem(Items.BLAZE_POWDER) && be.hasItem(ItemRegistry.FIRE_GEM) && be.hasItem(Items.MAGMA_CREAM) && be.hasItem(ItemRegistry.FIRE_ESSENCE);
    }

    public static void summonInigo(World world, BlockPos pos) {
        InigoEntity inigo = EntityRegistry.INIGO.create(world);
        inigo.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        inigo.onSummoned();
        world.spawnEntity(inigo);
    }

    public static boolean canSummonNull(SummoningPedestalBlockEntity be) {
        return be.hasItem(Items.ENDER_PEARL) && be.hasItem(ItemRegistry.END_GEM) && be.hasItem(Items.END_CRYSTAL) && be.hasItem(ItemRegistry.END_ESSENCE);
    }

    public static void summonNull(World world, BlockPos pos) {
        NullEntity entity = EntityRegistry.NULL.create(world);
        entity.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        entity.onSummoned();
        world.spawnEntity(entity);
    }

    public static void addLightning(World world, BlockPos pos) {
        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos.up()));
        ((LightningAccess) lightningEntity).setFireSpawning(false);
        lightningEntity.setCosmetic(true);
        world.spawnEntity(lightningEntity);
    }
}
