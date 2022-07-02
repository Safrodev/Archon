package safro.archon.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.entity.ai.DistanceMeleeGoal;
import safro.archon.entity.ai.DistanceRangedGoal;
import safro.archon.entity.projectile.WaterBoltEntity;
import safro.archon.registry.ItemRegistry;
import safro.archon.util.TridentAccess;

public class LevenEntity extends AbstractBossEntity implements RangedAttackMob {

    public LevenEntity(EntityType<? extends LevenEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new DistanceMeleeGoal(this, 1.5D, false, 10));
        this.goalSelector.add(1, new DistanceRangedGoal(this, 1.2D, 40, 40.0F, 10));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.7D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createLevenAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.26D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64D);
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        this.swingHand(Hand.MAIN_HAND);
        this.moveControl.strafeTo(random.nextBoolean() ? 0.5F : -0.5F, random.nextBoolean() ? 0.5F : -0.5F);
        double e = target.getX() - this.getX();
        double f = target.getBodyY(0.5D) - this.getBodyY(0.5D);
        double g = target.getZ() - this.getZ();
        double h = Math.sqrt(Math.sqrt(this.squaredAttackRange(target))) * 0.5D;

        WaterBoltEntity bolt = new WaterBoltEntity(this.world, this, e + this.getRandom().nextGaussian() * h, f, g + this.getRandom().nextGaussian() * h);
        bolt.setPosition(bolt.getX(), this.getBodyY(0.5D) + 0.5D, bolt.getZ());
        bolt.setSplash(true);
        this.world.spawnEntity(bolt);
    }

    public void tickMeleeAttack() {
        if (this.random.nextFloat() <= 0.05F) {
            for (int i = 0; i < 10; i++) {
                TridentEntity trident = new TridentEntity(world, this, new ItemStack(Items.TRIDENT));
                trident.teleport(this.getX(), this.getEyeY(), this.getZ());
                trident.setVelocity(new Vec3d(random.nextGaussian(), random.nextGaussian() / 2, random.nextGaussian()).normalize().multiply(0.75));
                trident.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                ((TridentAccess)trident).setQuickDespawn();
                trident.setPunch(2);
                world.spawnEntity(trident);
            }
        }
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.getTarget() != null && this.getTarget().isAlive()) {
            if (this.distanceTo(getTarget()) < 10) {
                this.tickMeleeAttack();
            }
        }
    }

    public boolean isPushedByFluids() {
        return false;
    }

    public boolean damage(DamageSource source, float amount) {
        if (source == DamageSource.DROWN || source == DamageSource.FREEZE) {
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    public BossBar.Color getBossColor() {
        return BossBar.Color.BLUE;
    }

    @Override
    public int getInvulTime() {
        return 60;
    }

    @Override
    public Item getDrop() {
        return ItemRegistry.WATER_GEM;
    }

    @Override
    public Text getSpawnMessage() {
        return new TranslatableText("text.archon.leven.spawn").formatted(Formatting.BLUE);
    }

    @Override
    public Text getKillMessage() {
        return new TranslatableText("text.archon.leven.kill").formatted(Formatting.BLUE);
    }
}
