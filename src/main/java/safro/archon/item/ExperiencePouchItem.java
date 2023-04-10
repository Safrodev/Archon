package safro.archon.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.Archon;
import safro.archon.client.screen.ExperiencePouchScreenHandler;
import safro.archon.registry.ItemRegistry;

import java.util.List;

public class ExperiencePouchItem extends Item implements NamedScreenHandlerFactory {
    private final int max;

    public ExperiencePouchItem(int maxLevel, Settings settings) {
        super(settings);
        this.max = maxLevel;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!world.isClient && player.getMainHandStack().isOf(this)) {
            player.openHandledScreen(this);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    public static int getExperience(ItemStack stack) {
        return stack.getOrCreateSubNbt(Archon.MODID).getInt("xp");
    }

    public int getMaxXp() {
        return max;
    }

    public static void addExperience(ItemStack stack, int amount) {
        stack.getOrCreateSubNbt(Archon.MODID).putInt("xp", getExperience(stack) + amount);
    }

    public static void grantExperience(ItemStack stack, ServerPlayerEntity player, int amount) {
        player.addExperience(amount);
        int total = getExperience(stack) - amount;
        stack.getOrCreateSubNbt(Archon.MODID).putInt("xp", total);
    }

    public static boolean canAddXp(ServerPlayerEntity player, ItemStack stack, int amount) {
        if (stack.getItem() instanceof ExperiencePouchItem pouch) {
            if (getTotalXp(player) >= amount) {
                return getExperience(stack) < pouch.getMaxXp();
            }
        }
        return false;
    }

    // Credit to XP Tome mod
    private static int getTotalXp(ServerPlayerEntity player) {
        int level = player.experienceLevel;
        if (level == 0) return 0;
        if (level <= 15) return sum(level, 7, 2);
        if (level <= 30) return 315 + sum(level - 15, 37, 5);
        int i = 1395 + sum(level - 30, 112, 9);
        return (int) (i + (player.experienceProgress * player.getNextLevelExperience()));
    }

    private static int sum(int n, int a0, int d) {
        return n * (2 * a0 + (n - 1) * d) / 2;
    }

    public boolean hasGlint(ItemStack stack) {
        return stack.isOf(ItemRegistry.SUPER_EXPERIENCE_POUCH);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal(getExperience(stack) + "/" + getMaxXp()).formatted(Formatting.GREEN));
    }

    @Override
    public Text getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        PropertyDelegate propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return index == 0 ? getExperience(stack) : 0;
            }

            public void set(int index, int value) {
            }

            public int size() {
                return 1;
            }
        };
        return new ExperiencePouchScreenHandler(syncId, inv, propertyDelegate);
    }
}
