package safro.archon.item;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import safro.archon.Archon;
import vazkii.patchouli.api.PatchouliAPI;

public class GrimoireItem extends Item {

    public GrimoireItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            if (FabricLoader.getInstance().isModLoaded("patchouli")) {
                PatchouliAPI.get().openBookGUI((ServerPlayerEntity) user, new Identifier(Archon.MODID, "grimoire"));
                return TypedActionResult.success(user.getStackInHand(hand));
            } else {
                user.sendMessage(Text.translatable("text.archon.invalid_book").formatted(Formatting.RED), true);
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
