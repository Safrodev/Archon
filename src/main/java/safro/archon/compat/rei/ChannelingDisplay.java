package safro.archon.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import safro.archon.recipe.ChannelingRecipe;

import java.util.Collections;
import java.util.List;

public class ChannelingDisplay implements Display {
    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;
    private final int manaCost;

    public ChannelingDisplay(ChannelingRecipe recipe) {
        this.input = Collections.singletonList(EntryIngredients.ofItemStacks(recipe.getInputs()));
        this.output = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));
        this.manaCost = recipe.getManaCost();
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    public int getManaCost() {
        return manaCost;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ArchonREIPlugin.CHANNELING_DISPLAY;
    }
}
