package safro.archon.api.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.registry.SpellRegistry;
import safro.archon.util.ArchonUtil;

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
        Identifier id = SpellRegistry.REGISTRY.getId(this);
        return "spell." + id.getNamespace() + "." + id.getPath();
    }

    /**
     * Called when the player successfully uses the spell. Used to implement spell functionality.
     * @param world World the spell is being executed in
     * @param player Player casting the spell
     * @param power Player's Spell Power for the element
     * @param stack Stack of the wand item
     */
    public abstract void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack);

    /**
     * Called when the player uses a wand. Used to check if the player can use this spell
     * @param world World the spell is being executed in
     * @param player Player casting the spell
     * @param stack Stack of the wand item
     */
    public boolean canCast(World world, PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof SpellAttributable tool && tool.getElement() == this.getElement()) {
            return ArchonUtil.canRemoveMana(player, this.getManaCost());
        }
        return false;
    }

    /**
     * Called when the player uses the spell on a block. MUST return either PASS or SUCCESS
     * @param world World the spell is being executed in
     * @param player Player casting the spell
     * @param stack Stack of the wand item
     * @return Returns an ActionResult for a successful or passed cast.
     */
    public ActionResult castOnBlock(ServerWorld world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        return ActionResult.PASS;
    }

    /**
     * Used to check if a block needs to be cast on a block or not
     * @return Returns true if the spell needs to be used on a block
     */
    public boolean isBlockCasted() {
        return false;
    }

    /**
     * @return The sound played when this spell is successfully casted. Can be null.
     */
    @Nullable
    public abstract SoundEvent getCastSound();
}
