package safro.archon.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import safro.archon.entity.ai.DistanceMeleeGoal;
import safro.archon.entity.ai.ShardShotGoal;
import safro.archon.entity.ai.SummonEndermenGoal;
import safro.archon.registry.ItemRegistry;

public class NullEntity extends AbstractBossEntity {
    private static final TrackedData<Integer> SHARD_COOLDOWN = DataTracker.registerData(NullEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> SUMMON_COOLDOWN = DataTracker.registerData(NullEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public NullEntity(EntityType<? extends NullEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createNullAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.24D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 30.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 400.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new ShardShotGoal(this));
        this.goalSelector.add(0, new SummonEndermenGoal(this));
        this.goalSelector.add(1, new DistanceMeleeGoal(this, 1.5D, false, 10.0F));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getShardCooldown() > 0) {
            this.setShardCooldown(this.getShardCooldown() - 1);
        }

        if (this.getSummonCooldown() > 0) {
            this.setSummonCooldown(this.getSummonCooldown() - 1);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient()) {
            if ((source.getAttacker() != null && source.getAttacker() instanceof LivingEntity) || source.isIn(DamageTypeTags.IS_PROJECTILE)) {
                for (int i = 0; i < 16; i++) {
                    if (this.teleportRandomly()) break;
                }
            }
        }
        return super.damage(source, amount);
    }

    @Override
    public void initEquipment(Random random, LocalDifficulty difficulty) {
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.ENDER_BLADE));
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        Random random = world.getRandom();
        this.initEquipment(random, difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected float getDropChance(EquipmentSlot slot) {
        if (slot.getType() == EquipmentSlot.Type.HAND) {
            return 0.0F;
        }
        return super.getDropChance(slot);
    }

    protected boolean teleportRandomly() {
        if (!this.getWorld().isClient() && this.isAlive()) {
            double d = this.getX() + (this.random.nextDouble() - 0.5D) * 16.0D;
            double e = this.getY() + (double)(this.random.nextInt(10) - 5);
            double f = this.getZ() + (this.random.nextDouble() - 0.5D) * 16.0D;
            return this.teleportTo(d, e, f);
        } else {
            return false;
        }
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);

        while(mutable.getY() > this.getWorld().getBottomY() && !this.getWorld().getBlockState(mutable).blocksMovement()) {
            mutable.move(Direction.DOWN);
        }

        BlockState blockState = this.getWorld().getBlockState(mutable);
        boolean bl = blockState.blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (bl && !bl2) {
            Vec3d vec3d = this.getPos();
            boolean bl3 = this.teleport(x, y, z, true);
            if (bl3) {
                this.getWorld().emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(this));
                if (!this.isSilent()) {
                    this.getWorld().playSound(null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }
            }
            return bl3;
        } else {
            return false;
        }
    }

    public int getShardCooldown() {
        return this.dataTracker.get(SHARD_COOLDOWN);
    }

    public void setShardCooldown(int cooldown) {
        this.dataTracker.set(SHARD_COOLDOWN, cooldown);
    }

    public int getSummonCooldown() {
        return this.dataTracker.get(SUMMON_COOLDOWN);
    }

    public void setSummonCooldown(int cooldown) {
        this.dataTracker.set(SUMMON_COOLDOWN, cooldown);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SHARD_COOLDOWN, 200);
        this.dataTracker.startTracking(SUMMON_COOLDOWN, 80);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 12) {
            this.produceParticles(ParticleTypes.DRAGON_BREATH);
        } else {
            super.handleStatus(status);
        }
    }

    protected void produceParticles(ParticleEffect parameters) {
        for(int i = 0; i < 5; ++i) {
            double d = this.random.nextGaussian() * 0.02D;
            double e = this.random.nextGaussian() * 0.02D;
            double f = this.random.nextGaussian() * 0.02D;
            this.getWorld().addParticle(parameters, this.getParticleX(1.0D), this.getRandomBodyY() + 1.0D, this.getParticleZ(1.0D), d, e, f);
        }
    }

    @Override
    public BossBar.Color getBossColor() {
        return BossBar.Color.PURPLE;
    }

    @Override
    public int getInvulTime() {
        return 80;
    }

    @Override
    public Text getSpawnMessage() {
        return Text.translatable("text.archon.null.spawn").formatted(Formatting.DARK_PURPLE);
    }

    @Override
    public Text getKillMessage() {
        return Text.translatable("text.archon.null.kill").formatted(Formatting.DARK_PURPLE);
    }
}
