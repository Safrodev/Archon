package safro.archon.compat.emi;

import dev.emi.emi.api.EmiRecipeHandler;
import dev.emi.emi.api.recipe.EmiRecipe;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import safro.archon.client.screen.ScriptureTableScreenHandler;

import java.util.LinkedList;
import java.util.List;

public class ScriptingEmiHandler implements EmiRecipeHandler<ScriptureTableScreenHandler> {
    @Override
    public List<Slot> getInputSources(ScriptureTableScreenHandler handler) {
        List<Slot> list = new LinkedList<>();
        list.add(handler.slots.get(0));
        list.add(handler.slots.get(1));
        list.add(handler.slots.get(2));
        list.add(handler.slots.get(3));
        for (int i = 6; i < handler.slots.size();i++){
             list.add(handler.slots.get(i));
        }
        return list;
    }

    @Override
    public List<Slot> getCraftingSlots(ScriptureTableScreenHandler handler) {
        List<Slot> list = new LinkedList<>();
        list.add(handler.slots.get(0));
        list.add(handler.slots.get(1));
        list.add(handler.slots.get(2));
        list.add(handler.slots.get(3));
        return list;
    }

    @Override
    public @Nullable Slot getOutputSlot(ScriptureTableScreenHandler handler) {
        return handler.slots.get(5);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == ArchonEmiPlugin.SCRIPTING_CATEGORY && recipe.supportsRecipeTree();
    }
}
