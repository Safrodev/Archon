package safro.archon.entity.projectile;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.Archon;
import safro.archon.api.spell.HitExecutor;
import safro.archon.client.particle.SpellParticleEffect;
import safro.saflib.network.ParticlePacket;

public class SpellProjectileEntity extends ProjectileEntity {
    private static final int MAX_AGE = 400;
    private static final TrackedData<Float> RED = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> GREEN = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> BLUE = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SIZE = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> ENABLED = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<ParticleEffect> SECOND_PARTICLE = DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.PARTICLE);
    private final HitExecutor hitExecutor;

    public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.hitExecutor = HitExecutor.EMPTY;
    }

    public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, World world, LivingEntity owner, HitExecutor hitExecutor) {
        super(entityType, world);
        this.hitExecutor = hitExecutor;
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1F, owner.getZ());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > MAX_AGE) {
            this.discard();
        } else {
            Vec3d current = this.getPos();
            Vec3d next = current.add(this.getVelocity());
            HitResult hitResult = this.getWorld().raycast(new RaycastContext(current, next, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
            if (hitResult.getType() != HitResult.Type.MISS) {
                next = hitResult.getPos();
            }

            EntityHitResult entityHitResult = this.getEntityCollision(current, next);
            if (entityHitResult != null) {
                hitResult = entityHitResult;
            }

            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitResult).getEntity();
                Entity entity2 = this.getOwner();
                if (entity instanceof PlayerEntity && entity2 instanceof PlayerEntity && !((PlayerEntity) entity2).shouldDamagePlayer((PlayerEntity) entity)) {
                    hitResult = null;
                }
            }

            if (hitResult != null) {
                this.onCollision(hitResult);
                this.velocityDirty = true;
            }

            Vec3d vec3d = this.getVelocity();
            double x = this.getX() + vec3d.x;
            double y = this.getY() + vec3d.y;
            double z = this.getZ() + vec3d.z;
            this.setVelocity(vec3d.x, vec3d.y, vec3d.z);
            this.setPosition(x, y, z);
            this.checkBlockCollision();

            if (this.getWorld().isClient() && this.age > 4) {
                if (this.dataTracker.get(ENABLED)) {
                    this.addMainParticles();
                }

                if (this.getSecondParticle() != ParticleTypes.EFFECT && this.getSecondParticle() != null) {
                    this.getWorld().addParticle(this.getSecondParticle(), this.getX(), this.getY() + 0.5F, this.getZ(), 0.0F, 0.0F, 0.0F);
                }
            }
        }
    }

    private void addMainParticles() {
        SpellParticleEffect particle = new SpellParticleEffect(this.dataTracker.get(RED), this.dataTracker.get(GREEN), this.dataTracker.get(BLUE), this.dataTracker.get(SIZE));
        double deltaX = this.getX() - this.lastRenderX;
        double deltaY = this.getY() - this.lastRenderY;
        double deltaZ = this.getZ() - this.lastRenderZ;
        double distance = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 6.0D);
        for (double i = 0; i < distance; i++) {
            double step = i / distance;
            this.getWorld().addParticle(particle, (float) (this.prevX + deltaX * step), (float) (this.prevY + deltaY * step) + 0.1F, (float) (this.prevZ + deltaZ * step), 0.0125F * (random.nextFloat() - 0.5F), 0.0125F * (random.nextFloat() - 0.5F), 0.0125F * (random.nextFloat() - 0.5F));
        }
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), this::canHit);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient()) {
            if (entityHitResult.getEntity() instanceof LivingEntity target && this.getOwner() instanceof LivingEntity owner) {
                if (target != owner) {
                    this.hitExecutor.onHit(target, owner, this);

                    if (this.getWorld() instanceof ServerWorld) {
                        Vec3d pos = entityHitResult.getPos();
                        for (int i = 0; i < 10; i++) {
                            double x = pos.getX() + 0.5D;
                            double y = pos.getY() + 1.2D;
                            double z = pos.getZ() + 0.5D;
                            double vX = (target.getRandom().nextDouble() - 0.5D) / 3.0D;
                            double vY = (target.getRandom().nextDouble() - 0.5D) / 3.0D;
                            double vZ = (target.getRandom().nextDouble() - 0.5D) / 3.0D;
                            ParticlePacket.send(target, new SpellParticleEffect(this.dataTracker.get(RED), this.dataTracker.get(GREEN), this.dataTracker.get(BLUE), this.dataTracker.get(SIZE)), x, y, z, vX, vY, vZ);
                        }
                    }
                }
            }
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getWorld().isClient) {
            this.discard();
        }
    }

    @Override
    public void setVelocity(Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        float g = -MathHelper.sin((pitch + roll) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
        this.setVelocity(f, g, h, speed, divergence);
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(this.random.nextGaussian() * (double) 0.0075F * (double) divergence, this.random.nextGaussian() * (double) 0.0075F * (double) divergence, this.random.nextGaussian() * (double) 0.0075F * (double) divergence).multiply(speed);
        this.setVelocity(vec3d);
        float d = (float) Math.sqrt(vec3d.horizontalLengthSquared());
        this.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * 180.0F / (float) Math.PI));
        this.setPitch((float) (MathHelper.atan2(vec3d.y, d) * 180.0F / (float) Math.PI));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }

    public void setParticle(float red, float green, float blue, float size) {
        this.dataTracker.set(RED, red);
        this.dataTracker.set(GREEN, green);
        this.dataTracker.set(BLUE, blue);
        this.dataTracker.set(SIZE, size);
    }

    public void disableMainParticles() {
        this.dataTracker.set(ENABLED, false);
    }

    public ParticleEffect getSecondParticle() {
        return this.getDataTracker().get(SECOND_PARTICLE);
    }

    public void setSecondParticle(ParticleEffect particle) {
        this.getDataTracker().set(SECOND_PARTICLE, particle);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(RED, 1.0F);
        this.dataTracker.startTracking(GREEN, 1.0F);
        this.dataTracker.startTracking(BLUE, 1.0F);
        this.dataTracker.startTracking(SIZE, 0.5F);
        this.dataTracker.startTracking(ENABLED, true);
        this.dataTracker.startTracking(SECOND_PARTICLE, ParticleTypes.EFFECT);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("Red", this.dataTracker.get(RED));
        nbt.putFloat("Green", this.dataTracker.get(GREEN));
        nbt.putFloat("Blue", this.dataTracker.get(BLUE));
        nbt.putFloat("Size", this.dataTracker.get(SIZE));
        nbt.putBoolean("Enabled", this.dataTracker.get(ENABLED));
        if (this.getSecondParticle() != ParticleTypes.EFFECT) {
            nbt.putString("SecondParticle", this.getSecondParticle().asString());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(RED, nbt.getFloat("Red"));
        this.dataTracker.set(GREEN, nbt.getFloat("Green"));
        this.dataTracker.set(BLUE, nbt.getFloat("Blue"));
        this.dataTracker.set(SIZE, nbt.getFloat("Size"));
        this.dataTracker.set(ENABLED, nbt.getBoolean("Enabled"));

        if (nbt.contains("SecondParticle", NbtElement.STRING_TYPE)) {
            try {
                this.setSecondParticle(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("SecondParticle")), Registries.PARTICLE_TYPE.getReadOnlyWrapper()));
            } catch (CommandSyntaxException var5) {
                Archon.LOGGER.warn("Couldn't load custom particle {}", nbt.getString("SecondParticle"), var5);
            }
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}
