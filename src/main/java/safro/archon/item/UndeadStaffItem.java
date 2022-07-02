package safro.archon.item;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import safro.archon.entity.OmegaSkeltEntity;
import safro.archon.entity.PrimeSkeltEntity;
import safro.archon.entity.SkeltEntity;
import safro.archon.item.ManaItem;
import safro.archon.registry.EntityRegistry;
import safro.archon.registry.ItemRegistry;
import safro.archon.util.ArchonUtil;

public class UndeadStaffItem extends ManaItem {

    public UndeadStaffItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getManaCost() {
        return 100;
    }

    @Override
    public boolean activate(World world, PlayerEntity player, ItemStack stack, Hand hand) {
        for (ItemEntity e : world.getNonSpectatingEntities(ItemEntity.class, player.getBoundingBox().expand(15))) {
            if (e.getStack().isOf(ItemRegistry.SOUL_CORE_CREATURE)) {
                SkeltEntity skelt = EntityRegistry.SKELT.create(world);
                summonSkelt(world, player, e, skelt);
            } else if (e.getStack().isOf(ItemRegistry.SOUL_CORE_PLAYER)) {
                PrimeSkeltEntity skelt = EntityRegistry.PRIME_SKELT.create(world);
                summonSkelt(world, player, e, skelt);
            } else if (e.getStack().isOf(ItemRegistry.SOUL_CORE_BOSS)) {
                OmegaSkeltEntity skelt = EntityRegistry.OMEGA_SKELT.create(world);
                summonSkelt(world, player, e, skelt);
            }
        }
        return true;
    }

    public void summonSkelt(World world, PlayerEntity player, ItemEntity e, SkeltEntity skelt) {
        skelt.refreshPositionAndAngles(e.getBlockPos(), 0.0F, 0.0F);
        skelt.setOwner(player);
        skelt.initEquipment(world.getLocalDifficulty(e.getBlockPos()));
        world.spawnEntity(skelt);
        e.discard();
        ArchonUtil.get(player).removeMana(100);
        world.addParticle(ParticleTypes.SOUL, skelt.getX() + (skelt.getRandom().nextDouble() - 0.5D) * (double)skelt.getWidth(), skelt.getY() + 0.1D, skelt.getZ() + (skelt.getRandom().nextDouble() - 0.5D) * (double)skelt.getWidth(), skelt.getVelocity().x * -0.2D, 0.1D, skelt.getVelocity().z * -0.2D);
        float f = skelt.getRandom().nextFloat() * 0.4F + skelt.getRandom().nextFloat() > 0.9F ? 0.6F : 0.0F;
        skelt.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE, f, 0.6F +skelt.getRandom().nextFloat() * 0.4F);
    }
}
