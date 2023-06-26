package safro.archon.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class ScriptingSerializer implements RecipeSerializer<ScriptingRecipe> {

    @Override
    public ScriptingRecipe read(Identifier id, JsonObject json) {
        DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(json, "ingredients"));
        if (ingredients.size() != 3) {
            throw new JsonParseException("Scripting Recipe must have 3 input ingredients!");
        }
        Item result = ShapedRecipe.getItem(JsonHelper.getObject(json, "result"));
        return new ScriptingRecipe(id, ingredients, result);
    }

    @Override
    public ScriptingRecipe read(Identifier id, PacketByteBuf buf) {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
        for (int i = 0; i < ingredients.size(); i++) {
            ingredients.set(i, Ingredient.fromPacket(buf));
        }
        Item result = Item.byRawId(buf.readVarInt());
        return new ScriptingRecipe(id, ingredients, result);
    }

    @Override
    public void write(PacketByteBuf buf, ScriptingRecipe recipe) {
        buf.writeVarInt(recipe.getInputs().size());
        for (Ingredient in : recipe.getInputs()) {
            in.write(buf);
        }
        Item result = recipe.result;
        buf.writeVarInt(Item.getRawId(result));
    }

    public static DefaultedList<Ingredient> getIngredients(JsonArray json) {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        for (int i = 0; i < json.size(); i++) {
            Ingredient ingredient = Ingredient.fromJson(json.get(i));
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }
}
