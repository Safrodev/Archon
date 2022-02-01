package safro.archon.item.necromancy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.ManaComponent;
import safro.archon.entity.OmegaSkeltEntity;
import safro.archon.entity.PrimeSkeltEntity;
import safro.archon.entity.SkeltEntity;
import safro.archon.registry.EntityRegistry;
import safro.archon.registry.ItemRegistry;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class UndeadStaffItem extends Item {

    public UndeadStaffItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        ManaComponent mana = ArchonUtil.get(player);

        if (ArchonUtil.canRemoveMana(player, 100)) {
            for (ItemEntity e : world.getNonSpectatingEntities(ItemEntity.class, player.getBoundingBox().expand(15))) {
                if (e.getStack().isOf(ItemRegistry.SOUL_CORE_CREATURE)) {
                    SkeltEntity skelt = EntityRegistry.SKELT.create(world);
                    summonSkelt(world, player, e, skelt, mana);
                } else if (e.getStack().isOf(ItemRegistry.SOUL_CORE_PLAYER)) {
                    PrimeSkeltEntity skelt = EntityRegistry.PRIME_SKELT.create(world);
                    summonSkelt(world, player, e, skelt, mana);
                } else if (e.getStack().isOf(ItemRegistry.SOUL_CORE_BOSS)) {
                    OmegaSkeltEntity skelt = EntityRegistry.OMEGA_SKELT.create(world);
                    summonSkelt(world, player, e, skelt, mana);
                }
            }
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    public void summonSkelt(World world, PlayerEntity player, ItemEntity e, SkeltEntity skelt, ManaComponent mana) {
        skelt.refreshPositionAndAngles(e.getBlockPos(), 0.0F, 0.0F);
        skelt.setOwner(player);
        skelt.initEquipment(world.getLocalDifficulty(e.getBlockPos()));
        world.spawnEntity(skelt);
        e.discard();
        mana.removeMana(100);
        world.addParticle(ParticleTypes.SOUL, skelt.getX() + (skelt.getRandom().nextDouble() - 0.5D) * (double)skelt.getWidth(), skelt.getY() + 0.1D, skelt.getZ() + (skelt.getRandom().nextDouble() - 0.5D) * (double)skelt.getWidth(), skelt.getVelocity().x * -0.2D, 0.1D, skelt.getVelocity().z * -0.2D);
        float f = skelt.getRandom().nextFloat() * 0.4F + skelt.getRandom().nextFloat() > 0.9F ? 0.6F : 0.0F;
        skelt.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE, f, 0.6F +skelt.getRandom().nextFloat() * 0.4F);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("text.archon.mana_cost", 100).formatted(Formatting.BLUE));
    }
}
