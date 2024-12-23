package safro.archon.summon;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import safro.archon.api.summon.Summon;
import safro.archon.api.summon.SummonedMob;
import safro.archon.util.SummonHelper;

public class SilverSwarmSummon implements Summon {

    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        for (int i = 0; i < 10; i++) {
            SilverfishEntity entity = EntityType.SILVERFISH.create(world);
            entity.setTarget(player.getAttacking());
            ((SummonedMob)entity).archon$setOwner(player.getUuidAsString());
            double x = player.getX() + MathHelper.nextDouble(player.getRandom(), -3.0, 3.0);
            double z = player.getZ() + MathHelper.nextDouble(player.getRandom(), -3.0, 3.0);
            entity.refreshPositionAndAngles(x, player.getY(), z, player.getYaw(), player.getPitch());
            SummonHelper.setScaledLife(entity, soulPower, 10);
            SummonHelper.addStatScaling(entity, soulPower);
            world.spawnEntity(entity);
            SummonHelper.createParticlesAround(entity, world);
        }
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.silver_swarm";
    }
}
