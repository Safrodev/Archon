package safro.archon.client.render;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import safro.archon.entity.projectile.WindBallEntity;

public class WindBallEntityRenderer extends EntityRenderer<WindBallEntity> {

    public WindBallEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public boolean shouldRender(WindBallEntity entity, Frustum frustum, double d, double e, double f) {
        return false;
    }

    @Override
    public Identifier getTexture(WindBallEntity entity) {
        return null;
    }
}
