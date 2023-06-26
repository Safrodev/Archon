package safro.archon.registry;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import safro.archon.Archon;
import safro.archon.recipe.ChannelingRecipe;
import safro.archon.recipe.ChannelingSerializer;
import safro.archon.recipe.ScriptingRecipe;
import safro.archon.recipe.ScriptingSerializer;
import safro.saflib.registry.BaseRecipeRegistry;

public class RecipeRegistry extends BaseRecipeRegistry {
    static { MODID = Archon.MODID; }

    public static final RecipeType<ChannelingRecipe> CHANNELING = register("channeling");
    public static final RecipeSerializer<ChannelingRecipe> CHANNELING_SERIALIZER = register("channeling", new ChannelingSerializer());

    public static final RecipeType<ScriptingRecipe> SCRIPTING = register("scripting");
    public static final RecipeSerializer<ScriptingRecipe> SCRIPTING_SERIALIZER = register("scripting", new ScriptingSerializer());

    public static void init() {
    }
}
