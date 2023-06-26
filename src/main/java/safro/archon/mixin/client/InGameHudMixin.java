package safro.archon.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
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
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        PlayerEntity player = this.getCameraPlayer();
        if (this.client.player != null && this.client.player.isAlive() && player != null) {
            if (Archon.CONFIG.displayManaWithItem) {
                if (ArchonUtil.isValidManaItem(player.getStackInHand(Hand.MAIN_HAND)) || ArchonUtil.isValidManaItem(player.getStackInHand(Hand.OFF_HAND))) {
                    renderManaHud(context, player);
                }
            } else {
                renderManaHud(context, player);
            }
        }
    }

    private void renderManaHud(DrawContext context, PlayerEntity player) {
        InGameHud hud = (InGameHud) (Object) this;
        int xoffset = Archon.CONFIG.mana_xoffset;
        int yoffset = Archon.CONFIG.mana_yoffset;

        int a = this.scaledWidth / 2;
        int k = this.scaledHeight - (yoffset + 5);
        context.drawTexture(MANA_TEXTURE, a - (xoffset + 20), k, 0, 0, 16, 16, 16, 16);
        this.client.getProfiler().pop();

        if ((ArchonUtil.get(player).getMana() <= ArchonUtil.get(player).getMaxMana())) {
            String string = ArchonUtil.get(player).getMana() + "/" + ArchonUtil.get(player).getMaxMana();
            int n = this.scaledHeight - yoffset;
            context.drawText(this.getTextRenderer(), string, a - xoffset, n, 16777215, true);
            this.client.getProfiler().pop();
        }
    }
}
