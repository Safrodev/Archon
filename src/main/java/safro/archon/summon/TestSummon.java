package safro.archon.summon;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import safro.archon.api.summon.Summon;

public class TestSummon implements Summon {

    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        HuskEntity husk = EntityType.HUSK.create(world);
        husk.refreshPositionAndAngles(player.getBlockPos().north(), 0.0F, 0.0F);
        world.spawnEntity(husk);
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.test";
    }
}
