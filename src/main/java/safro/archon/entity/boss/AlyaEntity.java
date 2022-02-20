package safro.archon.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.entity.projectile.WindBallEntity;
import safro.archon.registry.ItemRegistry;
import safro.archon.util.LightningAccess;

import java.util.function.Predicate;

public class AlyaEntity extends AbstractBossEntity implements RangedAttackMob {
    private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE;
    private int strikeCooldown;

    public AlyaEntity(EntityType<? extends AlyaEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
    }

    protected void initGoals() {
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0D, 40, 20.0F));
        this.goalSelector.add(5, new FlyGoal(this, 1.0D));
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
    public Item getDrop() {
        return ItemRegistry.ANGELIC_STAR;
    }

    @Override
    public Text getSpawnMessage() {
        return new TranslatableText("text.archon.alya.spawn").formatted(Formatting.WHITE);
    }

    @Override
    public Text getKillMessage() {
        return new TranslatableText("text.archon.alya.kill").formatted(Formatting.WHITE);
    }

    public Text getStrikeMessage() {
        return new TranslatableText("text.archon.ayla.strike").formatted(Formatting.WHITE);
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
        WindBallEntity w = new WindBallEntity(this.world, this, g, h, i);
        w.setOwner(this);
        w.setPos(d, e, f);
        this.world.spawnEntity(w);
    }

    public void attack(LivingEntity target, float pullProgress) {
        this.shootAt(0, target);
    }

    public void tick() {
        super.tick();
        --strikeCooldown;
        if (this.getInvulnerableTimer() < 0) {
            if (this.getTarget() != null && strikeCooldown < 1) {
                LivingEntity target = this.getTarget();
                if (world.isSkyVisible(target.getBlockPos())) {
                    if (target instanceof PlayerEntity player) {
                        player.sendMessage(getStrikeMessage(), false);
                    }
                    LightningEntity lightningEntity = (LightningEntity) EntityType.LIGHTNING_BOLT.create(world);
                    lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(target.getBlockPos()));
                    ((LightningAccess)lightningEntity).setFireSpawning(false);
                    world.spawnEntity(lightningEntity);
                }
                strikeCooldown = 200 + random.nextInt(200);
            }
        }
    }

    public void onSummoned() {
        super.onSummoned();
        this.strikeCooldown = 200 + random.nextInt(200);
    }

    static {
        CAN_ATTACK_PREDICATE = LivingEntity::isMobOrPlayer;
    }
}
