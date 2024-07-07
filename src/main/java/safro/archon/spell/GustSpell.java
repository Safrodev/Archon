package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.registry.SoundRegistry;
import safro.archon.util.SpellUtil;

public class GustSpell extends Spell {

    public GustSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        SpellUtil.shoot(world, player, SpellParticleData.of(237, 237, 237, 0.7F), (target, owner, projectile) -> {
            SpellUtil.damage(player, target, projectile, this.getElement(), 0.3D, 5.0D);
        }, 1.5F);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundRegistry.GUST;
    }
}
