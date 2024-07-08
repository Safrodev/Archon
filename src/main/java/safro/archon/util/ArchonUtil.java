package safro.archon.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.api.ManaComponent;
import safro.archon.api.spell.Spell;
import safro.archon.item.*;
import safro.archon.item.fire.HeatRangerItem;
import safro.archon.registry.ComponentsRegistry;
import safro.archon.registry.ItemRegistry;

import java.util.*;

public class ArchonUtil {
    public static ManaComponent get(PlayerEntity player) {
        return ComponentsRegistry.MANA_COMPONENT.get(player);
    }

    public static boolean canRemoveMana(PlayerEntity player, int amount) {
        return ComponentsRegistry.MANA_COMPONENT.get(player).canUseMana(amount);
    }

    public static boolean hasScroll(PlayerEntity player) {
        return ComponentsRegistry.SCROLL_COMPONENT.get(player).hasScroll();
    }

    public static void setScroll(PlayerEntity player, String name) {
        ComponentsRegistry.SCROLL_COMPONENT.get(player).setScroll(name);
    }

    public static String getScroll(PlayerEntity player) {
        return ComponentsRegistry.SCROLL_COMPONENT.get(player).getScroll();
    }

    public static List<Spell> getSpells(PlayerEntity player) {
        return ComponentsRegistry.SPELL_COMPONENT.get(player).getSpells();
    }

    public static boolean addSpell(PlayerEntity player, Spell spell) {
        return ComponentsRegistry.SPELL_COMPONENT.get(player).addSpell(spell);
    }

    public static boolean isValidManaItem(ItemStack stack) {
        return stack.getItem() instanceof SpellWeaponItem || stack.getItem() instanceof ManaItem ||
                stack.getItem() instanceof ChannelerItem || stack.getItem() instanceof HeatRangerItem ||
                stack.getItem() instanceof ManaBerriesItem || stack.isOf(ItemRegistry.SOUL_CRUSHER) || stack.getItem() instanceof WandItem;
    }

    public static Text createManaText(int amt, boolean blue) {
        return Text.translatable("text.archon.mana_cost", amt).formatted(blue ? Formatting.BLUE : Formatting.GRAY);
    }

    public static void dropItem(World world, BlockPos pos, Item item) {
        double d = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
        double e = (double) (world.random.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
        double g = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
        ItemStack soulStack = new ItemStack(item);
        ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + g, soulStack);
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
    }

    public static void growBlock(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof Fertilizable fertilizable) {
            if (fertilizable.isFertilizable(world, pos, blockState, world.isClient)) {
                if (world instanceof ServerWorld) {
                    if (fertilizable.canGrow(world, world.random, pos, blockState)) {
                        fertilizable.grow((ServerWorld)world, world.random, pos, blockState);
                    }
                }
            }
        }
    }

    public static boolean isOwnedBy(PlayerEntity player, LivingEntity entity) {
        if (entity instanceof TameableEntity tame) {
            if (tame.getOwner() != null) {
                return tame.isOwner(player);
            }
        }
        return false;
    }

    public static void createLightning(World world, BlockPos pos, boolean spawnFire) {
        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos.up()));
        if (!spawnFire) {
            ((LightningAccess) lightningEntity).setFireSpawning(false);
        }
        world.spawnEntity(lightningEntity);
    }

    // Credit to Tech Reborn for this
    public static Set<BlockPos> getPosArea(World worldIn, BlockPos pos, LivingEntity entityLiving, int radius, boolean placeDummyBlocks) {
        if (!(entityLiving instanceof PlayerEntity playerIn)) {
            return ImmutableSet.of();
        }
        Set<BlockPos> targetBlocks = new HashSet<>();

        if (placeDummyBlocks) {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
        }

        HitResult hitResult = playerIn.raycast(20D, 0F, false);

        if (placeDummyBlocks) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }

        if (!(hitResult instanceof BlockHitResult)) {
            return Collections.emptySet();
        }
        Direction direction = ((BlockHitResult) hitResult).getSide();

        if (direction == Direction.SOUTH || direction == Direction.NORTH) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -1; y <= 1 + (radius - 1) * 2; y++) {
                    targetBlocks.add(pos.add(x, y, 0));
                }
            }
        } else if (direction == Direction.EAST || direction == Direction.WEST) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -1; y <= 1 + (radius - 1) * 2; y++) {
                    targetBlocks.add(pos.add(0, y, z));
                }
            }
        } else if (direction == Direction.DOWN || direction == Direction.UP) {
            Direction playerDirection = playerIn.getHorizontalFacing();
            int minX = 0;
            int maxX = 0;
            int minZ = 0;
            int maxZ = 0;

            switch (playerDirection) {
                case SOUTH -> {
                    minZ = -1;
                    maxZ = 1 + (radius - 1) * 2;
                    minX = -radius;
                    maxX = radius;
                }
                case NORTH -> {
                    minZ = -1 - (radius - 1) * 2;
                    maxZ = 1;
                    minX = -radius;
                    maxX = radius;
                }
                case WEST -> {
                    minZ = -radius;
                    maxZ = radius;
                    minX = -1 - (radius - 1) * 2;
                    maxX = 1;
                }
                case EAST -> {
                    minZ = -radius;
                    maxZ = radius;
                    minX = -1;
                    maxX = 1 + (radius - 1) * 2;
                }
            }
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    targetBlocks.add(pos.add(x, 0, z));
                }
            }
        }
        return targetBlocks;
    }
}
