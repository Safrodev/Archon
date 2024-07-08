package safro.archon.spell;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.spell_power.api.SpellPower;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.spell.Spell;
import safro.archon.api.spell.SpellParticleData;
import safro.archon.entity.projectile.TerrainEntity;
import safro.archon.registry.TagRegistry;
import safro.archon.util.SpellUtil;

public class TerrainTossSpell extends Spell {

    public TerrainTossSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, SpellPower.Result power, ItemStack stack) {
        TerrainEntity terrain = new TerrainEntity(world, player, ((target, owner, projectile) -> {
            SpellUtil.damage(player, target, projectile, this.getElement(), 4.0F, 1.0F);
        }), this.getTerrain(player));
        terrain.disableParticles();
        SpellUtil.shoot(world, player, SpellParticleData.of(255, 255, 255, 0.01F), terrain, 0.5F);
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
