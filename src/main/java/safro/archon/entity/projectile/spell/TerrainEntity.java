package safro.archon.entity.projectile.spell;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import safro.archon.registry.EntityRegistry;

public class TerrainEntity extends SpellProjectileEntity {
    private final Block block;

    public TerrainEntity(EntityType<? extends TerrainEntity> entityType, World world) {
        super(entityType, world);
        this.block = Blocks.STONE;
    }

    public TerrainEntity(World world, PlayerEntity owner, HitExecutor hitExecutor, Block block) {
        super(EntityRegistry.TERRAIN, world, owner, hitExecutor, new ItemStack(block));
        this.block = block;
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.SMOKE;
    }

    public Block getBlock() {
        return this.block;
    }

    @Override
    public float getDrag() {
        return 1F;
    }
}
