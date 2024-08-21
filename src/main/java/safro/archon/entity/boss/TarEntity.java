package safro.archon.entity.boss;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.archon.entity.ai.DistanceMeleeGoal;
import safro.archon.mixin.FallingBlockEntityAccessor;

import java.util.EnumSet;

public class TarEntity extends AbstractBossEntity {

    public TarEntity(EntityType<? extends TarEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new DistanceMeleeGoal(this, 1.5D, false, 10));
        this.goalSelector.add(1, new TarEntity.DropRocksGoal());
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createTarAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64D);
    }

    @Override
    public BossBar.Color getBossColor() {
        return BossBar.Color.YELLOW;
    }

    @Override
    public int getInvulTime() {
        return 60;
    }

    @Override
    public Text getSpawnMessage() {
        return Text.translatable("text.archon.tar.spawn").formatted(Formatting.YELLOW);
    }

    @Override
    public Text getKillMessage() {
        return Text.translatable("text.archon.tar.kill").formatted(Formatting.YELLOW);
    }

    public boolean damage(DamageSource source, float amount) {
        if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            if (!(source.getSource() instanceof PotionEntity)) {
                return false;
            }
        }

        if (source.getAttacker() instanceof PlayerEntity p && this.random.nextFloat() <= 0.4F) {
            this.swingHand(Hand.MAIN_HAND);
            this.pushEntity(p, 0.75F);
        }
        return super.damage(source, amount);
    }

    protected class DropRocksGoal extends Goal {
        protected int cooldown;
        protected int startTime;

        protected DropRocksGoal() {
            this.setControls(EnumSet.of(Control.LOOK));
        }

        public boolean canStart() {
            LivingEntity livingEntity = TarEntity.this.getTarget();
            if (livingEntity != null && livingEntity.isAlive()) {
                if (TarEntity.this.distanceTo(livingEntity) < 10) {
                    return false;
                }
                return TarEntity.this.age >= this.startTime;
            } else {
                return false;
            }
        }

        public boolean shouldContinue() {
            LivingEntity livingEntity = TarEntity.this.getTarget();
            return livingEntity != null && livingEntity.isAlive() && this.cooldown > 0;
        }

        public void start() {
            this.cooldown = this.getTickCount(20);
            this.startTime = TarEntity.this.age + 50;
            TarEntity.this.getLookControl().lookAt(TarEntity.this.getTarget(), 30F, 30F);
        }

        public void tick() {
            --this.cooldown;
            if (this.cooldown == 0) {
                BlockPos spawnPos = TarEntity.this.getTarget().getBlockPos().up(5);
                for (int i = 0; i < 5; i++) {
                    BlockPos pos = getDirFromInt(spawnPos, i);
                    FallingBlockEntity block = FallingBlockEntityAccessor.create(getWorld(), pos.getX(), pos.getY(), pos.getZ(), Blocks.COBBLESTONE.getDefaultState());
                    block.setHurtEntities(20.0F, 40);
                    block.timeFalling = Integer.MIN_VALUE;
                    block.dropItem = false;
                    getWorld().spawnEntity(block);
                }

                getWorld().playSound(null, spawnPos, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                TarEntity.this.swingHand(Hand.MAIN_HAND);
            }
        }

        private BlockPos getDirFromInt(BlockPos origin, int x) {
            if (x == 0) {
                return origin;
            } else if (x == 1) {
                return origin.north();
            } else if (x == 2) {
                return origin.south();
            } else if (x == 3) {
                return origin.east();
            } else if (x == 4) {
                return origin.west();
            }
            return origin;
        }
    }
}
