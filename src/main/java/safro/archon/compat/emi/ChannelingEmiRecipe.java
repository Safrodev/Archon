package safro.archon.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import safro.archon.recipe.ChannelingRecipe;

import java.util.Collections;
import java.util.List;

public class ChannelingEmiRecipe implements EmiRecipe {

    public ChannelingEmiRecipe(ChannelingRecipe recipe){
        this.recipe = recipe;
        //taking the list of inputs from the recipe and mapping them to a list of EmiStacks which then makes one EmiIngredient (which will cycle through the tag if present).
        this.inputs = recipe.getInputs().stream().map(item -> EmiIngredient.of(Collections.singletonList(EmiStack.of(item)))).toList();
        //stack and list representation of the output
        this.output = EmiStack.of(recipe.result);
        this.outputList = Collections.singletonList(this.output);
        //setting up rendering of the mana cost text. positioning it in the middle of the viewer with width/2
        this.manaCost = Text.translatable("text.archon.mana_cost", recipe.getManaCost()).asOrderedText();
        int width = MinecraftClient.getInstance().textRenderer.getWidth(this.manaCost);
        this.labelX = 41 - width/2;
    }

    private final ChannelingRecipe recipe;
    private final List<EmiIngredient> inputs;
    private final EmiStack output;
    private final List<EmiStack> outputList;
    private final int labelX;
    private final OrderedText manaCost;

    @Override
    public EmiRecipeCategory getCategory() {
        return ArchonEmiPlugin.CHANNELING_CATEGORY;
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
        return 83;
    }

    @Override
    public int getDisplayHeight() {
        return 42;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        //very similar to REI widgets, but EMI draws the gray background, so everything is transparent pieces.
        widgets.addSlot(inputs.get(0), 4, 4);
        widgets.addTexture(EmiTexture.EMPTY_ARROW,28,5); //pre-provided arrow widget to render that on the viewer background
        widgets.addSlot(output,57,0).large(true).recipeContext(this);//output true makes the slot big
        widgets.addText(manaCost,labelX,29,43690,true);
    }
}
