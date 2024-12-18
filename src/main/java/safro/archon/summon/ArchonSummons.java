package safro.archon.summon;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.api.summon.Summon;
import safro.archon.api.summon.SummonHandler;
import safro.archon.item.SoulTomeItem;
import safro.saflib.SafLib;

public class ArchonSummons {
    public static final Summon TEST = SummonHandler.register("test", new TestSummon());

    public static void init() {
        createTome("test", TEST);
    }

    public static SoulTomeItem createTome(String name, Summon summon) {
        SoulTomeItem item = Registry.register(Registries.ITEM, new Identifier(Archon.MODID, name), new SoulTomeItem(summon, new FabricItemSettings().maxCount(1)));
        SafLib.ITEMS.add(new ItemStack(item));
        return item;
    }

    public static Item getTome(Summon summon) {
        String s = SummonHandler.getId(summon);
        Identifier tome = new Identifier(s + "_summon_tome");
        return Registries.ITEM.get(tome);
    }
}
