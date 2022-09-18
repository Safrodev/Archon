package safro.archon.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import safro.archon.item.ExperiencePouchItem;
import safro.archon.registry.MiscRegistry;

public class ExperiencePouchScreenHandler extends ScreenHandler {
    private final ItemStack stack;

    public ExperiencePouchScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ItemStack.EMPTY);
    }

    public ExperiencePouchScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack stack) {
        super(MiscRegistry.EXPERIENCE_POUCH_SH, syncId);
        this.stack = stack;

        int k;
        for(k = 0; k < 3; ++k) {
            for(int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + k * 9 + 9, 36 + l * 18, 109 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 36 + k * 18, 167));
        }
    }

    public int getExperience() {
        return ExperiencePouchItem.getExperience(this.stack);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
