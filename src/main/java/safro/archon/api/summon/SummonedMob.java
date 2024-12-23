package safro.archon.api.summon;

import net.minecraft.entity.player.PlayerEntity;

public interface SummonedMob {
    void archon$setLifetime(int seconds);

    void archon$setOwner(String uuid);

    boolean archon$isOwner(PlayerEntity player);
}
