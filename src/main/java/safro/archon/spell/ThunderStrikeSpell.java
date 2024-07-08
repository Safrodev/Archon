package safro.archon.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.util.ArchonUtil;

public class ThunderStrikeSpell extends RaycastSpell {

    public ThunderStrikeSpell(Element type, int manaCost) {
        super(type, manaCost, 15);
    }

    @Override
    public void onRaycast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack, LivingEntity target) {
        ArchonUtil.createLightning(world, target.getBlockPos(), false);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.ITEM_TRIDENT_THUNDER;
    }
}
