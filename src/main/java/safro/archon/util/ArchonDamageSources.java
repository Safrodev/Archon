package safro.archon.util;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import safro.archon.Archon;
import safro.saflib.util.DamageSourceUtil;

public class ArchonDamageSources {
    public static final RegistryKey<DamageType> HELLBEAM = DamageSourceUtil.register(Archon.MODID, "hellbeam");

    public static void init() {}
}
