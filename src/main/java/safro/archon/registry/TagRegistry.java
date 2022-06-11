package safro.archon.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;

public class TagRegistry {
    // Entity
    public static final TagKey<EntityType<?>> BOSSES = entity(new Identifier("c:bosses"));
    public static final TagKey<EntityType<?>> CREATURES = entity(new Identifier(Archon.MODID, "creatures"));
    public static final TagKey<EntityType<?>> PLAYERS = entity(new Identifier(Archon.MODID, "players"));

    // Item
    public static final TagKey<Item> BOOKS = item(new Identifier("c:books"));
    public static final TagKey<Item> LAPIS_LAZULIS = item(new Identifier("c:lapis_lazulis"));

    private static TagKey<EntityType<?>> entity(Identifier id) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, id);
    }

    private static TagKey<Item> item(Identifier id) {
        return TagKey.of(Registry.ITEM_KEY, id);
    }

    public static void init() {
    }
}
