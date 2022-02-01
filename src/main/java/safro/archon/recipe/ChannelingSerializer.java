package safro.archon.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

public class ChannelingSerializer implements RecipeSerializer<ChannelingRecipe> {

    @Override
    public ChannelingRecipe read(Identifier id, JsonObject json) {
        ChannelingJsonFactory recipeJson = new Gson().fromJson(json, ChannelingJsonFactory.class);
        JsonArray ingredients = recipeJson.input;
        ArrayList<Ingredient> myIngredients = new ArrayList<>();
        for(JsonElement jsonElement : ingredients) {
            myIngredients.add(Ingredient.fromJson(jsonElement));
        }
        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.output)).get();
        ItemStack output = new ItemStack(outputItem, recipeJson.count);
        int manaCost = recipeJson.manaCost;
        return new ChannelingRecipe(myIngredients, output, manaCost, id);
    }

    @Override
    public ChannelingRecipe read(Identifier id, PacketByteBuf buf) {
        JsonArray array = new JsonArray();
        ArrayList<Ingredient> myIngredients = new ArrayList<>();
        for(JsonElement jsonElement : array)
        {
            myIngredients.add(Ingredient.fromJson(jsonElement));
        }
        ItemStack stack = buf.readItemStack();
        int cost = buf.readInt();
        return new ChannelingRecipe(myIngredients, stack, cost, id);
    }

    @Override
    public void write(PacketByteBuf buf, ChannelingRecipe recipe) {
        for(int i = 0; i < recipe.ingredients.size(); i++) {
            recipe.ingredients.get(i).write(buf);
        }
        buf.writeItemStack(recipe.getOutput());
    }
}
