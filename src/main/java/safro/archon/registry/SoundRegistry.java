package safro.archon.registry;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import safro.archon.Archon;

public class SoundRegistry {
    // Used Sounds
    public static SoundEvent CHANNEL_MANA = register("channel_mana");
    public static SoundEvent COMBUSTION = register("combustion");
    public static SoundEvent GUST = register("gust");

    static SoundEvent register(String id) {
        SoundEvent sound = SoundEvent.of(new Identifier(Archon.MODID, id));
        Registry.register(Registries.SOUND_EVENT, new Identifier(Archon.MODID, id), sound);
        return sound;
    }
}
