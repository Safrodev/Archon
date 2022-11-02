package safro.archon.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import safro.archon.network.ExperienceChangePacket;

@Environment(EnvType.CLIENT)
public class ExperienceButtonWidget extends PressableWidget {
    private final TextRenderer textRenderer;
    protected final int value;
    private final Text text;
    protected final boolean add;

    protected ExperienceButtonWidget(int x, int y, int value, boolean add, TextRenderer textRenderer) {
        super(x, y, 22, 22, Text.of(Integer.toString(value)));
        this.value = value;
        this.textRenderer = textRenderer;
        this.text = Text.of(Integer.toString(value));
        this.add = add;
    }

    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ExperiencePouchScreen.TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int j = 0;
        if (this.isHovered()) {
            j += this.width * 3;
        }

        this.drawTexture(matrices, this.x, this.y, j, 219, this.width, this.height);
        this.drawText(matrices);
    }

    private void drawText(MatrixStack matrices) {
        int x = this.value >= 10 ? 6 : 8;
        drawTextWithShadow(matrices, this.textRenderer, this.text, this.x + x, this.y + 7, 16777215 | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    public void appendNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    protected MutableText getNarrationMessage() {
        return this.text.copy();
    }

    @Override
    public void onPress() {
        ExperienceChangePacket.send(this.value, this.add);
    }
}
