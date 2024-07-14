package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;

public class WarpSpell extends Spell {

    public WarpSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        HitResult hit = player.raycast(8 + power.nonCriticalValue(), 0.0F, true);
        player.teleport(hit.getPos().x, hit.getPos().y, hit.getPos().z);
    }

    @Override
    public @Nullable SoundEvent getCastSound() {
        return SoundEvents.ENTITY_ENDERMAN_TELEPORT;
    }
}
