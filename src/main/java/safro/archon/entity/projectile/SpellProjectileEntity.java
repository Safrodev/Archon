package safro.archon.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import safro.archon.registry.EntityRegistry;

public class SpellProjectileEntity extends AbstractFireballEntity {
    private final HitExecutor hitExecutor;
    private final TickExecutor tickExecutor;
    private boolean leftOwner;
    private boolean shot;

    public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.hitExecutor = HitExecutor.EMPTY;
        this.tickExecutor = TickExecutor.EMPTY;
    }

    public SpellProjectileEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ, HitExecutor hitExecutor, TickExecutor tickExecutor, ItemStack item) {
        super(EntityRegistry.SPELL_PROJECTILE, owner, velocityX, velocityY, velocityZ, world);
        this.hitExecutor = hitExecutor;
        this.tickExecutor = tickExecutor;
        this.setItem(item);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.world.isClient) {
            if (entityHitResult.getEntity() instanceof LivingEntity target && this.getOwner() instanceof LivingEntity owner) {
                this.hitExecutor.onHit(target, owner, this);
            }
        }
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.world.isClient || (entity == null || !entity.isRemoved()) && this.world.isChunkLoaded(this.getBlockPos())) {
            if (!this.shot) {
                this.emitGameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
                this.shot = true;
            }

            if (!this.leftOwner) {
                this.leftOwner = this.shouldLeaveOwner();
            }
            this.baseTick();

            if (this.isBurning()) {
                this.setOnFireFor(1);
            }

            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }

            this.checkBlockCollision();
            Vec3d vec3d = this.getVelocity();
            double d = this.getX() + vec3d.x;
            double e = this.getY() + vec3d.y;
            double f = this.getZ() + vec3d.z;
            ProjectileUtil.setRotationFromVelocity(this, 0.2F);
            float g = this.getDrag();
            if (this.isTouchingWater()) {
                for(int i = 0; i < 4; ++i) {
                    this.world.addParticle(ParticleTypes.BUBBLE, d - vec3d.x * 0.25D, e - vec3d.y * 0.25D, f - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
                }
                g = 0.8F;
            }

            this.setVelocity(vec3d.add(this.powerX, this.powerY, this.powerZ).multiply(g));
            this.setPosition(d, e, f);
        } else if (this.world.isClient) {
            this.tickExecutor.onTick(this);
        } else {
            this.discard();
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.leftOwner) {
            nbt.putBoolean("LeftOwnerSpell", true);
        }
        nbt.putBoolean("HasBeenShotSpell", this.shot);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.leftOwner = nbt.getBoolean("LeftOwner");
        this.shot = nbt.getBoolean("HasBeenShot");
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient && hitResult.getType() == HitResult.Type.ENTITY) {
            this.discard();
        }
    }

    private boolean shouldLeaveOwner() {
        Entity entity = this.getOwner();
        if (entity != null) {
            for (Entity entity2 : this.world.getOtherEntities(this, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), (entityx) -> !entityx.isSpectator() && entityx.canHit())) {
                if (entity2.getRootVehicle() == entity.getRootVehicle()) {
                    return false;
                }
            }
        }
        return true;
    }
}
