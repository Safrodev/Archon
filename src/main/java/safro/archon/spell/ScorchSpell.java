package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import safro.archon.api.Element;
import safro.archon.api.Spell;

public class ScorchSpell extends Spell {

    public ScorchSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        HitResult result = raycast(player);
        if (result.getType() == HitResult.Type.ENTITY) {
            ((EntityHitResult)result).getEntity().setOnFireFor(5);
        }
    }

    @Override
    public boolean canCast(World world, PlayerEntity player, ItemStack stack) {
        if (super.canCast(world, player, stack)) {
            HitResult result = raycast(player);
            if (result.getType() == HitResult.Type.ENTITY) {
                return ((EntityHitResult)result).getEntity() instanceof LivingEntity;
            }
        }
        return false;
    }

    private HitResult raycast(PlayerEntity player) {
        return player.raycast(5.0F, 0.0F, false);
    }

    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ITEM_FLINTANDSTEEL_USE;
    }
}
