package safro.archon.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.Archon;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.api.spell.SpellAttributable;
import safro.archon.enchantment.ArcaneEnchantment;
import safro.archon.registry.SpellRegistry;
import safro.archon.util.ArchonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WandItem extends Item implements SpellAttributable {
    private final Element type;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public WandItem(Element element, int power, Settings settings) {
        super(settings);
        this.type = element;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        addPower(builder, element, power);
        this.attributeModifiers = builder.build();
    }

    @Override
    public Element getElement() {
        return this.type;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!world.isClient) {
            if (getSpells(player).isEmpty()) {
                player.sendMessage(Text.translatable("text.archon.invalid_spell").formatted(Formatting.RED), true);
                return TypedActionResult.pass(stack);
            }

            Spell current = getCurrentSpell(stack, player);
            if (player.isSneaking()) {
                this.cycleSpells(stack, player);
                player.sendMessage(Text.translatable(getCurrentSpell(stack, player).getTranslationKey()).formatted(Formatting.GREEN), true);
                return TypedActionResult.success(stack);

            } else if (current != null && !current.isBlockCasted() && current.canCast(world, player, stack)) {
                current.cast(world, player, SpellPower.getSpellPower(this.getElement().getSchool(), player), stack);
                ArcaneEnchantment.applyArcane(player, stack, current.getManaCost());

                if (current.getCastSound() != null) {
                    world.playSound(null, player.getBlockPos(), current.getCastSound(), SoundCategory.PLAYERS, 0.9F, 1.0F);
                }
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        PlayerEntity player = context.getPlayer();
        Spell current = getCurrentSpell(stack, player);

        if (!context.getWorld().isClient() && player != null) {
            ServerWorld world = (ServerWorld) context.getWorld();
            if (current != null && current.isBlockCasted() && ArchonUtil.canRemoveMana(player, current.getManaCost())) {
                if (current.castOnBlock(world, context.getBlockPos(), player, stack) == ActionResult.SUCCESS) {
                    ArcaneEnchantment.applyArcane(player, stack, current.getManaCost());

                    if (current.getCastSound() != null) {
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), current.getCastSound(), SoundCategory.PLAYERS, 0.9F, 1.0F);
                    }
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public int getEnchantability() {
        return 3;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Nullable
    public Spell getCurrentSpell(ItemStack stack, PlayerEntity player) {
        if (stack.getOrCreateSubNbt(Archon.MODID).contains("CurrentSpell")) {
            String name = stack.getOrCreateSubNbt(Archon.MODID).getString("CurrentSpell");
            return SpellRegistry.REGISTRY.get(new Identifier(name));
        }

        if (getSpells(player).isEmpty()) return null;
        Spell spell = getSpells(player).get(0);
        stack.getOrCreateSubNbt(Archon.MODID).putString("CurrentSpell", SpellRegistry.REGISTRY.getId(spell).toString());
        return spell;
    }

    public void cycleSpells(ItemStack stack, PlayerEntity player) {
        if (getSpells(player).size() > 1) {
            List<Spell> spells = ArchonUtil.getSpells(player);
            do {
                Collections.rotate(spells, 1);
                stack.getOrCreateSubNbt(Archon.MODID).putString("CurrentSpell", SpellRegistry.REGISTRY.getId(spells.get(0)).toString());
            } while (getCurrentSpell(stack, player).getElement() != this.getElement());
        }
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
            Spell spell = SpellRegistry.REGISTRY.get(new Identifier(name));
            tooltip.add(Text.translatable("text.archon.current_spell", Text.translatable(spell.getTranslationKey()).getString()).formatted(Formatting.GRAY));
            tooltip.add(ArchonUtil.createManaText(spell.getManaCost(), false));
        } else {
            tooltip.add(Text.translatable("text.archon.none").formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
