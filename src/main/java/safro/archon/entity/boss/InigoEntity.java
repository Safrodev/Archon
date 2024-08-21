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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellSchools;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.entity.ai.BurstGoal;
import safro.archon.entity.projectile.SpellProjectileEntity;
import safro.archon.registry.EntityRegistry;
import safro.saflib.util.MathUtil;

public class InigoEntity extends AbstractBossEntity implements RangedAttackMob {
    private static final TrackedData<Integer> BURST_COOLDOWN = DataTracker.registerData(InigoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> FLEE_COOLDOWN = DataTracker.registerData(InigoEntity.class, TrackedDataHandlerRegistry.INTEGER);

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
        this.setTarget(target);
        this.lookAtEntity(target, 180.0F, 180.0F);
        this.setBodyYaw(this.getHeadYaw());

        SpellProjectileEntity beam = new SpellProjectileEntity(EntityRegistry.SPELL_PROJECTILE, this.getWorld(), this, (hit, owner, projectile) -> {
            hit.damage(SpellDamageSource.mob(SpellSchools.FIRE, this), 15.0F);
            hit.setOnFireFor(3);
        });
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - beam.getY();
        double f = target.getZ() - this.getZ();
        SpellParticleData data = SpellParticleData.of(255, 79, 48);
        beam.setParticle(data.red(), data.green(), data.blue(), data.size());
        beam.setVelocity(d, e, f, 3.0F, 0.8F);
        this.getWorld().spawnEntity(beam);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity attacker && !source.isSourceCreativePlayer()) {
            attacker.setOnFireFor(3);
        }
        return super.damage(source, amount);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 64) {
            for (int i = 0; i < Direction.Axis.VALUES.length; i++) {
                for (Vec3d vec3d : MathUtil.getCircle(this.getX(), this.getY(), this.getZ(), 4.5D, 2, Direction.Axis.VALUES[i])) {
                    this.getWorld().addParticle(ParticleTypes.SMALL_FLAME, vec3d.x, vec3d.y, vec3d.z, 0.0D, 0.03D, 0.0D);
                }
            }
        } else {
            super.handleStatus(status);
        }
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
