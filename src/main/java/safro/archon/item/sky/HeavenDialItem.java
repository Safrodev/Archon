package safro.archon.item.sky;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import safro.archon.item.ManaItem;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class HeavenDialItem extends ManaItem {

    public HeavenDialItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getManaCost() {
        return 80;
    }

    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(30D));
        if (list.size() > 0) {
            for (LivingEntity e : list) {
                if (e instanceof PlayerEntity || ArchonUtil.isOwnedBy(player, e) || e instanceof VillagerEntity || e instanceof IronGolemEntity) {
                    this.heal(world, e);
                }
            }
            return true;
        }
        return false;
    }

    private void heal(World world, LivingEntity entity) {
        if (!world.isClient) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 1));
            entity.heal(20);
        }
        for (int i = 0; i < 15; i++) {
            world.addParticle(ParticleTypes.HAPPY_VILLAGER, entity.getParticleX(1.0D), entity.getRandomBodyY() + 0.5D, entity.getParticleZ(1.0D), 0.0D, 0.0D, 0.0D);
        }
    }
}
