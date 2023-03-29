package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.Spell;

public class PropelSpell extends Spell {

    public PropelSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        Vec3d vec = player.getRotationVector();
        player.setVelocity(0, 0.4F, 0);
        player.addVelocity(vec.x * 0.5F + (vec.x - player.getVelocity().x), 0, vec.z * 0.5F + (vec.z - player.getVelocity().z));
        player.velocityModified = true;
        player.fallDistance = 0;
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_HORSE_JUMP;
    }
}
