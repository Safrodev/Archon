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
            An integer value that is factored into the Soul Power scaling function to calculate soul power from number of souls in a Staff of Undead
            The greater the value (ex. 50), the HARDER it will be to increase your Soul Power (more souls needed for each level)
            The smaller the value (ex. 5), the EASIER it will be to increase your Soul Power (less souls needed for each level)
            The number should follow these bounds: 1 <= x <= 100
            Default: 10
            """
    )
    @Syncing
    public int soulPowerScaling = 10;

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

    @Comment("""
             The max amount of experience the standard experience pouch can hold.
             Default: 550
            """)
    @Syncing
    public int experiencePouchMax = 550;

    @Comment("""
             The max amount of experience the super experience pouch can hold.
             Default: 2920
            """)
    @Syncing
    public int superExperiencePouchMax = 2920;

    @Override
    public String getName() {
        return "archon";
    }

    @Override
    public String getExtension() {
        return "json5";
    }
}
