package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.util.ArchonUtil;

public class VacuumSpell extends RaycastSpell {

    public VacuumSpell(Element type, int manaCost) {
        super(type, manaCost, 20);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack, LivingEntity target) {
        double d = player.getX() - target.getX();
        double e = player.getY() - target.getY();
        double f = player.getZ() - target.getZ();
        target.setVelocity(d * 0.1D, e * 0.1D + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * 0.1D);
    }

    public boolean canTarget(PlayerEntity caster, LivingEntity target) {
        if (target instanceof PlayerEntity player) {
            return !player.getAbilities().flying;
        }
        return target.isAlive() && !ArchonUtil.isOwnedBy(caster, target);
    }

    @Override
    public @Nullable SoundEvent getCastSound() {
        return SoundEvents.ENTITY_SNOWBALL_THROW;
    }
}
