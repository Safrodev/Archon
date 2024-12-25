package safro.archon.summon;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import safro.archon.api.summon.Summon;
import safro.archon.util.SummonHelper;

public class TitanSummon implements Summon {

    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        IronGolemEntity entity = EntityType.IRON_GOLEM.create(world);
        if (entity != null) {
            entity.setPlayerCreated(true);
            entity.setCustomName(Text.translatable(this.getTranslationKey()));
            int duration = SummonHelper.getScaledLife(soulPower, 25) * 20;
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, duration, 2, true, false, false));
            SummonHelper.spawnAndScale(world, player, entity, soulPower, 25);
        }
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.titan";
    }
}
