package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;

public class SwapSpell extends RaycastSpell {

    public SwapSpell(Element type, int manaCost) {
        super(type, manaCost, 25);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack, LivingEntity target) {
        BlockPos t = target.getBlockPos();
        BlockPos p = player.getBlockPos();

        if (player.teleport(t.getX(), t.getY(), t.getZ(), true)) {
            world.emitGameEvent(GameEvent.TELEPORT, p, GameEvent.Emitter.of(player));
            target.teleport(p.getX(), p.getY(), p.getZ(), true);
        }
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ENTITY_ENDER_EYE_LAUNCH;
    }
}
