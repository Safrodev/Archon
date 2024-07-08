package safro.archon.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import safro.archon.api.spell.HitExecutor;
import safro.archon.registry.EntityRegistry;

public class TerrainEntity extends SpellProjectileEntity {
    private Block block;

    public TerrainEntity(EntityType<? extends TerrainEntity> entityType, World world) {
        super(entityType, world);
        this.block = Blocks.STONE;
    }

    public TerrainEntity(World world, LivingEntity owner, HitExecutor hitExecutor, Block block) {
        super(EntityRegistry.TERRAIN, world, owner, hitExecutor);
        this.block = block;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient()) {
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return this.block;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        Identifier id = new Identifier(nbt.getString("Terrain"));
        this.block = Registries.BLOCK.get(id);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        Identifier id = Registries.BLOCK.getId(this.block);
        nbt.putString("Terrain", id.toString());
    }
}
