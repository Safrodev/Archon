package safro.archon.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkManager {

    public static void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(ExperienceChangePacket.ID, ((server, player, handler, buf, responseSender) -> ExperienceChangePacket.receive(player, buf)));
        ServerPlayNetworking.registerGlobalReceiver(InfernalImplodePacket.ID, (((server, player, handler, buf, responseSender) -> InfernalImplodePacket.receive(player))));
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ShakePacket.ID, ((client, handler, buf, responseSender) -> ShakePacket.receive(client)));
        ClientPlayNetworking.registerGlobalReceiver(InfernoLaserPacket.ID, (((client, handler, buf, responseSender) -> InfernoLaserPacket.receive(client, buf))));
    }
}
