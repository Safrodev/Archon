package safro.archon.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.DefaultInformationDisplay;
import net.minecraft.item.ItemConvertible;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.recipe.ChannelingRecipe;
import safro.archon.recipe.ScriptingRecipe;
import safro.archon.registry.ItemRegistry;
import safro.archon.registry.RecipeRegistry;

public class ArchonREIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<ChannelingDisplay> CHANNELING_DISPLAY = CategoryIdentifier.of(new Identifier(Archon.MODID, "channeling"));
    public static final CategoryIdentifier<ScriptingDisplay> SCRIPTING_DISPLAY = CategoryIdentifier.of(new Identifier(Archon.MODID, "scripting"));

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ChannelingCategory());
        registry.addWorkstations(CHANNELING_DISPLAY, ChannelingCategory.ICON);

        registry.add(new ScriptingCategory());
        registry.addWorkstations(SCRIPTING_DISPLAY, ScriptingCategory.ICON);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(ChannelingRecipe.class, RecipeRegistry.CHANNELING, ChannelingDisplay::new);
        registry.registerRecipeFiller(ScriptingRecipe.class, RecipeRegistry.SCRIPTING, ScriptingDisplay::new);

        registry.add(create(ItemRegistry.TERRANITE_STONE, "rei.archon.terranite_stone"));
        registry.add(create(ItemRegistry.ANGELIC_STAR, "rei.archon.angelic_star"));
        registry.add(create(ItemRegistry.WAVE_CRYSTAL, "rei.archon.wave_crystal"));
        registry.add(create(ItemRegistry.CHARRED_EYE, "rei.archon.charred_eye"));
        registry.add(create(ItemRegistry.SOULLESS_EYE, "rei.archon.soulless_eye"));

        SoulBindingRecipeFiller filler = new SoulBindingRecipeFiller();
        filler.registerDisplays(registry);
    }

    private static DefaultInformationDisplay create(ItemConvertible item, String key) {
        DefaultInformationDisplay info = DefaultInformationDisplay.createFromEntry(EntryStacks.of(item), item.asItem().getName());
        info.line(Text.translatable(key));
        return info;
    }
}
