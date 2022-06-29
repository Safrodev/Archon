package safro.archon.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.client.ClientEvents;

public class ShakePacket {
    public static final Identifier ID = new Identifier(Archon.MODID, "shake_screen");

    public static void send(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        ServerPlayNetworking.send(player, ID, buf);
    }

    public static void receive(MinecraftClient client) {
        if (client.getCameraEntity() instanceof PlayerEntity player) {
            ClientEvents.addShake(player);
        }
    }
}
