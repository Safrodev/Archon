package safro.archon.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import safro.archon.Archon;

public class ParticlePacket {
    public static final Identifier ID = new Identifier(Archon.MODID, "spawn_particles");

    public static void send(Entity entity, ParticleEffect effect, double x, double y, double z, double vX, double vY, double vZ) {
        PlayerLookup.tracking(entity).forEach(player -> send(player, effect, x, y, z, vX, vY, vZ));
    }

    public static void send(ServerPlayerEntity player, ParticleEffect effect, double x, double y, double z, double vX, double vY, double vZ) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeRegistryValue(Registry.PARTICLE_TYPE, effect.getType());
        effect.write(buf);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(vX);
        buf.writeDouble(vY);
        buf.writeDouble(vZ);
        ServerPlayNetworking.send(player, ID, buf);
    }

    public static void receive(MinecraftClient client, PacketByteBuf buf) {
        if (client.world != null) {
            ParticleEffect effect = readEffect(buf, buf.readRegistryValue(Registry.PARTICLE_TYPE));
            client.world.addParticle(effect, buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }

    private static <T extends ParticleEffect> T readEffect(PacketByteBuf buf, ParticleType<T> type) {
        return type.getParametersFactory().read(type, buf);
    }
}
