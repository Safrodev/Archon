package safro.archon.item.end;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.item.ManaItem;

import java.util.List;

public class SeekingAmuletItem extends ManaItem {

    public SeekingAmuletItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getManaCost() {
        return 100;
    }

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        GlobalPos pos = getStored(stack);
        if (!world.isClient() && !player.isSneaking() && pos != null) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            MinecraftServer server = ((ServerWorld) world).getServer();
            ServerWorld target = server.getWorld(pos.getDimension());
            if (target != null && World.isValid(pos.getPos())) {
                ChunkPos chunkPos = new ChunkPos(pos.getPos());
                target.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, serverPlayer.getId());
                serverPlayer.stopRiding();
                serverPlayer.teleport(target, pos.getPos().getX() + 0.5D, pos.getPos().getY(), pos.getPos().getZ() + 0.5D, serverPlayer.getYaw(), serverPlayer.getPitch());
                player.getItemCooldownManager().set(this, 30);
                return true;
            }
        }
        return false;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        PlayerEntity player = context.getPlayer();
        Vec3d postarget = context.getHitPos();
        if (!context.getWorld().isClient() && player != null && player.isSneaking()) {
            ServerWorld target = (ServerWorld) context.getWorld();
            BlockPos floored = BlockPos.ofFloored(postarget.getX(), postarget.getY() + 1.0D, postarget.getZ());
            GlobalPos pos = GlobalPos.create(target.getRegistryKey(), floored);
            stack.getOrCreateNbt().put("TeleportPos", writeGlobalPos(pos));
            player.sendMessage(Text.translatable("text.archon.set_pos", floored.getX(), floored.getY(), floored.getZ()), false);
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("text.archon.seeking_amulet1").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("text.archon.seeking_amulet2").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("text.archon.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Nullable
    private static GlobalPos getStored(ItemStack stack) {
        return stack.getOrCreateNbt().contains("TeleportPos") ? readGlobalPos(stack.getOrCreateNbt().getCompound("TeleportPos")) : null;
    }

    private static GlobalPos readGlobalPos(NbtCompound compound) {
        Identifier world = new Identifier(compound.getString("World"));
        BlockPos pos = NbtHelper.toBlockPos(compound.getCompound("Pos"));
        return GlobalPos.create(RegistryKey.of(RegistryKeys.WORLD, world), pos);
    }

    private static NbtCompound writeGlobalPos(GlobalPos pos) {
        NbtCompound compound = new NbtCompound();
        compound.putString("World", pos.getDimension().getValue().toString());
        compound.put("Pos", NbtHelper.fromBlockPos(pos.getPos()));
        return compound;
    }
}
