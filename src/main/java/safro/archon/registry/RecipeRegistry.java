package safro.archon.registry;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import safro.archon.Archon;
import safro.archon.recipe.*;
import safro.saflib.registry.BaseRecipeRegistry;

public class RecipeRegistry extends BaseRecipeRegistry {
    static { MODID = Archon.MODID; }

    public static final RecipeType<ChannelingRecipe> CHANNELING = register("channeling");
    public static final RecipeSerializer<ChannelingRecipe> CHANNELING_SERIALIZER = register("channeling", new ChannelingSerializer());

    public static final RecipeType<ScriptingRecipe> SCRIPTING = register("scripting");
    public static final RecipeSerializer<ScriptingRecipe> SCRIPTING_SERIALIZER = register("scripting", new ScriptingSerializer());

    public static final RecipeSerializer<SoulBindingRecipe> SOUL_BINDING_SERIALIZER = register("crafting_special_soul_binding", new SpecialRecipeSerializer<>(SoulBindingRecipe::new));

    public static void init() {
    }
}
