package safro.archon.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import safro.archon.registry.ParticleRegistry;

public record SpellParticleEffect(float r, float g, float b, float size) implements ParticleEffect {
    public static final Codec<SpellParticleEffect> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(p -> p.r),
                    Codec.FLOAT.fieldOf("g").forGetter(p -> p.g),
                    Codec.FLOAT.fieldOf("b").forGetter(p -> p.b),
                    Codec.FLOAT.fieldOf("size").forGetter(p -> p.size)
            ).apply(instance, SpellParticleEffect::new));
    public static final ParticleEffect.Factory<SpellParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
        public SpellParticleEffect read(ParticleType<SpellParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
            stringReader.expect(' ');
            String[] vars = stringReader.readString().split(",");
            return new SpellParticleEffect(Float.parseFloat(vars[0]), Float.parseFloat(vars[1]), Float.parseFloat(vars[2]), Float.parseFloat(vars[3]));
        }

        public SpellParticleEffect read(ParticleType<SpellParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return new SpellParticleEffect(packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat(), packetByteBuf.readFloat());
        }
    };

    @Override
    public ParticleType<SpellParticleEffect> getType() {
        return ParticleRegistry.SPELL;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.r);
        buf.writeFloat(this.g);
        buf.writeFloat(this.b);
        buf.writeFloat(this.size);
    }

    @Override
    public String asString() {
        return Registries.PARTICLE_TYPE.getId(this.getType()) + " " + this.r + "," + this.g + "," + this.b + "," + this.size;
    }
}
