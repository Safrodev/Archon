package safro.archon.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import safro.archon.client.screen.ScriptureTableScreen;
import safro.archon.registry.BlockRegistry;
import safro.archon.registry.TagRegistry;

import java.util.ArrayList;
import java.util.List;

public class ScriptingCategory implements DisplayCategory<ScriptingDisplay> {
    public static final TranslatableText TITLE = new TranslatableText("rei.archon.scripting");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(BlockRegistry.SCRIPTURE_TABLE);

    @Override
    public Renderer getIcon() {
        return ICON;
    }

    @Override
    public Text getTitle() {
        return TITLE;
    }

    @Override
    public CategoryIdentifier<? extends ScriptingDisplay> getCategoryIdentifier() {
        return ArchonREIPlugin.SCRIPTING_DISPLAY;
    }

    @Override
    public List<Widget> setupDisplay(ScriptingDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        int x = bounds.getMinX();
        int y = bounds.getMinY();

        widgets.add(Widgets.createTexturedWidget(ScriptureTableScreen.TEXTURE, bounds, 15, 15));
        widgets.add(Widgets.createTexturedWidget(ScriptureTableScreen.TEXTURE, x + 10, y + 50, 176, 29, 18, 4));
        widgets.add(Widgets.createSlot(new Point(x + 10, y + 30)).entries(EntryIngredients.ofIngredient(Ingredient.fromTag(TagRegistry.LAPIS_LAZULIS))));
        widgets.add(Widgets.createSlot(new Point(x + 50, y + 30)).entries(EntryIngredients.ofIngredient(Ingredient.fromTag(TagRegistry.BOOKS))));
        return widgets;
    }
}
