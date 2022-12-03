package safro.archon.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.registry.BlockRegistry;
import safro.archon.registry.ItemRegistry;
import safro.archon.registry.MiscRegistry;
import safro.archon.registry.RecipeRegistry;

public class ArchonEmiPlugin implements EmiPlugin {

    //recipe category identifiers define the lang translation key. I've added them below the REI keys in the lang files
    private static final Identifier CHANNELING_ID = new Identifier(Archon.MODID, "channeling");
    private static final Identifier SCRIPTING_ID = new Identifier(Archon.MODID, "scripting");

    //the workstation will define the category to show when right-clicking the channeler or scripture table
    private static final EmiStack CHANNELING_WORKSTATION = EmiStack.of(ItemRegistry.CHANNELER);
    private static final EmiStack SCRIPTING_WORKSTATION = EmiStack.of(BlockRegistry.SCRIPTURE_TABLE);

    //EMI categories for the specific types of recipes. basically the same purpose as the REI ones
    public static final EmiRecipeCategory CHANNELING_CATEGORY = new EmiRecipeCategory(CHANNELING_ID, CHANNELING_WORKSTATION,new ArchonSimplifiedRenderer(0,0));
    public static final EmiRecipeCategory SCRIPTING_CATEGORY = new EmiRecipeCategory(SCRIPTING_ID, SCRIPTING_WORKSTATION,new ArchonSimplifiedRenderer(16,0));


    @Override
    public void register(EmiRegistry registry) {
        //register the categories
        registry.addCategory(CHANNELING_CATEGORY);
        registry.addCategory(SCRIPTING_CATEGORY);

        //register the workstations
        registry.addWorkstation(CHANNELING_CATEGORY,CHANNELING_WORKSTATION);
        registry.addWorkstation(SCRIPTING_CATEGORY,SCRIPTING_WORKSTATION);

        //register recipe handler for auto transferring slots. N/A to channeling as it is a world-interaction recipe
        registry.addRecipeHandler(MiscRegistry.SCRIPTURE_TABLE_SH, new ScriptingEmiHandler());

        //add all the recipes of the two needed types into new EmiRecipe wrappers
        RecipeManager manager = registry.getRecipeManager();
        manager.listAllOfType(RecipeRegistry.CHANNELING).forEach((recipe)-> registry.addRecipe(new ChannelingEmiRecipe(recipe)));
        manager.listAllOfType(RecipeRegistry.SCRIPTING).forEach((recipe)-> registry.addRecipe(new ScriptingEmiRecipe(recipe)));
    }
}
