package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import safro.archon.api.Element;

public class ScorchSpell extends RaycastSpell {

    public ScorchSpell(Element type, int manaCost) {
        super(type, manaCost, 5);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, ItemStack stack, LivingEntity target) {
        target.setOnFireFor(10);
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ITEM_FLINTANDSTEEL_USE;
    }
}
