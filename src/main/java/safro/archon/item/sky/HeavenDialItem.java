package safro.archon.item.sky;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.registry.MiscRegistry;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class HeavenDialItem extends Item {

    public HeavenDialItem(Settings settings) {
        super(settings);
    }

    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(30D));
        if (list.size() > 0) {
            for (LivingEntity e : list) {
                if (e instanceof PlayerEntity || ArchonUtil.isOwnedBy(player, e) || e instanceof VillagerEntity || e instanceof IronGolemEntity) {
                    this.heal(world, e);
                }
            }
            return true;
        }
        return false;
    }

    private void heal(World world, LivingEntity entity) {
        if (!world.isClient) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 1));
            entity.heal(20);
        }
        for (int i = 0; i < 15; i++) {
            world.addParticle(ParticleTypes.HAPPY_VILLAGER, entity.getParticleX(1.0D), entity.getRandomBodyY() + 0.5D, entity.getParticleZ(1.0D), 0.0D, 0.0D, 0.0D);
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (ArchonUtil.canRemoveMana(player, 80) && activate(world, player, stack, hand)) {
            if (EnchantmentHelper.getLevel(MiscRegistry.ARCANE, stack) >= 1) {
                int removed = (int) (80 * 0.2);
                ArchonUtil.get(player).removeMana(80 - removed);
            } else {
                ArchonUtil.get(player).removeMana(80);
            }
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("text.archon.mana_cost", 80).formatted(Formatting.BLUE));
    }
}
