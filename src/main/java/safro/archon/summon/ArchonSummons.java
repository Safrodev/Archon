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
    public static final Summon HOUND_PACK = SummonHandler.register("hound_pack", new HoundPackSummon());
    public static final Summon SILVER_SWARM = SummonHandler.register("silver_swarm", new SilverSwarmSummon());
    public static final Summon ARCHER = SummonHandler.register("archer", new ArcherSummon());
    public static final Summon TWIN_KNIGHTS = SummonHandler.register("twin_knights", new TwinKnightsSummon());
    public static final Summon TITAN = SummonHandler.register("titan", new TitanSummon());

    public static void init() {
        createTome("hound_pack", HOUND_PACK);
        createTome("silver_swarm", SILVER_SWARM);
        createTome("archer", ARCHER);
        createTome("twin_knights", TWIN_KNIGHTS);
        createTome("titan", TITAN);
    }

    public static SoulTomeItem createTome(String name, Summon summon) {
        SoulTomeItem item = Registry.register(Registries.ITEM, new Identifier(Archon.MODID, name + "_soul_tome"), new SoulTomeItem(summon, new FabricItemSettings().maxCount(1)));
        SafLib.ITEMS.add(new ItemStack(item));
        return item;
    }

    public static Item getTome(Summon summon) {
        String s = SummonHandler.getId(summon);
        Identifier tome = new Identifier(s + "_soul_tome");
        return Registries.ITEM.get(tome);
    }
}
