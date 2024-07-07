package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.entity.projectile.spell.CloudshotEntity;
import safro.archon.util.SpellUtil;

public class CloudshotSpell extends Spell {

    public CloudshotSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        CloudshotEntity cloudshot = new CloudshotEntity(world, player, ((target, owner, projectile) -> {
            SpellUtil.damage(player, target, projectile, this.getElement(), 8.0D, 0.5D);
        }));
        SpellUtil.shoot(world, player, SpellParticleData.of(240, 240, 209), cloudshot, 1.0F);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_MOSS_CARPET_PLACE;
    }
}
