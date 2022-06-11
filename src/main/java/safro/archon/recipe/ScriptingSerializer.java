package safro.archon.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class ScriptingSerializer implements RecipeSerializer<ScriptingRecipe> {

    @Override
    public ScriptingRecipe read(Identifier id, JsonObject json) {
        List<Ingredient> ingredients = getIngredients(JsonHelper.getArray(json, "ingredients"));
        if (ingredients.size() != 3) {
            throw new JsonParseException("Scripting Recipe must have 3 input ingredients!");
        }
        return new ScriptingRecipe(id, ingredients, ShapedRecipe.getItem(JsonHelper.getObject(json, "result")));
    }

    @Override
    public ScriptingRecipe read(Identifier id, PacketByteBuf buf) {
        int size = buf.readInt();
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ingredients.set(i, Ingredient.fromPacket(buf));
        }
        return new ScriptingRecipe(id, ingredients, getItemFromPacket(buf));
    }

    @Override
    public void write(PacketByteBuf buf, ScriptingRecipe recipe) {
        buf.writeInt(recipe.getInputs().size());
        for (Ingredient in : recipe.getInputs()) {
            in.write(buf);
        }
        buf.writeItemStack(recipe.getOutput());
    }

    public static List<Ingredient> getIngredients(JsonArray json) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            Ingredient ingredient = Ingredient.fromJson(json.get(i));
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    public static Item getItemFromPacket(PacketByteBuf buf) {
        String string = buf.readString();
        Item item = Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Invalid item: " + string);
        } else {
            return item;
        }
    }
}
