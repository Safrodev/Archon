package safro.archon.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class NetworkManager {

    public static void initServer() {

    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ShakePacket.ID, ((client, handler, buf, responseSender) -> ShakePacket.receive(client)));
    }
}
