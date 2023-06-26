package safro.archon.client.render;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.client.model.SkeltEntityModel;
import safro.archon.entity.SkeltEntity;

public class SkeltEntityRenderer extends BipedEntityRenderer<SkeltEntity, SkeltEntityModel<SkeltEntity>> {
    private static final Identifier TEXTURE = new Identifier(Archon.MODID, "textures/entity/skelt.png");

    public SkeltEntityRenderer(EntityRendererFactory.Context context) {
        this(context, EntityModelLayers.SKELETON, EntityModelLayers.SKELETON_INNER_ARMOR, EntityModelLayers.SKELETON_OUTER_ARMOR);
    }

    public SkeltEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(ctx, new SkeltEntityModel(ctx.getPart(layer)), 0.5F);
        this.addFeature(new ArmorFeatureRenderer(this, new SkeltEntityModel(ctx.getPart(legArmorLayer)), new SkeltEntityModel(ctx.getPart(bodyArmorLayer)), ctx.getModelManager()));
    }

    protected void scale(SkeltEntity entity, MatrixStack matrixStack, float f) {
        if (entity.getScale() > 1F) {
            float a = entity.getScale();
            matrixStack.scale(a, a, a);
            this.shadowRadius = 1F;
        } else
            this.shadowRadius = 0.5F;
    }

    public Identifier getTexture(SkeltEntity entity) {
        return TEXTURE;
    }
}
