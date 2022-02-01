package safro.archon;

import net.fabricmc.api.ClientModInitializer;
import safro.archon.registry.ClientRegistry;

public class ArchonClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientRegistry.init();
    }
}
