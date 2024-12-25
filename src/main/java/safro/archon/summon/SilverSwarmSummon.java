package safro.archon.summon;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import safro.archon.api.summon.Summon;
import safro.archon.util.SummonHelper;

public class SilverSwarmSummon implements Summon {

    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        for (int i = 0; i < 10; i++) {
            SilverfishEntity entity = EntityType.SILVERFISH.create(world);
            if (entity != null) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 2, SummonHelper.getScaledLife(soulPower, 10) * 20, true, false, false));
                SummonHelper.spawnAndScale(world, player, entity, soulPower, 10);
            }
        }
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.silver_swarm";
    }
}
