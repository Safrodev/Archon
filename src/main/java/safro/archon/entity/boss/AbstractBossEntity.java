package safro.archon.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractBossEntity extends HostileEntity {
    private final ServerBossBar bossBar;
    private static final TrackedData<Integer> INVUL_TIMER;
    private int blockBreakingCooldown;

    public AbstractBossEntity(EntityType<? extends AbstractBossEntity> entityType, World world) {
        super(entityType, world);
        this.bossBar = new ServerBossBar(this.getDisplayName(), getBossColor(), BossBar.Style.PROGRESS);
        this.experiencePoints = 50;
    }

    public abstract BossBar.Color getBossColor();

    public abstract int getInvulTime();

    public abstract Item getDrop();

    public abstract Text getSpawnMessage();
    public abstract Text getKillMessage();

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(INVUL_TIMER, 0);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Invul", this.getInvulnerableTimer());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setInvulTimer(nbt.getInt("Invul"));
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    public void onSummoned() {
        this.setInvulTimer(getInvulTime());
        this.bossBar.setPercent(0.0F);
        List<ServerPlayerEntity> var = world.getNonSpectatingEntities(ServerPlayerEntity.class, this.getBoundingBox().expand(50));
        sendMessage(getSpawnMessage(), var);
    }

    public void sendMessage(Text text, List<ServerPlayerEntity> list) {
        while (list.iterator().hasNext()) {
            ServerPlayerEntity player = (ServerPlayerEntity)list.iterator().next();
            player.sendMessage(text, false);
            list.remove(player);
        }
    }

    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    public void tickMovement() {
        super.tickMovement();
        if (this.getInvulnerableTimer() > 0) {
            for(int d = 0; d < 3; ++d) {
                this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double)(this.random.nextFloat() * 3.3F), this.getZ() + this.random.nextGaussian(), 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
            }
        }
    }

    protected void mobTick() {
        int i;
        if (this.getInvulnerableTimer() > 0) {
            i = this.getInvulnerableTimer() - 1;
            this.bossBar.setPercent(1.0F - (float)i / 220.0F);
            if (i <= 0) {
                this.onInvulComplete();
            }
            this.setInvulTimer(i);
            if (this.age % 10 == 0) {
                this.heal(10.0F);
            }
        } else {
            super.mobTick();
            if (this.age % 20 == 0) {
                this.heal(1.0F);
            }
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }

    protected void onInvulComplete() {}

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source != DamageSource.DROWN) {
            if (this.getInvulnerableTimer() > 0 && source != DamageSource.OUT_OF_WORLD) {
                return false;
            } else {
                return super.damage(source, amount);
            }
        } else {
            return false;
        }
    }

    public int getInvulnerableTimer() {
        return (Integer)this.dataTracker.get(INVUL_TIMER);
    }

    public void setInvulTimer(int ticks) {
        this.dataTracker.set(INVUL_TIMER, ticks);
    }

    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        ItemEntity itemEntity = this.dropItem(getDrop());
        if (itemEntity != null) {
            itemEntity.setCovetedItem();
        }
    }

    public void onKilledOther(ServerWorld world, LivingEntity other) {
        if (other instanceof PlayerEntity) {
            List<ServerPlayerEntity> var = world.getNonSpectatingEntities(ServerPlayerEntity.class, this.getBoundingBox().expand(50));
            sendMessage(getKillMessage(), var);
        }
    }

    public void checkDespawn() {
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) {
            this.discard();
        } else {
            this.despawnCounter = 0;
        }
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    public boolean canUsePortals() {
        return false;
    }

    static {
        INVUL_TIMER = DataTracker.registerData(AbstractBossEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

}
