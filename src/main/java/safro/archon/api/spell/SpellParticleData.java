package safro.archon.api.spell;

public record SpellParticleData(float red, float green, float blue, float size) {

    public static SpellParticleData of(int red, int green, int blue, float size) {
        return new SpellParticleData((float)red / 255.0F, (float)green / 255.0F, (float)blue / 255.0F, size);
    }

    public static SpellParticleData of(int red, int green, int blue) {
        return new SpellParticleData((float)red / 255.0F, (float)green / 255.0F, (float)blue / 255.0F, 0.5F);
    }
}
