package safro.archon.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.entity.projectile.WindBallEntity;

import java.util.function.Predicate;

public class AlyaEntity extends AbstractBossEntity implements RangedAttackMob {
    private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE;

    public AlyaEntity(EntityType<? extends AlyaEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
    }

    protected void initGoals() {
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0D, 40, 20.0F));
        this.goalSelector.add(3, new FlyGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, 0, false, false, CAN_ATTACK_PREDICATE));
    }

    public static DefaultAttributeContainer.Builder createAlyaAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6000000238418579D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64D);
    }

    @Override
    public BossBar.Color getBossColor() {
        return BossBar.Color.WHITE;
    }

    @Override
    public int getInvulTime() {
        return 100;
    }

    @Override
    public Text getSpawnMessage() {
        return Text.translatable("text.archon.alya.spawn").formatted(Formatting.WHITE);
    }

    @Override
    public Text getKillMessage() {
        return Text.translatable("text.archon.alya.kill").formatted(Formatting.WHITE);
    }

    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    public void slowMovement(BlockState state, Vec3d multiplier) {
    }

    public void tickMovement() {
        Vec3d vec3d = this.getVelocity().multiply(1.0D, 0.6D, 1.0D);
        if (!this.getWorld().isClient && this.getTarget() != null) {
            LivingEntity entity = this.getTarget();
            if (entity != null) {
                double d = vec3d.y;
                if (this.getY() < entity.getY()) {
                    d = Math.max(0.0D, d);
                    d += 0.3D - d * 0.6000000238418579D;
                }

                vec3d = new Vec3d(vec3d.x, d, vec3d.z);
                Vec3d vec3d2 = new Vec3d(entity.getX() - this.getX(), 0.0D, entity.getZ() - this.getZ());
                if (vec3d2.horizontalLengthSquared() > 9.0D) {
                    Vec3d vec3d3 = vec3d2.normalize();
                    vec3d = vec3d.add(vec3d3.x * 0.3D - vec3d.x * 0.6D, 0.0D, vec3d3.z * 0.3D - vec3d.z * 0.6D);
                }
            }
        }

        this.setVelocity(vec3d);
        if (vec3d.horizontalLengthSquared() > 0.05D) {
            this.setYaw((float)MathHelper.atan2(vec3d.z, vec3d.x) * 57.295776F - 90.0F);
        }
        super.tickMovement();
    }

    private double getHeadX(int headIndex) {
        if (headIndex <= 0) {
            return this.getX();
        } else {
            float f = (this.bodyYaw + (float)(180 * (headIndex - 1))) * 0.017453292F;
            float g = MathHelper.cos(f);
            return this.getX() + (double)g * 1.3D;
        }
    }

    private double getHeadY(int headIndex) {
        return headIndex <= 0 ? this.getY() + 1.0D : this.getY() + 2.2D;
    }

    private double getHeadZ(int headIndex) {
        if (headIndex <= 0) {
            return this.getZ();
        } else {
            float f = (this.bodyYaw + (float)(180 * (headIndex - 1))) * 0.017453292F;
            float g = MathHelper.sin(f);
            return this.getZ() + (double)g * 1.3D;
        }
    }

    private void shootAt(int headIndex, LivingEntity target) {
        this.shootWind(headIndex, target.getX(), target.getY() + (double)target.getStandingEyeHeight() * 0.5D, target.getZ());
    }

    private void shootWind(int headIndex, double targetX, double targetY, double targetZ) {
        double d = this.getHeadX(headIndex);
        double e = this.getHeadY(headIndex);
        double f = this.getHeadZ(headIndex);
        double g = targetX - d;
        double h = targetY - e;
        double i = targetZ - f;
        WindBallEntity w = new WindBallEntity(this.getWorld(), this, g, h, i);
        w.setOwner(this);
        w.setPos(d, e, f);
        this.getWorld().spawnEntity(w);
    }

    public void attack(LivingEntity target, float pullProgress) {
        this.setTarget(target);
        this.shootAt(0, target);
    }

    public boolean damage(DamageSource source, float amount) {
        if (super.damage(source, amount)) {
            double low = 0.3D * this.getMaxHealth();
            if (this.getHealth() <= low) {
                if (this.isOnGround() && getWorld().getBlockState(this.getBlockPos().up(2)).isAir()) {
                    this.setVelocity(this.getVelocity().add(0, 1, 0));
                }
            }
            return true;
        }
        return false;
    }

    static {
        CAN_ATTACK_PREDICATE = LivingEntity::isMobOrPlayer;
    }
}
