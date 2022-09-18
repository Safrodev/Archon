package safro.archon.util;

import net.minecraft.entity.damage.DamageSource;

public class ArchonDamageSource extends DamageSource {
    public static final DamageSource HELLBEAM = new ArchonDamageSource("hellbeam").setUsesMagic();

    public ArchonDamageSource(String name) {
        super(name);
    }
}
