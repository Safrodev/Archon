package safro.archon.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EntityView;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.registry.CriteriaRegistry;
import safro.archon.registry.ItemRegistry;

import java.util.UUID;

public class SkeltEntity extends TameableEntity implements Angerable {
    private static final TrackedData<Integer> ANGER_TIME = DataTracker.registerData(WolfEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Nullable
    private UUID angryAt;

    public SkeltEntity(EntityType<? extends SkeltEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(false);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, AbstractSkeletonEntity.class, false));
        this.targetSelector.add(5, new UniversalAngerGoal<>(this, true));
    }

    public static DefaultAttributeContainer.Builder createSkeltAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getAttacker();
            this.setSitting(false);
            if (entity instanceof PlayerEntity) {
                amount = (amount + 1.0F) / 2.0F;
            }
            return super.damage(source, amount);
        }
    }

    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(this.getWorld().getDamageSources().mobAttack(this), (float)((int)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)));
        if (bl) {
            this.applyDamageEffects(this, target);
        }
        return bl;
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();
        if (this.getWorld().isClient) {
            boolean bl = this.isOwner(player) || this.isTamed();
            return bl ? ActionResult.CONSUME : ActionResult.PASS;
        } else {
            if (this.isTamed()) {
                if (!super.interactMob(player, hand).isAccepted() && this.isOwner(player)) {
                    if (stack.isOf(ItemRegistry.UNDEAD_STAFF)) {
                        this.setSitting(!this.isSitting());
                        this.navigation.stop();
                        this.setTarget(null);
                        this.messageSitting(player);
                        return ActionResult.SUCCESS;
                    }
                }
            }
            return super.interactMob(player, hand);
        }
    }

    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        if (target instanceof SkeltEntity e && e.isTamed()) {
            return false;
        } else if (target instanceof AbstractHorseEntity && ((AbstractHorseEntity)target).isTame()) {
            return false;
        } else if (target instanceof WolfEntity w && w.isTamed()) {
            return false;
        }
        return super.canAttackWithOwner(target, owner);
    }

    public void setOwner(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerUuid(player.getUuid());
        if (player instanceof ServerPlayerEntity) {
            CriteriaRegistry.SUMMON_UNDEAD_CRITERION.trigger((ServerPlayerEntity)player, this);
        }
    }

    public void messageSitting(PlayerEntity player) {
        if (this.isSitting()) {
            player.sendMessage(Text.translatable("text.archon.skelt_sit").formatted(Formatting.DARK_AQUA), true);
        } else
            player.sendMessage(Text.translatable("text.archon.skelt_stand").formatted(Formatting.DARK_AQUA), true);
    }

    public void initEquipment(Random random, LocalDifficulty difficulty) {
    }

    public float getScale() {
        return 1;
    }

    public int getAngerTime() {
        return this.dataTracker.get(ANGER_TIME);
    }

    public void setAngerTime(int angerTime) {
        this.dataTracker.set(ANGER_TIME, angerTime);
    }

    @Nullable
    @Override
    public UUID getAngryAt() {
        return this.angryAt;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void chooseRandomAngerTime() {
        UniformIntProvider range = TimeHelper.betweenSeconds(20, 39);
        this.setAngerTime(range.get(this.random));
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }
}
