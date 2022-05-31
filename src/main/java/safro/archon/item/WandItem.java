package safro.archon.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import safro.archon.Archon;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.enchantment.ArcaneEnchantment;
import safro.archon.util.ArchonUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class WandItem extends Item {
    private final Element type;

    public WandItem(Element element, Settings settings) {
        super(settings);
        this.type = element;
    }

    public Element getElement() {
        return this.type;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            if (ArchonUtil.getSpells(player).isEmpty()) {
                player.sendMessage(new TranslatableText("text.archon.invalid_spell").formatted(Formatting.RED), true);
                return TypedActionResult.pass(stack);
            }

            Spell current = getCurrentSpell(stack, player);
            if (player.isSneaking()) {
                cycleSpells(stack, player);
                player.sendMessage(new TranslatableText(getCurrentSpell(stack, player).getTranslationKey()).formatted(Formatting.GREEN), true);
                return TypedActionResult.success(stack);

            } else if (current != null && current.canCast(world, player, stack)) {
                current.cast(world, player, stack);
                ArcaneEnchantment.applyArcane(player, stack, current.getManaCost());
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    @Nullable
    public Spell getCurrentSpell(ItemStack stack, PlayerEntity player) {
        if (stack.getOrCreateSubNbt(Archon.MODID).contains("CurrentSpell")) {
            String name = stack.getOrCreateSubNbt(Archon.MODID).getString("CurrentSpell");
            return Archon.SPELL.get(new Identifier(name));
        }

        if (getSpells(player).isEmpty()) return null;
        Spell spell = getSpells(player).get(0);
        stack.getOrCreateSubNbt(Archon.MODID).putString("CurrentSpell", Archon.SPELL.getId(spell).toString());
        return spell;
    }

    public List<Spell> cycleSpells(ItemStack stack, PlayerEntity player) {
        Collections.rotate(getSpells(player), 1);
        stack.getOrCreateSubNbt(Archon.MODID).putString("CurrentSpell", Archon.SPELL.getId(getSpells(player).get(0)).toString());
        return getSpells(player);
    }

    public List<Spell> getSpells(PlayerEntity player) {
        return ArchonUtil.getSpells(player);
    }
}
