package safro.archon.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SilverfishEntityModel;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.entity.ManaLeechEntity;

public class ManaLeechEntityRenderer extends MobEntityRenderer<ManaLeechEntity, SilverfishEntityModel<ManaLeechEntity>> {
    private static final Identifier TEXTURE = new Identifier(Archon.MODID, "textures/entity/mana_leech.png");

    public ManaLeechEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SilverfishEntityModel(context.getPart(EntityModelLayers.SILVERFISH)), 0.3F);
    }

    protected float getLyingAngle(ManaLeechEntity entity) {
        return 180.0F;
    }

    public Identifier getTexture(ManaLeechEntity entity) {
        return TEXTURE;
    }
}
