package safro.archon.summon;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import safro.archon.api.summon.Summon;
import safro.archon.api.summon.SummonedMob;
import safro.archon.util.SummonHelper;

public class HoundPackSummon implements Summon {

    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        for (int i = 0; i < 5; i++) {
            WolfEntity wolf = EntityType.WOLF.create(world);
            wolf.setOwner(player);
            ((SummonedMob)wolf).archon$setOwner(player.getUuidAsString());
            wolf.setTarget(player.getAttacking());
            wolf.setAngerTime(600);
            double x = player.getX() + MathHelper.nextDouble(player.getRandom(), -3.0, 3.0);
            double z = player.getZ() + MathHelper.nextDouble(player.getRandom(), -3.0, 3.0);
            wolf.refreshPositionAndAngles(x, player.getY(), z, player.getYaw(), player.getPitch());
            SummonHelper.setScaledLife(wolf, soulPower, 20);
            SummonHelper.addStatScaling(wolf, soulPower);
            world.spawnEntity(wolf);
            SummonHelper.createParticlesAround(wolf, world);
        }
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.hound_pack";
    }
}
