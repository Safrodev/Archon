package safro.archon.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import safro.archon.Archon;
import safro.archon.registry.ParticleRegistry;

public class InfernoLaserPacket {
    public static final Identifier ID = new Identifier(Archon.MODID, "inferno_laser");

    public static void send(ServerWorld world, Vec3d boss, Vec3d target) {
        PacketByteBuf buf = PacketByteBufs.create();
        writeVec(buf, boss);
        writeVec(buf, target);
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, BlockPos.ofFloored(boss))) {
            ServerPlayNetworking.send(player, ID, buf);
        }
    }

    public static void receive(MinecraftClient client, PacketByteBuf buf) {
        Vec3d boss = readVec(buf);
        Vec3d target = readVec(buf);
        double distance = boss.distanceTo(target);
        if (client.world != null) {
            for (double i = 0; i < distance; i += 2) {
                double position = ((i - 1) + 0.35408622399424106D * 2) / distance;
                double x = boss.x + (target.x - boss.x) * position;
                double y = boss.y + (target.y - boss.y) * position;
                double z = boss.z + (target.z - boss.z) * position;
                Vec3d vec3d = new Vec3d(x, y, z);
                client.world.addParticle(ParticleRegistry.INFERNO_LASER, true, vec3d.x, vec3d.y, vec3d.z, 2.0D, 0.0D, 0.0D);
            }
            client.world.playSound(boss.x, boss.y, boss.z, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 1.0F, 0.8F, true);
        }
    }

    private static void writeVec(PacketByteBuf buf, Vec3d vec3d) {
        buf.writeDouble(vec3d.x);
        buf.writeDouble(vec3d.y);
        buf.writeDouble(vec3d.z);
    }

    private static Vec3d readVec(PacketByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        return new Vec3d(x, y, z);
    }
}
