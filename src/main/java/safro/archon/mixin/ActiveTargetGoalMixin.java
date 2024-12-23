package safro.archon.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import safro.archon.api.summon.SummonedMob;

import java.util.function.Predicate;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin<T extends LivingEntity> extends TrackTargetGoal {

    public ActiveTargetGoalMixin(MobEntity mob, Class<T> targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(mob, checkVisibility, checkCanNavigate);
    }

    @Shadow @Nullable protected LivingEntity targetEntity;

    @Inject(method = "canStart", at = @At("RETURN"), cancellable = true)
    private void canSummonedMobAttackOwner(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if (this.targetEntity != null && this.targetEntity instanceof PlayerEntity player) {
                if (((SummonedMob)this.mob).archon$isOwner(player)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
