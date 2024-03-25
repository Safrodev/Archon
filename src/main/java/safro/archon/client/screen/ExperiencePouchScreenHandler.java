package safro.archon.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import safro.archon.registry.ItemRegistry;
import safro.archon.registry.MiscRegistry;

public class ExperiencePouchScreenHandler extends ScreenHandler {
    private final ItemStack stack;
    private final PropertyDelegate propertyDelegate;

    public ExperiencePouchScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new ArrayPropertyDelegate(1));
    }

    public ExperiencePouchScreenHandler(int syncId, PlayerInventory playerInventory, PropertyDelegate propertyDelegate) {
        super(MiscRegistry.EXPERIENCE_POUCH_SH, syncId);
        this.stack = playerInventory.getMainHandStack();
        this.propertyDelegate = propertyDelegate;

        int k;
        for (k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + k * 9 + 9, 36 + l * 18, 109 + k * 18));
            }
        }

        for (k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 36 + k * 18, 167));
        }

        this.addProperties(propertyDelegate);
    }

    public int getExperience() {
        return this.propertyDelegate.get(0);
    }

    public int getMaxExperience() {
        return this.stack.isOf(ItemRegistry.SUPER_EXPERIENCE_POUCH) ? 2920 : 550;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
