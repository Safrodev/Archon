package safro.archon.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.Archon;
import safro.archon.registry.ItemRegistry;

import java.util.List;

public class ExperiencePouchItem extends Item {
    private final int max;

    public ExperiencePouchItem(int maxLevel, Settings settings) {
        super(settings);
        this.max = maxLevel;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.isSneaking()) {
            if (!world.isClient && canAddXp(player, stack)) {
                addExperience(stack);
                removeExperience(player);
                return TypedActionResult.success(stack);
            }
        } else {
            if (!world.isClient && getExperience(stack) > 0) {
                grantExperience(stack, player);
                stack.getOrCreateSubNbt(Archon.MODID).putInt("xp", 0);
            }
        }
        return TypedActionResult.pass(stack);
    }

    public int getExperience(ItemStack stack) {
        return stack.getOrCreateSubNbt(Archon.MODID).getInt("xp");
    }

    public int getMaxXp() {
        return max;
    }

    public void addExperience(ItemStack stack) {
        stack.getOrCreateSubNbt(Archon.MODID).putInt("xp", getExperience(stack) + 10);
    }

    public void grantExperience(ItemStack stack, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.addExperience(getExperience(stack));
        }
    }

    public boolean canAddXp(PlayerEntity player, ItemStack stack) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (serverPlayer.totalExperience >= 10) {
                return getExperience(stack) < getMaxXp();
            }
        }
        return false;
    }

    public void removeExperience(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.addExperience(-10);
        }
    }

    public boolean hasGlint(ItemStack stack) {
        return stack.isOf(ItemRegistry.SUPER_EXPERIENCE_POUCH);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal(getExperience(stack) + "/" + getMaxXp()).formatted(Formatting.GREEN));
    }
}
