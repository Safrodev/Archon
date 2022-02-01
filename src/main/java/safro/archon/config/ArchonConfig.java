package safro.archon.config;

import draylar.omegaconfig.api.Comment;
import draylar.omegaconfig.api.Config;
import draylar.omegaconfig.api.Syncing;

@Syncing
public class ArchonConfig implements Config {

    @Comment(
            """
                    Sets the position of the mana display.
                    The x-offset is subtracted from the x pos of the middle of the screen. (Ex: 0 would make it right in the middle)
                    The y-offset is subtracted from the y pos at the bottom of the screen. (Ex: 0 would make it at the very bottom of the screen)
                    X-Offset Default: 140
                    Y-Offset Default: 15
            """
    )
    public int mana_xoffset = 140;
    public int mana_yoffset = 15;

    @Comment(
            """
                    Chance for a soul to drop when killing players and creatures using a soul scythe (bosses always drop a soul)
                    The number should follow these bounds: 0 <= x <= 1.0 and end with "F"
                    Default: 0.05F
            """
    )
    public float soulDropChance = 0.05F;

    @Comment(
            """
                    Weight/Chance for the Wizard Village House to spawn in villages
                    Default: 10
            """
    )
    public int wizard_village_weight = 10;

    @Comment(
            """
                    The vein size of all element nodes except sky.
                    Default: 4
            """
    )
    public int nodeVeinSize = 4;

    @Comment(
            """
                    The amount of veins per chunk of all element nodes except sky.
                    Default: 4
            """
    )
    public int nodeChunkRate = 4;

    @Comment(
            """
                    The Chance for Sky Node Features to spawn (lower is more common, higher is more rare)
                    Default: 400
            """
    )
    public int skyNodeChance = 400;

    @Comment(
            """
                    The Chance for Mana Berry Bushes to spawn
                    Default: 32
            """)
    public int manaBerryBushChance = 32;

    @Comment(
            """
                    The Spacing and Separation of Spire structures in your world. NOTE: The Spacing value MUST be larger than the Separation value.
                    Default Spacing: 22
                    Default Separation: 20
            """
    )
    public int spire_spacing = 22;
    public int spire_separation = 20;

    @Comment(
            """
                    Determines whether the a sound should be played when using a channeler
                    Client-Sided, Accepts "true" or "false"
                    Default: true
            """
    )
    public boolean play_channel_sound = true;

    @Override
    public String getName() {
        return "archon";
    }

    @Override
    public String getExtension() {
        return "json5";
    }
}
