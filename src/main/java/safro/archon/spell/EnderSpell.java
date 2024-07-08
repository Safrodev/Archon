package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;

public class EnderSpell extends Spell {

    public EnderSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        EnderChestInventory enderChestInventory = player.getEnderChestInventory();
        Text container = Text.translatable("container.enderchest");

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, playerx) -> GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderChestInventory), container));
        player.incrementStat(Stats.OPEN_ENDERCHEST);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_ENDER_CHEST_OPEN;
    }
}
