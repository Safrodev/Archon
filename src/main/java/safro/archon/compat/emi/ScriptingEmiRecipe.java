package safro.archon.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;
import org.jetbrains.annotations.Nullable;
import safro.archon.Archon;
import safro.archon.client.screen.ScriptureTableScreen;
import safro.archon.recipe.ScriptingRecipe;
import safro.archon.registry.TagRegistry;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ScriptingEmiRecipe implements EmiRecipe {

    public ScriptingEmiRecipe(ScriptingRecipe recipe){
        this.recipe = recipe;
        //EmiIngredients are a custom ingredient that can also do other things like rendering etc.
        this.inputs = new LinkedList<>();
        //fancy way to grab the current items in the LAPIS_LAZULI tag. ifPresentOrElse will give an empty stack to lapis if no candidates are found
        Optional<RegistryEntryList.Named<Item>> opt = Registry.ITEM.getEntryList(TagRegistry.LAPIS_LAZULIS);
        opt.ifPresentOrElse((named) -> lapis = EmiIngredient.of(named.stream().map((item) -> EmiStack.of(item.value())).toList()),() -> lapis = EmiStack.EMPTY);
        //add the three inputs from the recipe itself
        this.inputs.addAll(recipe.getInputs().stream().map(EmiIngredient::of).toList());
        //same tag fanciness as above for the books tag, this time adding into the inputs list, as a book is required to complete the recipe
        Optional<RegistryEntryList.Named<Item>> opt2 = Registry.ITEM.getEntryList(TagRegistry.BOOKS);
        opt2.ifPresentOrElse((named) -> inputs.add(EmiIngredient.of(named.stream().map((item) -> EmiStack.of(item.value())).toList())),() ->inputs.add(EmiStack.EMPTY));
        //simple stack and list representation of the output
        output = EmiStack.of(recipe.getOutput());
        outputList = Collections.singletonList(this.output);
    }

    private final ScriptingRecipe recipe;
    private EmiIngredient lapis;
    private final List<EmiIngredient> inputs;
    private final EmiStack output;
    private final List<EmiStack> outputList;

    private final static Identifier SCRIPTING_BACKGROUND = new Identifier(Archon.MODID,"textures/gui/scripture_table_transparent.png");

    @Override
    public EmiRecipeCategory getCategory() {
        return ArchonEmiPlugin.SCRIPTING_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputList;
    }

    @Override
    public int getDisplayWidth() {
        return 147;
    }

    @Override
    public int getDisplayHeight() {
        return 65;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        //very similar to REI widgets, but EMI draws the gray background, so everything is transparent pieces.
        widgets.addTexture(new EmiTexture(SCRIPTING_BACKGROUND,0,0,147,65,147,65,147,65),0,0);
        widgets.addSlot(lapis,3,3).drawBack(false);
        widgets.addSlot(inputs.get(0),30,44).drawBack(false);
        widgets.addSlot(inputs.get(1),65,44).drawBack(false);
        widgets.addSlot(inputs.get(2),102,44).drawBack(false);
        widgets.addSlot(inputs.get(3),126,3).drawBack(false);
        widgets.addSlot(output,65,3).recipeContext(this); //recipe context is used to define the output(s) for use in the reciep tree.
        widgets.addTexture(new EmiTexture(ScriptureTableScreen.TEXTURE,176,29,18,4),3,26);
    }
}
