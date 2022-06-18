package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.registry.SoundRegistry;
import safro.archon.util.SpellUtil;

public class GustSpell extends Spell {

    public GustSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        SpellUtil.create(world, player, ParticleTypes.SPIT, Items.GRAY_STAINED_GLASS_PANE, (target, owner, projectile) -> {
            Vec3d vec3d = projectile.getVelocity().multiply(1.0D, 0.0D, 1.0D).normalize().multiply(3D);
            if (vec3d.lengthSquared() > 0.0D) {
                target.addVelocity(vec3d.x, 0.3D, vec3d.z);
            }
        });
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundRegistry.GUST;
    }
}
