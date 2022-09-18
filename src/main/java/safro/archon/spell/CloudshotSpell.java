package safro.archon.spell;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.archon.api.Element;
import safro.archon.api.Spell;
import safro.archon.entity.projectile.spell.CloudshotEntity;
import safro.archon.registry.BlockRegistry;
import safro.archon.util.SpellUtil;

public class CloudshotSpell extends Spell {

    public CloudshotSpell(Element type, int manaCost) {
        super(type, manaCost);
    }

    @Override
    public void cast(World world, PlayerEntity player, ItemStack stack) {
        Vec3d vec3d = player.getRotationVec(0.0F);
        double vX = (player.getX() + vec3d.x * 4.0D) - player.getX();
        double vY = (player.getY() + vec3d.y * 4.0D) - player.getY();
        double vZ = (player.getZ() + vec3d.z * 4.0D) - player.getZ();
        CloudshotEntity cloudshot = new CloudshotEntity(world, player, vX, vY, vZ, new ItemStack(BlockRegistry.SOLID_CLOUD), ((target, owner, projectile) -> target.damage(DamageSource.mob(owner), 8.0F)));
        SpellUtil.spawn(world, player, cloudshot, 1.0F);
    }

    @Nullable
    @Override
    public SoundEvent getCastSound() {
        return SoundEvents.BLOCK_MOSS_CARPET_PLACE;
    }
}
