package safro.archon.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import safro.archon.registry.RecipeRegistry;

public class ScriptingRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final DefaultedList<Ingredient> inputs;
    public final Item result;

    public ScriptingRecipe(Identifier id, DefaultedList<Ingredient> inputs, Item result) {
        this.id = id;
        this.inputs = checkInputs(inputs);
        this.result = result;
    }

    public static DefaultedList<Ingredient> checkInputs(DefaultedList<Ingredient> in) {
        if (in.size() != 3) throw new IllegalArgumentException("Scripting Recipe can only have 3 ingredients!");
        return in;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        for (int i = 0; i < this.inputs.size(); i++) {
            Ingredient in = this.inputs.get(i);
            if (!in.test(inventory.getStack(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager manager) {
        return new ItemStack(this.result);
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager manager) {
        return new ItemStack(this.result);
    }

    public DefaultedList<Ingredient> getInputs() {
        return this.inputs;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.SCRIPTING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.SCRIPTING;
    }
}
