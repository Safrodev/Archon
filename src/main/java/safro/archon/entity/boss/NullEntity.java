package safro.archon.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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
import net.minecraft.item.Item;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import safro.archon.entity.ai.SpinShotGoal;
import safro.archon.registry.ItemRegistry;

public class NullEntity extends AbstractBossEntity {
    private static final TrackedData<Integer> SPIN_COOLDOWN = DataTracker.registerData(InigoEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public NullEntity(EntityType<? extends NullEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createNullAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.24D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 30.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 400.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SpinShotGoal(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSpinCooldown() > 0) {
            this.setSpinCooldown(this.getSpinCooldown() - 1);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient()) {
            if ((source.getAttacker() != null && source.getAttacker() instanceof PlayerEntity) || source.isIn(DamageTypeTags.IS_PROJECTILE)) {
                for (int i = 0; i < 16; i++) {
                    if (this.teleportRandomly()) break;
                }
            }
        }
        return super.damage(source, amount);
    }

    protected boolean teleportRandomly() {
        if (!this.getWorld().isClient() && this.isAlive()) {
            double d = this.getX() + (this.random.nextDouble() - 0.5D) * 16.0D;
            double e = this.getY() + (double)(this.random.nextInt(32) - 16);
            double f = this.getZ() + (this.random.nextDouble() - 0.5D) * 16.0D;
            return this.teleportTo(d, e, f);
        } else {
            return false;
        }
    }

    private boolean teleportTo(Entity entity) {
        Vec3d vec3d = new Vec3d(this.getX() - entity.getX(), this.getBodyY(0.5D) - entity.getEyeY(), this.getZ() - entity.getZ());
        vec3d = vec3d.normalize();
        double e = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double f = this.getY() + (double)(this.random.nextInt(16) - 8) - vec3d.y * 16.0D;
        double g = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
        return this.teleportTo(e, f, g);
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

    public int getSpinCooldown() {
        return this.dataTracker.get(SPIN_COOLDOWN);
    }

    public void setSpinCooldown(int cooldown) {
        this.dataTracker.set(SPIN_COOLDOWN, cooldown);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SPIN_COOLDOWN, 200);
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
    public Item getDrop() {
        return ItemRegistry.WAVE_CRYSTAL;
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
