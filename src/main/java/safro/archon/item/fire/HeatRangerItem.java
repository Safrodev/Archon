package safro.archon.item.fire;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.util.ArchonUtil;

import java.util.List;

public class HeatRangerItem extends BowItem {

    public HeatRangerItem(Settings settings) {
        super(settings);
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)user;
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
            if (!((double)f < 0.1D)) {
                if (!world.isClient) {
                    Vec3d vec3d = player.getRotationVec(0.0F);
                    double vX = (player.getX() + vec3d.x * 4.0D) - player.getX();
                    double vY = (player.getY() + vec3d.y * 4.0D) - player.getY();
                    double vZ = (player.getZ() + vec3d.z * 4.0D) - player.getZ();

                    FireballEntity fireball = new FireballEntity(world, player, vX, vY, vZ, 2);
                    fireball.updatePosition(player.getX(), player.getEyeY(), player.getZ() + vec3d.z * 2.0D);
                    fireball.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, f * 3.0F, 1.0F);

                    stack.damage(1, player, (p) -> {
                        p.sendToolBreakStatus(player.getActiveHand());
                    });
                    world.spawnEntity(fireball);
                }
                ArchonUtil.get(player).removeMana(10);
                world.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                player.incrementStat(Stats.USED.getOrCreateStat(this));
            }
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (ArchonUtil.get(user).getMana() < 10) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("text.archon.mana_cost", 10).formatted(Formatting.BLUE));
    }
}
