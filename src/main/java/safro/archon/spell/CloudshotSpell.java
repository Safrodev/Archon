package safro.archon.spell;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.registry.BlockRegistry;
import safro.archon.util.SpellUtil;

public class CloudshotSpell extends Spell {

    public CloudshotSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        SpellUtil.create(world, player, ParticleTypes.ENTITY_EFFECT, BlockRegistry.SOLID_CLOUD, ((target, owner, projectile) -> {
            target.damage(DamageSource.mob(owner), 4.0F);
        }));
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_MOSS_CARPET_PLACE;
    }
}
