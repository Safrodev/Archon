package safro.archon.api;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;

import java.util.UUID;

public class ManaAttributes {
    public static final ClampedEntityAttribute MAX_MANA = register("max_mana", 100, 1, 1024);

    public static final UUID CAPACITY_SCROLL_MODIFIER = UUID.fromString("26a3cd44-900c-4a88-8562-75fc31690ebe");
    public static final UUID MAX_ITEM_MODIFIER = UUID.fromString("d8b87e74-5699-483a-9644-4e16311a8d27");
    public static final UUID MANA_BOOST_MODIFIER = UUID.fromString("b3c2b78f-f1bd-47f0-8b5e-a11dff725fc6");

    private static ClampedEntityAttribute register(String name, double base, double min, double max) {
        ClampedEntityAttribute attribute = (ClampedEntityAttribute) new ClampedEntityAttribute("attribute.name.generic." + name, base, min, max).setTracked(true);
        return Registry.register(Registry.ATTRIBUTE, new Identifier(Archon.MODID, name), attribute);
    }

    public static void init() {}
}
