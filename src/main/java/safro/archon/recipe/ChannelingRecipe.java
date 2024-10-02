package safro.archon.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.Archon;
import safro.archon.registry.RecipeRegistry;

import java.util.ArrayList;
import java.util.List;

public class ChannelingRecipe implements Recipe<ChannelingInventory> {
    private final TagKey<Block> tag;
    private final Block block;
    public final ItemStack result;
    private final Identifier id;
    private final int manaCost;

    public ChannelingRecipe(Block input, ItemStack result, int manaCost, Identifier id) {
        this.block = input;
        this.tag = null;
        this.result = result;
        this.id = id;
        this.manaCost = manaCost;
    }

    public ChannelingRecipe(TagKey<Block> input, ItemStack result, int manaCost, Identifier id) {
        this.tag = input;
        this.block = null;
        this.result = result;
        this.id = id;
        this.manaCost = manaCost;
    }

    private static List<ItemStack> mapTag(TagKey<Block> tag) {
        List<ItemStack> list = new ArrayList<>();
        Registries.BLOCK.iterateEntries(tag).forEach(entry -> list.add(new ItemStack(entry.value())));
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

    @Nullable
    public TagKey<Block> getTag() {
        return tag;
    }

    @Nullable
    public Block getBlock() {
        return this.block;
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
        ItemStack test = new ItemStack(block);
        if (recipe.block != null) {
            return new ItemStack(recipe.block).isOf(test.getItem());
        } else if (recipe.tag != null) {
            for (RegistryEntry<Block> entry : Registries.BLOCK.iterateEntries(recipe.tag)) {
                if (new ItemStack(entry.value()).isOf(test.getItem())) {
                    return true;
                }
            }
            return false;
        } else {
            Archon.LOGGER.error("Recipe '" + recipe.id.toString() + "' has no valid input");
            return false;
        }
    }
}
