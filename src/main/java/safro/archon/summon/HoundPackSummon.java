package safro.archon.summon;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import safro.archon.api.summon.Summon;
import safro.archon.util.SummonHelper;

public class HoundPackSummon implements Summon {

    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        for (int i = 0; i < 5; i++) {
            WolfEntity wolf = EntityType.WOLF.create(world);
            wolf.setOwner(player);
            wolf.setAngerTime(600);
            SummonHelper.spawnAndScale(world, player, wolf, soulPower, 20);
        }
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.hound_pack";
    }
}
