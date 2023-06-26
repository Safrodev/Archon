package safro.archon.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import safro.archon.entity.ai.BurstGoal;
import safro.archon.network.InfernoLaserPacket;
import safro.archon.registry.ItemRegistry;

public class InigoEntity extends AbstractBossEntity implements RangedAttackMob {
    private static final TrackedData<Integer> BURST_COOLDOWN = DataTracker.registerData(InigoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> FLEE_COOLDOWN = DataTracker.registerData(InigoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private int beamDelay = 0;

    public InigoEntity(EntityType<? extends AbstractBossEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0F);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new BurstGoal(this, 4.5D));
        this.goalSelector.add(2, new RunAwayGoal<>(this, PlayerEntity.class, 10.0F, 2.0D, 2.2D));
        this.goalSelector.add(3, new ProjectileAttackGoal(this, 1.25D, 20, 60.0F));
        this.goalSelector.add(4, new GoToWalkTargetGoal(this, 1.0D));
        this.goalSelector.add(5, new WanderAroundGoal(this, 0.6D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createInigoAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 300.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getBurstCooldown() > 0) {
            this.setBurstCooldown(this.getBurstCooldown() - 1);
        }

        if (this.dataTracker.get(FLEE_COOLDOWN) > 0) {
            this.dataTracker.set(FLEE_COOLDOWN, this.dataTracker.get(FLEE_COOLDOWN) - 1);
        }
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        Vec3d pos = this.getEyePos();
        Vec3d targetPos = new Vec3d(target.getEyePos().x, target.getEyePos().y, target.getEyePos().z);
        targetPos.subtract(pos);
        targetPos.normalize();
        float targetYaw = (float) (MathHelper.atan2(targetPos.x, targetPos.z) * (double) (180F / (float) Math.PI));
        this.setYaw(-targetYaw - 180);
        Vec3d headPos = new Vec3d(pos.x, pos.y, pos.z);
        float rotation = ((this.getYaw() - 90) / 360) * (float) Math.PI * 2F;
        headPos.add(MathHelper.cos(rotation) * 7, 0, MathHelper.sin(rotation) * 7);

        if (!this.getWorld().isClient) {
            InfernoLaserPacket.send((ServerWorld) this.getWorld(), headPos, target.getEyePos());
        }

        this.setTarget(target);
        this.beamDelay = 15;
    }

    @Override
    public void mobTick() {
        super.mobTick();

        if (this.beamDelay > 0) {
            --this.beamDelay;

            if (this.beamDelay == 0 && this.getTarget() != null) {
                this.getTarget().damage(this.getWorld().getDamageSources().mobAttack(this), 15.0F);
            }
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity attacker && !source.isSourceCreativePlayer()) {
            attacker.setOnFireFor(3);
        }
        return super.damage(source, amount);
    }

    public int getBurstCooldown() {
        return this.dataTracker.get(BURST_COOLDOWN);
    }

    public void setBurstCooldown(int cooldown) {
        this.dataTracker.set(BURST_COOLDOWN, cooldown);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BURST_COOLDOWN, 60);
        this.dataTracker.startTracking(FLEE_COOLDOWN, 0);
    }

    @Override
    public BossBar.Color getBossColor() {
        return BossBar.Color.RED;
    }

    @Override
    public int getInvulTime() {
        return 60;
    }

    @Override
    public Item getDrop() {
        return ItemRegistry.CHARRED_EYE;
    }

    @Override
    public Text getSpawnMessage() {
        return Text.translatable("text.archon.inigo.spawn").formatted(Formatting.RED);
    }

    @Override
    public Text getKillMessage() {
        return Text.translatable("text.archon.inigo.kill").formatted(Formatting.RED);
    }

    static class RunAwayGoal<T extends LivingEntity> extends FleeEntityGoal<T> {
        private final InigoEntity inigo;

        public RunAwayGoal(InigoEntity mob, Class<T> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
            super(mob, fleeFromType, distance, slowSpeed, fastSpeed);
            this.inigo = mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && this.inigo.dataTracker.get(FLEE_COOLDOWN) <= 0;
        }

        @Override
        public void start() {
            super.start();
            this.inigo.dataTracker.set(FLEE_COOLDOWN, 200);
        }
    }
}
