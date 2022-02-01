package safro.archon.registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;

public class SoundRegistry {
    // Used Sounds
    public static SoundEvent CHANNEL_MANA = register("channel_mana");
    public static SoundEvent COMBUSTION = register("combustion");

    // Unused Sounds (for now)
    public static SoundEvent SOUL_CONVERSION = register("soul_conversion");

    static SoundEvent register(String id) {
        SoundEvent sound = new SoundEvent(new Identifier(Archon.MODID, id));
        Registry.register(Registry.SOUND_EVENT, new Identifier(Archon.MODID, id), sound);
        return sound;
    }
}
