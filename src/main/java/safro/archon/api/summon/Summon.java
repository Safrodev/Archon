package safro.archon.api.summon;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public interface Summon {
    /**
     * Executed when the player attempts to cast this Summon with an Undead Staff
     * @param world the server world
     * @param player the player summoning
     * @param soulPower the soul power of the Undead Staff
     */
    void onSummon(ServerWorld world, PlayerEntity player, int soulPower);

    /**
     * Translation Key for this Summon used to display on an Undead Staff
     * @return String key used in lang files
     */
    String getTranslationKey();
}
