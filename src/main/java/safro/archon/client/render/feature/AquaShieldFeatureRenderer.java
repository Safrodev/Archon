package safro.archon.client.render.feature;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import safro.archon.registry.EffectRegistry;

public class AquaShieldFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private static final Identifier SKIN = new Identifier("textures/entity/creeper/creeper_armor.png");
    private final PlayerEntityModel<AbstractClientPlayerEntity> model;

    public AquaShieldFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> ctx, EntityModelLoader loader, boolean slim) {
        super(ctx);
        this.model = new PlayerEntityModel<>(loader.getModelPart(slim ? EntityModelLayers.PLAYER_SLIM : EntityModelLayers.PLAYER), slim);
    }

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.hasStatusEffect(EffectRegistry.AQUA_SHIELD)) {
            float f = (float)entity.age + tickDelta;
            EntityModel<AbstractClientPlayerEntity> entityModel = this.getEnergySwirlModel();
            entityModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
            this.getContextModel().copyStateTo(entityModel);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(this.getEnergySwirlTexture(), this.getEnergySwirlX(f) % 1.0F, f * 0.01F % 1.0F));
            entityModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            entityModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 0.5F, 0.5F, 0.5F, 1.0F);
        }
    }

    protected float getEnergySwirlX(float partialAge) {
        return partialAge * 0.01F;
    }

    protected Identifier getEnergySwirlTexture() {
        return SKIN;
    }

    protected EntityModel<AbstractClientPlayerEntity> getEnergySwirlModel() {
        return this.model;
    }
}
