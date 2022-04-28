package safro.archon.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import safro.archon.registry.MiscRegistry;

import java.util.ArrayList;
import java.util.List;

public class ChannelingRecipe implements Recipe<ChannelingInventory> {
    private final Block input;
    private final TagKey<Block> tag;
    private final ItemStack result;
    private final Identifier id;
    private final int manaCost;

    public ChannelingRecipe(Block input, ItemStack result, int manaCost, Identifier id) {
        this.input = input;
        this.tag = null;
        this.result = result;
        this.id = id;
        this.manaCost = manaCost;
    }

    public ChannelingRecipe(TagKey<Block> tag, ItemStack result, int manaCost, Identifier id) {
        this.tag = tag;
        this.input = null;
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

    public TagKey<Block> getTag() {
        return tag;
    }

    @Override
    public ItemStack getOutput() {
        return this.result;
    }

    public List<ItemStack> getInputs() {
        if (this.getTag() != null) {
            List<ItemStack> list = new ArrayList<>();
            for (RegistryEntry<Block> entry : Registry.BLOCK.iterateEntries(this.getTag())) {
                list.add(new ItemStack(entry.value()));
            }
            return list;
        }
        return List.of(new ItemStack(this.getBlock()));
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

    /**
     * Used to check if the provided block is valid for a recipe
     * @param recipe Recipe to be used
     * @param block Block to be checked
     * @return Returns true if valid
     */
    public static boolean isValid(ChannelingRecipe recipe, Block block) {
        if (recipe.getBlock() != null) {
            return recipe.getBlock() == block;
        } else if (recipe.getTag() != null) {
            return block.getRegistryEntry().isIn(recipe.getTag());
        }
        return false;
    }
}
