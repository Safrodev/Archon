package safro.archon.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
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
    private final int maxLevel;

    public ExperiencePouchItem(int maxLevel, Settings settings) {
        super(settings);
        this.maxLevel = maxLevel;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.isSneaking()) {
            if (!world.isClient && canAddLevel(player, stack)) {
                addLevel(stack);
                removeXp(player);
                return TypedActionResult.success(stack);
            }
        } else {
            if (!world.isClient && getLevels(stack) > 0) {
                grantLevels(stack, player);
                stack.getOrCreateSubNbt(Archon.MODID).putInt("xp", 0);
            }
        }
        return TypedActionResult.pass(stack);
    }

    public int getLevels(ItemStack stack) {
        return stack.getOrCreateSubNbt(Archon.MODID).getInt("xp");
    }

    public int getMaxLevels() {
        return maxLevel;
    }

    public void addLevel(ItemStack stack) {
        stack.getOrCreateSubNbt(Archon.MODID).putInt("xp", getLevels(stack) + 1);
    }

    public void grantLevels(ItemStack stack, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.addExperienceLevels(getLevels(stack));
        }
    }

    public boolean canAddLevel(PlayerEntity player, ItemStack stack) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            if (serverPlayer.experienceLevel >= 1) {
                return getLevels(stack) < getMaxLevels();
            }
        }
        return false;
    }

    public void removeXp(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.setExperienceLevel(serverPlayer.experienceLevel - 1);
        }
    }

    public boolean hasGlint(ItemStack stack) {
        return stack.isOf(ItemRegistry.SUPER_EXPERIENCE_POUCH);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new LiteralText(getLevels(stack) + "/" + getMaxLevels()).formatted(Formatting.GREEN));
    }
}
