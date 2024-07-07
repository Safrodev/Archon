package safro.archon.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import safro.archon.Archon;
import safro.archon.client.particle.SpellParticleEffect;

import java.util.function.Function;

public class ParticleRegistry {
    public static final ParticleType<SpellParticleEffect> SPELL = register("spell", true, SpellParticleEffect.FACTORY, type -> SpellParticleEffect.CODEC);
    public static final DefaultParticleType WATER_BALL = register("water_ball", false);
    public static final DefaultParticleType INFERNO_LASER = register("inferno_laser", true);

    private static DefaultParticleType register(String name, boolean alwaysShow) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(Archon.MODID, name), FabricParticleTypes.simple(alwaysShow));
    }

    private static <T extends ParticleEffect> ParticleType<T> register(String name, boolean alwaysShow, ParticleEffect.Factory<T> factory, Function<ParticleType<T>, Codec<T>> codecGetter) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(Archon.MODID, name), new ParticleType<T>(alwaysShow, factory) {
            @Override
            public Codec<T> getCodec() {
                return codecGetter.apply(this);
            }
        });
    }

    public static void init() {
    }
}
