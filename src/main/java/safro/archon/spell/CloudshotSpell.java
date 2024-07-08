package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.util.SpellUtil;

public class CloudshotSpell extends Spell {

    public CloudshotSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        SpellUtil.shoot(world, player, SpellParticleData.of(240, 240, 209, 0.4F), 0.7F, ((target, owner, projectile) -> {
            SpellUtil.damage(player, target, projectile, this.getElement(), 6.0D, 0.2D);
        }));
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_MOSS_CARPET_PLACE;
    }
}
