package safro.archon.api.summon;

import net.minecraft.util.Identifier;
import safro.archon.Archon;

import java.util.HashMap;
import java.util.Map;

public class SummonHandler {
    private static final Map<Identifier, Summon> SUMMONS = new HashMap<>();

    public static Summon register(Identifier id, Summon summon) {
        SUMMONS.put(id, summon);
        return summon;
    }

    public static Summon register(String key, Summon summon) {
        register(new Identifier(Archon.MODID, key), summon);
        return summon;
    }

    public static Summon fromString(String id) {
        return SUMMONS.get(new Identifier(id));
    }

    public static String getId(Summon summon) {
        for (Map.Entry<Identifier, Summon> entry : SUMMONS.entrySet()) {
            if (entry.getValue().equals(summon)) {
                return entry.getKey().toString();
            }
        }

        Archon.LOGGER.error("Cannot find ID for a Summon! It may not have been registered.");
        return null;
    }
}
