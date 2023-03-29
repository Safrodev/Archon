package safro.archon.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;
import safro.archon.Archon;
import safro.archon.entity.boss.InigoEntity;

public class InigoEntityRenderer extends LivingEntityRenderer<InigoEntity, PlayerEntityModel<InigoEntity>> {
    private static final Identifier TEXTURE = new Identifier(Archon.MODID, "textures/entity/boss/inigo.png");

    public InigoEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel(context.getPart(EntityModelLayers.PLAYER), false), 0.5F);
        this.addFeature(new ArmorFeatureRenderer(this, new BipedEntityModel(context.getPart(EntityModelLayers.PLAYER_SLIM_INNER_ARMOR)), new BipedEntityModel(context.getPart(EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR))));
        this.addFeature(new PlayerHeldItemFeatureRenderer(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(InigoEntity leven) {
        return TEXTURE;
    }

    @Override
    protected boolean hasLabel(InigoEntity entity) {
        return false;
    }
}
