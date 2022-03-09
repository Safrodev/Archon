package safro.archon.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.Archon;
import safro.archon.api.ManaComponent;
import safro.archon.entity.SkeltEntity;
import safro.archon.registry.ItemRegistry;
import safro.archon.registry.TagRegistry;
import safro.archon.util.ArchonUtil;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void soulCrusherMana(DamageSource source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (source.getAttacker() instanceof PlayerEntity player) {
            ManaComponent mana = ArchonUtil.get(player);
            if (player.getMainHandStack().isOf(ItemRegistry.SOUL_CRUSHER) && mana.getMana() < mana.getMaxMana()) {
                mana.addMana(manaForType());
            } else if (player.getMainHandStack().isOf(ItemRegistry.SOUL_SCYTHE) && soulForType() != null) {
                if (TagRegistry.BOSSES.contains(entity.getType())) {
                    ArchonUtil.dropItem(entity.world, entity.getBlockPos(), soulForType());
                } else if (entity.getRandom().nextFloat() <= Archon.CONFIG.soulDropChance) {
                    ArchonUtil.dropItem(entity.world, entity.getBlockPos(), soulForType());
                }
            }
        }
    }

    private int manaForType() {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (TagRegistry.BOSSES.contains(entity.getType())) {
            return 50;
        } else if (entity.isPlayer()) {
            return 30;
        } else if (entity instanceof PassiveEntity) {
            return 20;
        }
        return 0;
    }

    private Item soulForType() {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (TagRegistry.BOSSES.contains(entity.getType())) {
            return ItemRegistry.BOSS_SOUL;
        } else if (TagRegistry.PLAYERS.contains(entity.getType())) {
            return ItemRegistry.PLAYER_SOUL;
        } else if (TagRegistry.CREATURES.contains(entity.getType()) && !(entity instanceof SkeltEntity)) {
            return ItemRegistry.CREATURE_SOUL;
        }
        return null;
    }
}
