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
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.archon.registry.ItemRegistry;

import java.util.EnumSet;

public class TarEntity extends AbstractBossEntity {

    public TarEntity(EntityType<? extends TarEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new TarEntity.AttackGoal(this, 1.5D, false));
        this.goalSelector.add(1, new TarEntity.DropRocksGoal());
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
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
    public Item getDrop() {
        return ItemRegistry.TERRANITE_STONE;
    }

    @Override
    public Text getSpawnMessage() {
        return new TranslatableText("text.archon.tar.spawn").formatted(Formatting.YELLOW);
    }

    @Override
    public Text getKillMessage() {
        return new TranslatableText("text.archon.tar.kill").formatted(Formatting.YELLOW);
    }

    public boolean damage(DamageSource source, float amount) {
        if (source instanceof ProjectileDamageSource) {
            if (!(source.getSource() instanceof PotionEntity)) {
                return false;
            }
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
                BlockPos east = spawnPos.east();
                BlockPos north = spawnPos.north();
                BlockPos west = spawnPos.west();
                BlockPos south = spawnPos.south();
                for (int i = 0; i < 5; i++) {
                    BlockPos pos = getDirFromInt(spawnPos, i);
                    FallingBlockEntity block = new FallingBlockEntity(world, pos.getX(), pos.getY(), pos.getZ(), Blocks.COBBLESTONE.getDefaultState());
                    block.setHurtEntities(20.0F, 40);
                    block.timeFalling = Integer.MIN_VALUE;
                    block.dropItem = false;
                    world.spawnEntity(block);
                }

            /*    FallingBlockEntity block = new FallingBlockEntity(world, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), Blocks.COBBLESTONE.getDefaultState());
                block.setHurtEntities(20.0F, 40);
                block.timeFalling = Integer.MIN_VALUE;
                world.spawnEntity(block);
                FallingBlockEntity block1 = new FallingBlockEntity(world, east.getX(), east.getY(), east.getZ(), Blocks.COBBLESTONE.getDefaultState());
                block1.setHurtEntities(20.0F, 40);
                block1.timeFalling = Integer.MIN_VALUE;
                world.spawnEntity(block1);
                FallingBlockEntity block2 = new FallingBlockEntity(world, west.getX(), west.getY(), west.getZ(), Blocks.COBBLESTONE.getDefaultState());
                block2.setHurtEntities(20.0F, 40);
                block2.timeFalling = Integer.MIN_VALUE;
                world.spawnEntity(block2);
                FallingBlockEntity block3 = new FallingBlockEntity(world, south.getX(), south.getY(), south.getZ(), Blocks.COBBLESTONE.getDefaultState());
                block3.setHurtEntities(20.0F, 40);
                block3.timeFalling = Integer.MIN_VALUE;
                world.spawnEntity(block3);
                FallingBlockEntity block4 = new FallingBlockEntity(world, north.getX(), north.getY(), north.getZ(), Blocks.COBBLESTONE.getDefaultState());
                block4.setHurtEntities(20.0F, 40);
                block4.timeFalling = Integer.MIN_VALUE;
                world.spawnEntity(block4); */

                world.playSound(null, spawnPos, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
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

    protected class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        public boolean canStart() {
            LivingEntity livingEntity = TarEntity.this.getTarget();
            if (super.canStart()) {
                if (livingEntity != null && livingEntity.isAlive()) {
                    return TarEntity.this.distanceTo(livingEntity) < 10;
                }
            }
            return false;
        }
    }
}
