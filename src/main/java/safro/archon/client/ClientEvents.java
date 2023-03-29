package safro.archon.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import safro.archon.Archon;
import safro.archon.network.InfernalImplodePacket;
import safro.archon.registry.ClientRegistry;

import java.util.ArrayList;
import java.util.List;

public class ClientEvents {
    private static final List<PlayerEntity> SHAKE_QUEUE = new ArrayList<>();
    private static int tick = 0;
    private static boolean pressed = false;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
           if (!SHAKE_QUEUE.isEmpty()) {
               if (tick > 0) {
                   SHAKE_QUEUE.forEach(player -> {
                       float n = tick * 0.5F;
                       float m = tick % 2 == 0 ? n : -n;
                       player.setPitch(player.getPitch() + m);
                   });
                   --tick;
               }
           }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ClientRegistry.IC_KEY.wasPressed()) {
                if (!pressed) {
                    InfernalImplodePacket.send();
                }
                pressed = true;
            } else {
                pressed = false;
            }
        });
    }

    public static void addShake(PlayerEntity player) {
        if (Archon.CONFIG.enableScreenShake) {
            SHAKE_QUEUE.add(player);
            player.setPitch(player.getPitch() - 6);
            tick = 60;
        }
    }
}
