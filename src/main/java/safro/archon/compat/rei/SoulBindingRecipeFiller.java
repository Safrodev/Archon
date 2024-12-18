package safro.archon.compat.rei;

import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.minecraft.item.ItemStack;
import safro.archon.Archon;
import safro.archon.item.SoulTomeItem;
import safro.archon.item.UndeadStaffItem;
import safro.archon.recipe.SoulBindingRecipe;
import safro.saflib.SafLib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SoulBindingRecipeFiller implements CraftingRecipeFiller<SoulBindingRecipe> {

    @Override
    public Collection<Display> apply(SoulBindingRecipe soulBindingRecipe) {
        List<Display> displays = new ArrayList<>();
        List<EntryStack<?>> stacks = EntryRegistry.getInstance().getEntryStacks().filter(entry -> entry.getValueType() == ItemStack.class && entry.<ItemStack>castValue().getItem() instanceof UndeadStaffItem).toList();

        for (EntryStack<?> entry : stacks) {
            ItemStack staff = entry.castValue();
            for (ItemStack tome : SafLib.getItemsFor(Archon.MODID).stream().filter(s -> s.getItem() instanceof SoulTomeItem).toList()) {
                ItemStack output = staff.copy();
                displays.add(new DefaultCustomShapelessDisplay(soulBindingRecipe,
                        List.of(EntryIngredient.of(entry.copy()), EntryIngredients.of(tome.copy())),
                        List.of(EntryIngredients.of(output))
                ));
            }
        }
        return displays;
    }

    @Override
    public Class<SoulBindingRecipe> getRecipeClass() {
        return SoulBindingRecipe.class;
    }
}
