package safro.archon.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.Archon;
import safro.archon.api.summon.Summon;
import safro.archon.api.summon.SummonHandler;
import safro.archon.enchantment.ArcaneEnchantment;
import safro.archon.registry.ItemRegistry;
import safro.archon.util.ArchonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UndeadStaffItem extends Item {

    public UndeadStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof ServerPlayerEntity player) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                if (player.getInventory().getStack(i).isOf(ItemRegistry.SOUL)) {
                    player.getInventory().getStack(i).decrement(1);
                    addSoul(stack);
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!world.isClient) {
            if (getSummons(stack).isEmpty()) {
                player.sendMessage(Text.translatable("text.archon.no_summons").formatted(Formatting.RED), true);
                return TypedActionResult.pass(stack);
            }

            Summon current = getCurrentSummon(stack);
            if (player.isSneaking()) {
                cycleSummons(stack);
                player.sendMessage(Text.translatable(getCurrentSummon(stack).getTranslationKey()).formatted(Formatting.GREEN), true);
                return TypedActionResult.success(stack);

            } else if (current != null && ArchonUtil.canRemoveMana(player, 100)) {
                int soulPower = getSoulPower(stack);
                current.onSummon((ServerWorld)world, player, soulPower);
                ArcaneEnchantment.applyArcane(player, stack, 100);
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_WARDEN_EMERGE, SoundCategory.PLAYERS, 0.9F, 1.0F);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    @Nullable
    public static Summon getCurrentSummon(ItemStack stack) {
        ArrayList<Summon> summons = getSummons(stack);
        return summons.isEmpty() ? null : summons.get(0);
    }

    public static void cycleSummons(ItemStack stack) {
        ArrayList<Summon> summons = getSummons(stack);
        if (summons.size() > 1) {
            Collections.rotate(summons, 1);
            saveSummons(summons, stack);
        }
    }

    public static void addSummon(Summon summon, ItemStack stack) {
        ArrayList<Summon> list = getSummons(stack);
        list.add(summon);
        saveSummons(list, stack);
    }

    public static ArrayList<Summon> getSummons(ItemStack stack) {
        ArrayList<Summon> list = new ArrayList<>();
        NbtList nbtList = stack.getOrCreateNbt().getList("Summons", NbtCompound.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound tag = nbtList.getCompound(i);
            list.add(SummonHandler.fromString(tag.getString("Id")));
        }
        return list;
    }

    private static void saveSummons(ArrayList<Summon> summons, ItemStack stack) {
        NbtList nbtList = new NbtList();
        for (Summon s : summons) {
            NbtCompound tag = new NbtCompound();
            String id = SummonHandler.getId(s);
            if (id != null) {
                tag.putString("Id", id);
                nbtList.add(tag);
            } else {
                Archon.LOGGER.error("Skipping saving " + s + " as it does not have a registered identifier.");
            }
        }
        stack.getOrCreateNbt().put("Summons", nbtList);
    }

    public static int getSouls(ItemStack stack) {
        return stack.getOrCreateNbt().contains("Souls") ? stack.getOrCreateNbt().getInt("Souls") : 0;
    }

    public static void addSoul(ItemStack stack) {
        stack.getOrCreateNbt().putInt("Souls", getSouls(stack) + 1);
    }

    public static int getSoulPower(ItemStack stack) {
        if (stack.getOrCreateNbt().contains("Souls") && getSouls(stack) >= 5) {
            int souls = getSouls(stack);
            double scaled = Math.log((double) souls / 5.0) / Math.log(1.1);
            return (int) Math.ceil(1.0 + scaled);
        } else {
            return 0;
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int souls = getSouls(stack);
        int power = getSoulPower(stack);
        tooltip.add(Text.translatable("text.archon.undead.power", power).formatted(Formatting.DARK_AQUA));
        tooltip.add(Text.translatable("text.archon.undead.souls", souls).formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public int getEnchantability() {
        return 3;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
