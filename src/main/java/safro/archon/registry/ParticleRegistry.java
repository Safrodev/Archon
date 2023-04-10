package safro.archon.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import safro.archon.Archon;

public class ParticleRegistry {
    public static final DefaultParticleType WATER_BALL = register("water_ball", false);
    public static final DefaultParticleType INFERNO_LASER = register("inferno_laser", true);

    private static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(Archon.MODID, name), FabricParticleTypes.simple(alwaysShow));
    }

    public static void init() {
    }
}
