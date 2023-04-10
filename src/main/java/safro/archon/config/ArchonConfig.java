package safro.archon.config;

import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;
import draylar.omegaconfig.api.Syncing;

@Syncing
public class ArchonConfig implements Config {

    @Comment(
            """
            Sets the position of the mana display; Client-sided
            The x-offset is subtracted from the x pos of the middle of the screen. (Ex: 0 would make it right in the middle)
            The y-offset is subtracted from the y pos at the bottom of the screen. (Ex: 0 would make it at the very bottom of the screen)
            X-Offset Default: 180
            Y-Offset Default: 15
            """
    )
    public int mana_xoffset = 180;
    public int mana_yoffset = 15;

    @Comment(
            """
            Determines whether mana should only display if you have a mana item in your hand.
            Client-Sided, Accepts "true" or "false"
            Default: false (always shows mana)
            """
    )
    public boolean displayManaWithItem = false;

    @Comment(
            """
            Chance for a soul to drop when killing players and creatures using a soul scythe (bosses always drop a soul)
            The number should follow these bounds: 0 <= x <= 1.0
            Default: 0.05 (1/20)
            """
    )
    @Syncing
    public float soulDropChance = 0.05F;

    @Comment(
            """
            Weight/Chance for the Wizard Village House to spawn in villages
            Default: 10
            """
    )
    @Syncing
    public int wizard_village_weight = 10;

    @Comment(
            """
            Determines whether the a sound should be played when using a channeler
            Client-Sided, Accepts "true" or "false"
            Default: true
            """
    )
    public boolean play_channel_sound = true;

    @Comment(
            """
            The chance that Harvesters will drop the bonus related to the mob
            The number should follow these bounds: 0 <= x <= 1.0
            Default: 0.05 (1/20)
            """
    )
    @Syncing
    public float harvester_chance = 0.05F;

    @Comment(
            """
            Whether screen shaking should be enabled or not. Used for players hit with the Rumble spell.
            Client-Sided, Accepts "true" or "false"
            Default: true
            """
    )
    public boolean enableScreenShake = true;

    @Comment(
            """
            Whether bonus spell critical damage should be added when Spell Power is installed
            Default: true
            """
    )
    @Syncing
    public boolean enableSpellPowerCompat = true;

    @Override
    public String getName() {
        return "archon";
    }

    @Override
    public String getExtension() {
        return "json5";
    }
}
