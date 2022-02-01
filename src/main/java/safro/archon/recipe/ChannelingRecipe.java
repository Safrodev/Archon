package safro.archon.recipe;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import safro.archon.Archon;
import safro.archon.registry.MiscRegistry;
import safro.archon.registry.SoundRegistry;
import safro.archon.util.ArchonUtil;

import java.util.ArrayList;

public class ChannelingRecipe implements Recipe<ChannelingInventory> {
    public final ArrayList<Ingredient> ingredients;
    public final ItemStack result;
    private final Identifier id;
    private final int manaCost;

    public ChannelingRecipe(ArrayList<Ingredient> ingredientList, ItemStack result, int manaCost, Identifier id) {
        this.ingredients = ingredientList;
        this.result = result;
        this.id = id;
        this.manaCost = manaCost;
    }

    @Override
    public boolean matches(ChannelingInventory inv, World world) {
        if (ArchonUtil.canRemoveMana(inv.getPlayer(), getManaCost())) {
            return ingredients.get(0).test(inv.getStack(0));
        }
        return false;
    }

    @Override
    public ItemStack craft(ChannelingInventory inv) {
        ItemEntity result = new ItemEntity(inv.getWorld(), inv.getPos().getX(), inv.getPos().getY(), inv.getPos().getZ(), getOutput().copy());
        result.setToDefaultPickupDelay();
        inv.getWorld().spawnEntity(result);
        inv.getBlockStack().decrement(1);
        ArchonUtil.get(inv.getPlayer()).removeMana(getManaCost());
        if (Archon.CONFIG.play_channel_sound) {
            inv.getWorld().playSound(null, inv.getPos(), SoundRegistry.CHANNEL_MANA, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        if (inv.getPlayer() instanceof ServerPlayerEntity) {
            MiscRegistry.CHANNELED_CRITERION.trigger((ServerPlayerEntity)inv.getPlayer(), getOutput().copy());
        }
        return getOutput().copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return this.result;
    }

    // Might be client side only
    public ItemStack getIngredient() {
        if (ingredients.get(0) != null) {
            return ingredients.get(0).getMatchingStacks()[0];
        }
        return ItemStack.EMPTY;
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
