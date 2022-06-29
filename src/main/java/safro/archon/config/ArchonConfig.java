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
            The vein size of all element nodes except sky.
            Default: 4
            """
    )
    @Syncing
    public int nodeVeinSize = 4;

    @Comment(
            """
            The amount of veins per chunk of all element nodes except sky.
            Default: 4
            """
    )
    @Syncing
    public int nodeChunkRate = 4;

    @Comment(
            """
            The minimum y-height that Earth nodes can generate at.
            Default: -64
            """
    )
    @Syncing
    public int earthNodeMin = -64;

    @Comment(
            """
            The maximum y-height that Earth nodes can generate at.
            Default: 0
            """
    )
    @Syncing
    public int earthNodeMax = 0;

    @Comment(
            """
            The minimum y-height that Warth nodes can generate at.
            Default: 27
            """
    )
    @Syncing
    public int waterNodeMin = 27;

    @Comment(
            """
            The maximum y-height that Water nodes can generate at.
            Default: 40
            """
    )
    @Syncing
    public int waterNodeMax = 40;

    @Comment(
            """
            The minimum y-height that End nodes can generate at.
            Default: 8
            """
    )
    @Syncing
    public int endNodeMin = 8;

    @Comment(
            """
            The maximum y-height that End nodes can generate at.
            Default: 180
            """
    )
    @Syncing
    public int endNodeMax = 180;

    @Comment(
            """
            The Chance for Sky Node Features to spawn (lower is more common, higher is more rare)
            Default: 400
            """
    )
    @Syncing
    public int skyNodeChance = 400;

    @Comment(
            """
            The Chance for Mana Berry Bushes to spawn (lower is more common, higher is more rare)
            Default: 384
            """
    )
    @Syncing
    public int manaBerryBushChance = 384;

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

    @Override
    public String getName() {
        return "archon";
    }

    @Override
    public String getExtension() {
        return "json5";
    }
}
