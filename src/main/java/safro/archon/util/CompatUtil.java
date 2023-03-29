package safro.archon.util;

import net.fabricmc.loader.api.FabricLoader;

public class CompatUtil {

    public static boolean isSpellPowerInstalled() {
        return FabricLoader.getInstance().isModLoaded("spell_power");
    }
}
