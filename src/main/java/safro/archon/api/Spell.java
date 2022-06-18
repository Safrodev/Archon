package safro.archon.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import safro.archon.Archon;
import safro.archon.item.WandItem;
import safro.archon.util.ArchonUtil;

import javax.annotation.Nullable;

public abstract class Spell {
    private final Element element;
    private final int cost;

    public Spell(Element type, int manaCost) {
        this.element = type;
        this.cost = manaCost;
    }

    public Element getElement() {
        return element;
    }

    public int getManaCost() {
        return this.cost;
    }

    public String getTranslationKey() {
        Identifier id = Archon.SPELL.getId(this);
        return "spell." + id.getNamespace() + "." + id.getPath();
    }

    /**
     * Called when the player successfully uses the spell. Used to implement spell functionality.
     * @param world World the spell is being executed in
     * @param player Player casting the spell
     * @param stack Stack of the wand item
     */
    public abstract void cast(World world, PlayerEntity player, ItemStack stack);

    /**
     * Called when the player uses a wand. Used to check if the player can use this spell
     * @param world World the spell is being executed in
     * @param player Player casting the spell
     * @param stack Stack of the wand item
     */
    public boolean canCast(World world, PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof WandItem wand && wand.getElement() == this.getElement()) {
            return ArchonUtil.canRemoveMana(player, this.getManaCost());
        }
        return false;
    }

    /**
     * @return The sound played when this spell is successfully casted. Can be null.
     */
    @Nullable
    public abstract SoundEvent getCastSound();
}
