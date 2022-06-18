package safro.archon.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.recipe.ChannelingRecipe;
import safro.archon.recipe.ScriptingRecipe;

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
        registry.registerFiller(ChannelingRecipe.class, ChannelingDisplay::new);
        registry.registerFiller(ScriptingRecipe.class, ScriptingDisplay::new);
    }
}
