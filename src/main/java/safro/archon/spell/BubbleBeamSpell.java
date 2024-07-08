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
import safro.archon.entity.projectile.WaterBoltEntity;

public class BubbleBeamSpell extends Spell {

    public BubbleBeamSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        WaterBoltEntity bolt = new WaterBoltEntity(world, player, 5.0F, 5.0F, 5.0F);
        bolt.setPosition(player.getX(), player.getEyeY() - 0.1F, bolt.getZ());
        bolt.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.5F, 1.0F);
        bolt.setSplash(false);
        world.spawnEntity(bolt);
    }

    @Override
    public @Nullable SoundEvent getCastSound() {
        return SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP;
    }
}
