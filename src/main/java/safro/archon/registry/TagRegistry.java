package safro.archon.registry;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import safro.archon.Archon;

public class TagRegistry {
    public static final Tag<EntityType<?>> BOSSES = TagFactory.ENTITY_TYPE.create(new Identifier("c:bosses"));
    public static final Tag<EntityType<?>> CREATURES = TagFactory.ENTITY_TYPE.create(new Identifier(Archon.MODID, "creatures"));
    public static final Tag<EntityType<?>> PLAYERS = TagFactory.ENTITY_TYPE.create(new Identifier(Archon.MODID, "players"));

    public static void init() {

    }
}
