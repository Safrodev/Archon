package safro.archon.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Spell;
import safro.archon.registry.CriteriaRegistry;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class TomeItem extends Item {
    private final Spell spell;

    public TomeItem(Spell spell, Settings settings) {
        super(settings);
        this.spell = spell;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient) {
            if (ArchonUtil.addSpell(player, this.spell)) {
                player.sendMessage(Text.translatable("text.archon.learn_spell").setStyle(Style.EMPTY.withColor(this.spell.getElement().getColor())), false);
                if (player instanceof ServerPlayerEntity) {
                    CriteriaRegistry.LEARN_SPELL_CRITERION.trigger((ServerPlayerEntity)player);
                }
                stack.decrement(1);
                return TypedActionResult.success(stack);
            } else
                player.sendMessage(Text.translatable("text.archon.known_spell"), false);
        }
        return TypedActionResult.pass(stack);
    }

    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public String getTranslationKey() {
        return "item.archon.tome";
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(this.spell.getTranslationKey()).formatted(Formatting.GRAY));
    }
}
