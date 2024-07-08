package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;

public class PropelSpell extends Spell {

    public PropelSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        Vec3d vec = player.getRotationVector();
        double horizontal = 0.4D + (0.15D * power.nonCriticalValue());
        double vertical = 0.36D + (0.04D * power.nonCriticalValue());

        player.addVelocity(vec.x * horizontal + (vec.x - player.getVelocity().x), vertical, vec.z * horizontal + (vec.z - player.getVelocity().z));
        player.velocityModified = true;
        player.fallDistance = 0;
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_HORSE_JUMP;
    }
}
