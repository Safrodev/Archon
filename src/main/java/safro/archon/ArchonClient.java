package safro.archon;

import net.fabricmc.api.ClientModInitializer;
import safro.archon.client.ClientEvents;
import safro.archon.network.NetworkManager;
import safro.archon.registry.ClientRegistry;

public class ArchonClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetworkManager.initClient();
        ClientEvents.init();
        ClientRegistry.init();
    }
}
