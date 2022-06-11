package safro.archon.registry;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;
import safro.archon.recipe.ChannelingRecipe;
import safro.archon.recipe.ChannelingSerializer;
import safro.archon.recipe.ScriptingRecipe;
import safro.archon.recipe.ScriptingSerializer;

public class RecipeRegistry {
    public static final RecipeType<ChannelingRecipe> CHANNELING = register("channeling");
    public static final RecipeSerializer<ChannelingRecipe> CHANNELING_SERIALIZER = register("channeling", new ChannelingSerializer());

    public static final RecipeType<ScriptingRecipe> SCRIPTING = register("scripting");
    public static final RecipeSerializer<ScriptingRecipe> SCRIPTING_SERIALIZER = register("scripting", new ScriptingSerializer());

    static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registry.RECIPE_TYPE, new Identifier(Archon.MODID, id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Archon.MODID, id), serializer);
    }

    public static void init() {
    }
}
