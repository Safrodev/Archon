package safro.archon.client.render;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class EmptyEntityRenderer extends EntityRenderer<Entity> {

    public EmptyEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public boolean shouldRender(Entity entity, Frustum frustum, double d, double e, double f) {
        return false;
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return null;
    }
}
