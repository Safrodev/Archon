package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.util.SpellUtil;

public class HellbeamSpell extends Spell {

    public HellbeamSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        SpellUtil.shoot(world, player, SpellParticleData.of(235, 69, 19), (target, owner, projectile) -> {
            SpellUtil.damage(player, target, projectile, this.getElement(), 8.5D, 0.0D);
        }, 2.0F);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return null;
    }
}
