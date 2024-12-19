package safro.archon.recipe;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import safro.archon.api.summon.Summon;
import safro.archon.item.SoulTomeItem;
import safro.archon.item.UndeadStaffItem;
import safro.archon.registry.ItemRegistry;
import safro.archon.registry.RecipeRegistry;

public class SoulBindingRecipe extends SpecialCraftingRecipe {

    public SoulBindingRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        ItemStack staff = ItemStack.EMPTY;
        ItemStack tome = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                if (stack.isOf(ItemRegistry.UNDEAD_STAFF)) {
                    if (!staff.isEmpty()) {
                        return false;
                    }

                    staff = stack;
                } else if (stack.getItem() instanceof SoulTomeItem) {
                    if (!tome.isEmpty()) {
                        return false;
                    }

                    tome = stack;
                }
            }
        }

        if (!staff.isEmpty() && !tome.isEmpty()) {
            Summon tomeSummon = ((SoulTomeItem)tome.getItem()).getSummon();
            return !UndeadStaffItem.getSummons(staff).contains(tomeSummon);
        } else {
            return false;
        }
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack staff = ItemStack.EMPTY;
        ItemStack tome = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                if (stack.isOf(ItemRegistry.UNDEAD_STAFF)) {
                    if (!staff.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    staff = stack;
                } else if (stack.getItem() instanceof SoulTomeItem) {
                    if (!tome.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    tome = stack;
                }
            }
        }

        if (!staff.isEmpty() && !tome.isEmpty()) {
            Summon tomeSummon = ((SoulTomeItem)tome.getItem()).getSummon();
            if (!UndeadStaffItem.getSummons(staff).contains(tomeSummon)) {
                ItemStack result = staff.copyWithCount(1);
                UndeadStaffItem.addSummon(tomeSummon, result);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.SOUL_BINDING_SERIALIZER;
    }
}
