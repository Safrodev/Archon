package safro.archon.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;

public class WeatherSpell extends Spell {
    private final boolean rain;

    public WeatherSpell(Element type, int manaCost, boolean rain) {
        super(type, manaCost);
        this.rain = rain;
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        if (world instanceof ServerWorld serverWorld) {
            if (this.rain) {
                int duration = ServerWorld.RAIN_WEATHER_DURATION_PROVIDER.get(serverWorld.getRandom());
                serverWorld.setWeather(0, duration, true, false);
            } else {
                int duration = ServerWorld.CLEAR_WEATHER_DURATION_PROVIDER.get(serverWorld.getRandom());
                serverWorld.setWeather(duration, 0, false, false);
            }
        }
    }

    @Override
    @Nullable
    public SoundEvent getCastSound() {
        return null;
    }
}
