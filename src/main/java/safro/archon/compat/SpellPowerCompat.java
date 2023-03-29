package safro.archon.compat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import safro.archon.api.Spell;

public class SpellPowerCompat {

    public static double getBonusDamage(LivingEntity caster, LivingEntity target, Spell spell) {
        MagicSchool school = school(spell);
        SpellPower.Vulnerability vulnerability = SpellPower.getVulnerability(target, school);
        return SpellPower.getSpellPower(school, caster).randomValue(vulnerability);
    }

    public static boolean damage(PlayerEntity attacker, LivingEntity target, Spell spell, float amount) {
        return target.damage(SpellDamageSource.player(school(spell), attacker), amount);
    }

    public static MagicSchool school(Spell spell) {
        return switch (spell.getElement()) {
            case FIRE -> MagicSchool.FIRE;
            case WATER -> MagicSchool.FROST;
            case EARTH -> MagicSchool.ARCANE;
            case SKY -> MagicSchool.LIGHTNING;
            case END -> MagicSchool.SOUL;
        };
    }
}
