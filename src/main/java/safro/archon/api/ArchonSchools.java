package safro.archon.api;

import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import safro.archon.Archon;

public class ArchonSchools {
    public static final SpellSchool EARTH = SpellSchools.createMagic(new Identifier(Archon.MODID, "earth"), 0xBB8C4D);
    public static final SpellSchool VOID = SpellSchools.createMagic(new Identifier(Archon.MODID, "void"), 0x38184D);

    public static void register() {
        SpellSchools.register(EARTH);
        SpellSchools.register(VOID);
    }
}
