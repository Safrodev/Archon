package safro.archon.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.archon.registry.BlockRegistry;
import safro.archon.util.SummonUtil;

public class SummoningPedestalBlockEntity extends BlockEntity implements Clearable {
    private final DefaultedList<ItemStack> inventory;
    // 0 - Not processing anything
    // 1 - Summoning Tar boss
    // 2 - Summoning Alya boss
    // 3 - Summoning Leven boss
    // 4 - Summoning Inigo boss
    // 5 - Summoning Null boss
    private int processor = 0;
    private int spawnDelay = 60;

    public SummoningPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.SUMMONING_PEDESTAL_BE, pos, state);
        this.inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getMainHandStack();
        if (this.isIdle() && !world.isClient) {
            if (player.isSneaking()) {
                ItemScatterer.spawn(world, pos, inventory);
                world.updateListeners(pos, state, state, 3);
                markDirty(world, pos, state);
                return ActionResult.SUCCESS;
            } else if (!stack.isEmpty() && this.addItem(player.getAbilities().creativeMode ? stack.copy() : stack)) {
                return ActionResult.SUCCESS;
            } else {
                if (SummonUtil.canSummonTar(this)) {
                    checkAndQueue(player, world, state, pos, 1);
                } else if (SummonUtil.canSummonAlya(this)) {
                    checkAndQueue(player, world, state, pos, 2);
                } else if (SummonUtil.canSummonLeven(this)) {
                    checkAndQueue(player, world, state, pos, 3);
                } else if (SummonUtil.canSummonInigo(this)) {
                    checkAndQueue(player, world, state, pos, 4);
                } else if (SummonUtil.canSummonNull(this)) {
                    checkAndQueue(player, world, state, pos, 5);
                }
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    public void checkAndQueue(PlayerEntity player, World world, BlockState state, BlockPos pos, int processor) {
        if (world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.up().up()).isAir()) {
            this.setProcessor(processor);
            this.clear();
            world.updateListeners(pos, state, state, 3);
            markDirty(world, pos, state);
        } else {
            player.sendMessage(Text.translatable("text.archon.invalid_summon").formatted(Formatting.RED), true);
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, SummoningPedestalBlockEntity be) {
        if (be.isProcessing()) {
            --be.spawnDelay;
            if (world.random.nextFloat() <= 0.10) {
                SummonUtil.addLightning(world, pos);
            }
            if (be.spawnDelay <= 0) {
                if (be.getProcessor() == 1) {
                    SummonUtil.summonTar(world, pos.up());
                    be.setProcessor(0);
                } else if (be.getProcessor() == 2) {
                    SummonUtil.summonAlya(world, pos.up());
                    be.setProcessor(0);
                } else if (be.getProcessor() == 3) {
                    SummonUtil.summonLeven(world, pos.up());
                    be.setProcessor(0);
                } else if (be.getProcessor() == 4) {
                    SummonUtil.summonInigo(world, pos.up());
                    be.setProcessor(0);
                } else if (be.getProcessor() == 5) {
                    SummonUtil.summonNull(world, pos.up());
                    be.setProcessor(0);
                }
            }
        }
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory);
        if (nbt.contains("processor")) {
            processor = nbt.getInt("processor");
        }
    }

    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory, true);
        nbt.putInt("processor", processor);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.inventory, true);
        return nbtCompound;
    }

    public boolean addItem(ItemStack item) {
        if (!this.hasItem(item.getItem())) {
            for (int i = 0; i < this.inventory.size(); ++i) {
                ItemStack itemStack = this.inventory.get(i);
                if (itemStack.isEmpty()) {
                    this.inventory.set(i, item.split(1));
                    this.updateListeners();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasItem(Item item) {
        for (ItemStack itemStack : this.inventory) {
            if (item == itemStack.getItem()) {
                return true;
            }
        }
        return false;
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
    }

    public boolean isProcessing() {
        return processor > 0;
    }

    public boolean isIdle() {
        return processor == 0;
    }

    public int getProcessor() {
        return processor;
    }

    public void setProcessor(int type) {
        this.processor = type;
    }

    public void clear() {
        this.inventory.clear();
    }
}
