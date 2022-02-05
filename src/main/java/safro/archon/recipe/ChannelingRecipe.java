package safro.archon.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import safro.archon.registry.MiscRegistry;

public class ChannelingRecipe implements Recipe<ChannelingInventory> {
    public final Block input;
    public final ItemStack result;
    private final Identifier id;
    private final int manaCost;

    public ChannelingRecipe(Block input, ItemStack result, int manaCost, Identifier id) {
        this.input = input;
        this.result = result;
        this.id = id;
        this.manaCost = manaCost;
    }

    @Override
    public boolean matches(ChannelingInventory inv, World world) {
        return false;
    }

    @Override
    public ItemStack craft(ChannelingInventory inv) {
        return this.result;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    public Block getBlock() {
        return input;
    }

    @Override
    public ItemStack getOutput() {
        return this.result;
    }

    public ItemStack getIngredient() {
        return new ItemStack(input);
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
        return MiscRegistry.CHANNELING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return MiscRegistry.CHANNELING;
    }


}
