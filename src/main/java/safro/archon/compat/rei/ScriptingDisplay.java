package safro.archon.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import safro.archon.recipe.ScriptingRecipe;

import java.util.Collections;
import java.util.List;

public class ScriptingDisplay implements Display {
    private final List<EntryIngredient> input;
    private final List<EntryIngredient> output;

    public ScriptingDisplay(ScriptingRecipe recipe) {
        this.input = EntryIngredients.ofIngredients(recipe.getInputs());
        this.output = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return input;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return output;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ArchonREIPlugin.SCRIPTING_DISPLAY;
    }
}
