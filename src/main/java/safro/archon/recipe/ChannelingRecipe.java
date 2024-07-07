package safro.archon.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import safro.archon.registry.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;

public class ChannelingRecipe implements Recipe<ChannelingInventory> {
    private final Ingredient input;
    public final ItemStack result;
    private final Identifier id;
    private final int manaCost;

    public ChannelingRecipe(Ingredient input, ItemStack result, int manaCost, Identifier id) {
        this.input = input;
        this.result = result;
        this.id = id;
        this.manaCost = manaCost;
    }

    public ChannelingRecipe(Block input, ItemStack result, int manaCost, Identifier id) {
        this(Ingredient.ofItems(input), result, manaCost, id);
    }

    public ChannelingRecipe(TagKey<Block> tag, ItemStack result, int manaCost, Identifier id) {
        this(Ingredient.ofItems(mapTag(tag).toArray(new ItemConvertible[]{})), result, manaCost, id);
    }

    private static List<Block> mapTag(TagKey<Block> tag) {
        List<Block> list = new ArrayList<>();
        Registries.BLOCK.iterateEntries(tag).forEach(entry -> list.add(entry.value()));
        return list;
    }

    @Override
    public boolean matches(ChannelingInventory inv, World world) {
        return false;
    }

    @Override
    public ItemStack craft(ChannelingInventory inventory, DynamicRegistryManager manager) {
        return this.result;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.result;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public int getManaCost() {
        return manaCost;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.CHANNELING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.CHANNELING;
    }

    /**
     * Used to check if the provided block is valid for a recipe
     * @param recipe Recipe to be used
     * @param block Block to be checked
     * @return Returns true if valid
     */
    public static boolean isValid(ChannelingRecipe recipe, Block block) {
        return recipe.input.test(new ItemStack(block));
    }
}
