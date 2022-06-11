package safro.archon.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import safro.archon.block.entity.ScriptureTableBlockEntity;
import safro.archon.registry.MiscRegistry;
import safro.archon.registry.TagRegistry;

public class ScriptureTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final Slot ingredientSlot;
    private final PropertyDelegate propertyDelegate;
    private final ScreenHandlerContext context;

    public ScriptureTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(6), new ArrayPropertyDelegate(1), ScreenHandlerContext.EMPTY);
    }

    public ScriptureTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate, ScreenHandlerContext ctx) {
        super(MiscRegistry.SCRIPTURE_TABLE_SH, syncId);
        checkSize(inventory, 6);
        this.inventory = inventory;
        this.propertyDelegate = delegate;
        this.context = ctx;

        this.ingredientSlot = this.addSlot(new ScriptureTableScreenHandler.IngredientSlot(inventory, 0, 44, 58));
        this.addSlot(new ScriptureTableScreenHandler.IngredientSlot(inventory, 1, 79, 58));
        this.addSlot(new ScriptureTableScreenHandler.IngredientSlot(inventory, 2, 116, 58));
        this.addSlot(new ScriptureTableScreenHandler.BookSlot(inventory, 3, 140, 17));
        this.addSlot(new ScriptureTableScreenHandler.FuelSlot(inventory, 4, 17, 17));
        this.addSlot(new ScriptureTableScreenHandler.OutputSlot(inventory, 5, 79, 17));
        this.addProperties(delegate);

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public void onTakeOutput() {
        this.context.run(ScriptureTableBlockEntity::craftTome);
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if ((index < 0 || index > 2) && index != 3 && index != 4) {
                if (ScriptureTableScreenHandler.FuelSlot.matches(itemStack)) {
                    if (this.insertItem(itemStack2, 4, 5, false) || this.ingredientSlot.canInsert(itemStack2) && !this.insertItem(itemStack2, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (ScriptureTableScreenHandler.BookSlot.matches(itemStack)) {
                    if (!this.insertItem(itemStack2, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.ingredientSlot.canInsert(itemStack2)) {
                    if (!this.insertItem(itemStack2, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index > 5 && index < 32) {
                    if (!this.insertItem(itemStack2, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 32 && index < 41) {
                    if (!this.insertItem(itemStack2, 6, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(itemStack2, 6, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStack2, 6, 41, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    public int getFuel() {
        return this.propertyDelegate.get(0);
    }

    class OutputSlot extends Slot {
        public OutputSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return false;
        }

        public int getMaxItemCount() {
            return 1;
        }

        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            ScriptureTableScreenHandler.this.onTakeOutput();
            super.onTakeItem(player, stack);
        }
    }

    private static class IngredientSlot extends Slot {
        public IngredientSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return true;
        }

        public int getMaxItemCount() {
            return 64;
        }
    }

    private static class FuelSlot extends Slot {
        public FuelSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return matches(stack);
        }

        public static boolean matches(ItemStack stack) {
            return stack.isIn(TagRegistry.LAPIS_LAZULIS);
        }

        public int getMaxItemCount() {
            return 64;
        }
    }

    private static class BookSlot extends Slot {
        public BookSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        public boolean canInsert(ItemStack stack) {
            return matches(stack);
        }

        public static boolean matches(ItemStack stack) {
            return stack.isIn(TagRegistry.BOOKS);
        }

        public int getMaxItemCount() {
            return 64;
        }
    }
}
