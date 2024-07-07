package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;

public class BubbleBeamSpell extends Spell {

    public BubbleBeamSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {

    }

    @Override
    public @Nullable SoundEvent getCastSound() {
        return SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP;
    }
}
