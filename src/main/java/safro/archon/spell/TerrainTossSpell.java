package safro.archon.spell;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.entity.projectile.spell.TerrainEntity;
import safro.archon.registry.TagRegistry;
import safro.archon.util.SpellUtil;

public class TerrainTossSpell extends Spell {

    public TerrainTossSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        TerrainEntity terrain = new TerrainEntity(world, player, ((target, owner, projectile) -> {
            target.takeKnockback(3 * 0.5F, MathHelper.sin(projectile.getYaw() * 0.017453292F), -MathHelper.cos(projectile.getYaw() * 0.017453292F));
            SpellUtil.damage(player, target, this, 4.0F);
        }), this.getTerrain(player));
        SpellUtil.spawn(world, player, terrain, 2.0F);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_STONE_FALL;
    }

    private Block getTerrain(PlayerEntity player) {
        BlockState state = player.getBlockStateAtPos();
        return state.isIn(TagRegistry.TERRAIN) ? state.getBlock() : Blocks.STONE;
    }
}
