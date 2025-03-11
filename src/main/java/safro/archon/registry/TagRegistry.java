package safro.archon.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import safro.archon.Archon;
import safro.saflib.registry.BaseTagRegistry;

public class TagRegistry extends BaseTagRegistry {
    // Item
    public static final TagKey<Item> BOOKS = item("c", "books");
    public static final TagKey<Item> LAPIS_LAZULIS = item("c", "lapis_lazulis");
    public static final TagKey<Item> CAN_REAP = item("archon", "can_reap");

    // Block
    public static final TagKey<Block> TERRAIN = block(Archon.MODID, "terrain");

    // Biome
    public static final TagKey<Biome> HAS_SKY_NODE = biome(Archon.MODID, "has_sky_node");

    public static void init() {
    }
}
