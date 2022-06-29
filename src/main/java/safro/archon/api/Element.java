package safro.archon.api;

import net.minecraft.util.Formatting;

public enum Element {
    FIRE(Formatting.RED),
    WATER(Formatting.BLUE),
    EARTH(13601368),
    SKY(16711097),
    END(Formatting.LIGHT_PURPLE);

    private final int color;

    Element(int color) {
        this.color = color;
    }

    Element(Formatting color) {
        this.color = checkColor(color);
    }

    public int getColor() {
        return this.color;
    }

    private static int checkColor(Formatting formatting) {
        if (formatting.getColorValue() == null) {
            throw new IllegalArgumentException("Formatting color value must not be null!");
        }
        return formatting.getColorValue();
    }
}
