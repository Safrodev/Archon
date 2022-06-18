package safro.archon.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
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
import safro.archon.registry.ComponentsRegistry;
import safro.archon.util.ArchonUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
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
            if (getSpells(player).isEmpty()) {
                player.sendMessage(new TranslatableText("text.archon.invalid_spell").formatted(Formatting.RED), true);
                return TypedActionResult.pass(stack);
            }

            Spell current = getCurrentSpell(stack, player);
            if (player.isSneaking()) {
                ComponentsRegistry.SPELL_COMPONENT.get(player).setSpells(cycleSpells(stack, player));
                player.sendMessage(new TranslatableText(getCurrentSpell(stack, player).getTranslationKey()).formatted(Formatting.GREEN), true);
                return TypedActionResult.success(stack);

            } else if (current != null && current.canCast(world, player, stack)) {
                current.cast(world, player, stack);
                ArcaneEnchantment.applyArcane(player, stack, current.getManaCost());

                if (current.getCastSound() != null) {
                    world.playSound(null, player.getBlockPos(), current.getCastSound(), SoundCategory.PLAYERS, 0.9F, 1.0F);
                }
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
        List<Spell> spells = getSpells(player);
        if (spells.size() > 1) {
            Collections.rotate(spells, 1);
            ComponentsRegistry.SPELL_COMPONENT.sync(player);
            stack.getOrCreateSubNbt(Archon.MODID).putString("CurrentSpell", Archon.SPELL.getId(spells.get(0)).toString());
        }
        return spells;
    }

    public ArrayList<Spell> getSpells(PlayerEntity player) {
        ArrayList<Spell> list = new ArrayList<>();
        for (Spell spell : ArchonUtil.getSpells(player)) {
            if (spell.getElement() == this.getElement()) {
                list.add(spell);
            }
        }
        return list;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getOrCreateSubNbt(Archon.MODID).contains("CurrentSpell")) {
            String name = stack.getOrCreateSubNbt(Archon.MODID).getString("CurrentSpell");
            Spell spell = Archon.SPELL.get(new Identifier(name));
            tooltip.add(new TranslatableText(spell.getTranslationKey()).formatted(Formatting.GRAY));
        } else
            tooltip.add(new TranslatableText("text.archon.none").formatted(Formatting.GRAY));
    }
}
