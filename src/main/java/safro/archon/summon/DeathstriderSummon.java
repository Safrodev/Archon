package safro.archon.summon;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import safro.archon.api.summon.Summon;
import safro.archon.util.SummonHelper;

public class DeathstriderSummon implements Summon {

    @Override
    public void onSummon(ServerWorld world, PlayerEntity player, int soulPower) {
        SkeletonHorseEntity horse = EntityType.SKELETON_HORSE.create(world);
        if (horse != null) {
            horse.initialize(world, world.getLocalDifficulty(player.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            horse.setPosition(player.getX(), player.getY(), player.getZ());
            horse.setPersistent();
            horse.setTame(true);
            horse.setBreedingAge(0);
            horse.equipHorseArmor(player, new ItemStack(Items.IRON_HORSE_ARMOR));
            horse.saddle(SoundCategory.NEUTRAL);
            horse.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, SummonHelper.getScaledLife(soulPower, 30) * 20, 1, true, false, false));
            horse.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, SummonHelper.getScaledLife(soulPower, 30) * 20, 3, true, false, false));

            SummonHelper.spawnAndScale(world, player, horse, soulPower, 30);
        }
    }

    @Override
    public String getTranslationKey() {
        return "summon.archon.deathstrider";
    }
}
