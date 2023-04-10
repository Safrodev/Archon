package safro.archon.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import safro.archon.client.screen.ScriptureTableScreen;
import safro.archon.registry.BlockRegistry;
import safro.archon.registry.TagRegistry;

import java.util.ArrayList;
import java.util.List;

public class ScriptingCategory implements DisplayCategory<ScriptingDisplay> {
    public static final Text TITLE = Text.translatable("rei.archon.scripting");
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

        widgets.add(Widgets.createTexturedWidget(ScriptureTableScreen.TEXTURE, bounds, 13, 13));
        widgets.add(Widgets.createTexturedWidget(ScriptureTableScreen.TEXTURE, x + 3, y + 26, 176, 29, 18, 4));
        widgets.add(Widgets.createSlot(new Point(x + 4, y + 4)).entries(getFromTag(TagRegistry.LAPIS_LAZULIS)));
        widgets.add(Widgets.createSlot(new Point(x + 127, y + 4)).entries(getFromTag(TagRegistry.BOOKS)));

        widgets.add(Widgets.createSlot(new Point(x + 66, y + 4)).entries(display.getOutputEntries().get(0)).markOutput());
        widgets.add(Widgets.createSlot(new Point(x + 66, y + 45)).entries(display.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createSlot(new Point(x + 31, y + 45)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(x + 103, y + 45)).entries(display.getInputEntries().get(2)).markInput());
        return widgets;
    }

    private static EntryIngredient getFromTag(TagKey<Item> tag) {
        List<ItemStack> list = new ArrayList<>();
        Registries.ITEM.iterateEntries(tag).forEach(entry -> list.add(new ItemStack(entry)));
        return EntryIngredients.ofItemStacks(list);
    }
}
