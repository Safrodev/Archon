package safro.archon.spell;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.damage.DamageSource;
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
import safro.archon.util.SpellUtil;

import java.util.List;

public class TerrainTossSpell extends Spell {
    private static final List<Material> VALID_MATERIALS = ImmutableList.of(Material.AGGREGATE, Material.ICE, Material.STONE, Material.AMETHYST, Material.ORGANIC_PRODUCT, Material.SOIL, Material.SOLID_ORGANIC);

    public TerrainTossSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        TerrainEntity terrain = new TerrainEntity(world, player, ((target, owner, projectile) -> {
            target.takeKnockback(3 * 0.5F, MathHelper.sin(projectile.getYaw() * 0.017453292F), -MathHelper.cos(projectile.getYaw() * 0.017453292F));
            target.damage(DamageSource.mob(owner), 4.0F);
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
        return VALID_MATERIALS.contains(state.getMaterial()) ? state.getBlock() : Blocks.STONE;
    }
}
