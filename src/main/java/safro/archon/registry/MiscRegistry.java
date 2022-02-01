package safro.archon.registry;

import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;
import safro.archon.enchantment.ArcaneEnchantment;
import safro.archon.recipe.ChannelingRecipe;
import safro.archon.recipe.ChannelingSerializer;
import safro.archon.util.criterion.ChanneledCriterion;
import safro.archon.util.criterion.SummonUndeadCriterion;

public class MiscRegistry {
    // Recipes
    public static RecipeType<ChannelingRecipe> CHANNELING;
    public static RecipeSerializer<ChannelingRecipe> CHANNELING_SERIALIZER;

    // Enchantments
    public static Enchantment ARCANE;

    // Criterion
    public static SummonUndeadCriterion SUMMON_UNDEAD_CRITERION;
    public static ChanneledCriterion CHANNELED_CRITERION;

    static <T extends Recipe<?>> RecipeType<T> register(String id) {
        return Registry.register(Registry.RECIPE_TYPE, new Identifier(Archon.MODID, id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Archon.MODID, id), serializer);
    }

    public static void init() {
        CHANNELING = register("channeling");
        CHANNELING_SERIALIZER = register("channeling", new ChannelingSerializer());

        SUMMON_UNDEAD_CRITERION = CriteriaAccessor.callRegister(new SummonUndeadCriterion());
        CHANNELED_CRITERION = CriteriaAccessor.callRegister(new ChanneledCriterion());

        ARCANE = Registry.register(Registry.ENCHANTMENT, new Identifier(Archon.MODID, "arcane"), new ArcaneEnchantment());
    }
}
