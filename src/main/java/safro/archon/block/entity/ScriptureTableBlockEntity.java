package safro.archon.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.client.screen.ScriptureTableScreenHandler;
import safro.archon.item.TomeItem;
import safro.archon.recipe.ScriptingRecipe;
import safro.archon.registry.BlockRegistry;
import safro.archon.registry.RecipeRegistry;
import safro.archon.registry.TagRegistry;

import java.util.Iterator;
import java.util.List;

public class ScriptureTableBlockEntity extends LockableContainerBlockEntity implements SidedInventory {
    // REFERENCE: Slot 0-2 - Ingredient Slots, Slot 3 - Book Slot, Slot 4 - Fuel Slot, Slot 5 - Output Slot
    private static final int[] TOP_SLOTS = new int[]{3, 5};
    private static final int[] BOTTOM_SLOTS = new int[]{0, 1, 2, 3};
    private static final int[] SIDE_SLOTS = new int[]{0, 1, 2, 4};
    private DefaultedList<ItemStack> inventory;
    protected final PropertyDelegate propertyDelegate;
    private int fuel;

    public ScriptureTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.SCRIPTURE_TABLE_BE, pos, state);
        this.inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return ScriptureTableBlockEntity.this.fuel;
            }

            public void set(int index, int value) {
                ScriptureTableBlockEntity.this.fuel = value;
            }

            public int size() {
                return 1;
            }
        };
    }

    protected Text getContainerName() {
        return new TranslatableText("container.archon.scripture");
    }

    public int size() {
        return this.inventory.size();
    }

    public boolean isEmpty() {
        Iterator var1 = this.inventory.iterator();
        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(itemStack.isEmpty());
        return false;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ScriptureTableBlockEntity be) {
        ItemStack itemStack = be.inventory.get(4);
        if (be.fuel <= 0 && itemStack.isIn(TagRegistry.LAPIS_LAZULIS)) {
            be.fuel = 20;
            itemStack.decrement(1);
            markDirty(world, pos, state);
        }

        if (canCraft(be.inventory) && be.fuel > 0) {
            ScriptingRecipe recipe = world.getRecipeManager().listAllOfType(RecipeRegistry.SCRIPTING).stream().filter(r -> {
                List<Ingredient> list = r.getInputs();
                return be.hasStack(list.get(0)) && be.hasStack(list.get(1)) && be.hasStack(list.get(2));
            }).findFirst().orElse(null);

            if (recipe != null) {
                be.inventory.set(5, recipe.getOutput().copy());
            }
        }
    }

    public static boolean canCraft(DefaultedList<ItemStack> slots) {
        if (!slots.get(3).isEmpty() && slots.get(5).isEmpty()) {
            for (int i = 0; i < 3; ++i) {
                ItemStack stack = slots.get(i);
                if (!stack.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void craftTome(World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof ScriptureTableBlockEntity be) {
            for (int i = 0; i < 4; i++) {
                be.inventory.get(i).decrement(1);
            }
            be.fuel -= 10;
            world.playSound(null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            markDirty(world, pos, world.getBlockState(pos));
        }
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.fuel = nbt.getByte("Fuel");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putByte("Fuel", (byte)this.fuel);
    }

    public ItemStack getStack(int slot) {
        return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
    }

    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    public void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
        }

    }

    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 5) {
            return false;
        } else if (slot == 3) {
            return stack.isIn(TagRegistry.BOOKS);
        } else if (slot == 4) {
            return stack.isIn(TagRegistry.LAPIS_LAZULIS);
        }
        return true;
    }

    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.UP) {
            return TOP_SLOTS;
        } else {
            return side == Direction.DOWN ? BOTTOM_SLOTS : SIDE_SLOTS;
        }
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot != 5 || stack.getItem() instanceof TomeItem;
    }

    public void clear() {
        this.inventory.clear();
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ScriptureTableScreenHandler(syncId, playerInventory, this, this.propertyDelegate, ScreenHandlerContext.create(world, pos));
    }

    public boolean hasStack(Ingredient ingredient) {
        for (ItemStack i : this.inventory) {
            if (ingredient.test(i)) {
                return true;
            }
        }
        return false;
    }
}
