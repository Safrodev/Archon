package safro.archon.compat.emi;

import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import safro.archon.Archon;

public class ArchonSimplifiedRenderer implements EmiRenderable {

    //this is responsible for rendering small black and white icons representing the recipe types in the Recipe Tree.

    private final int u;
    private final int v;
    private final Identifier SPRITE_SHEET = new Identifier(Archon.MODID,"textures/gui/simple_icons.png");

    public ArchonSimplifiedRenderer(int u, int v){
        this.u = u;
        this.v = v;
    }

    @Override
    public void render(DrawContext context, int x, int y, float delta) {
        context.drawTexture(SPRITE_SHEET, x, y, this.u, this.v, 16, 16, 32, 16);
    }
}
