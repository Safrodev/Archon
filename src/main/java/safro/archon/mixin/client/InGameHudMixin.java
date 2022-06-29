package safro.archon.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.Archon;
import safro.archon.util.ArchonUtil;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    private static final Identifier MANA_TEXTURE = new Identifier(Archon.MODID,"textures/gui/mana_icon.png");

    @Shadow @Final private MinecraftClient client;

    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "render", at = @At("TAIL"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        PlayerEntity player = this.getCameraPlayer();
        if (this.client.player != null && this.client.player.isAlive() && player != null) {
            if (Archon.CONFIG.displayManaWithItem) {
                if (ArchonUtil.isValidManaItem(player.getStackInHand(Hand.MAIN_HAND)) || ArchonUtil.isValidManaItem(player.getStackInHand(Hand.OFF_HAND))) {
                    renderManaHud(matrices, player);
                }
            } else {
                renderManaHud(matrices, player);
            }
        }
    }

    private void renderManaHud(MatrixStack matrices, PlayerEntity player) {
        InGameHud hud = (InGameHud) (Object) this;
        int xoffset = Archon.CONFIG.mana_xoffset;
        int yoffset = Archon.CONFIG.mana_yoffset;

        int a = this.scaledWidth / 2;
        RenderSystem.setShaderTexture(0, MANA_TEXTURE);
        int k = this.scaledHeight - (yoffset + 5);
        hud.drawTexture(matrices, a - (xoffset + 20), k, 0, 0, 16, 16, 16, 16);
        this.client.getProfiler().pop();

        if ((ArchonUtil.get(player).getMana() <= ArchonUtil.get(player).getMaxMana())) {
            String string = ArchonUtil.get(player).getMana() + "/" + ArchonUtil.get(player).getMaxMana();
            int n = this.scaledHeight - yoffset;
            this.getTextRenderer().draw(matrices, string, a - xoffset, n, 16777215);
            this.client.getProfiler().pop();
        }
    }
}
