package safro.archon.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.archon.registry.EffectRegistry;

import java.util.Iterator;
import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

    @Shadow protected M model;

    @Shadow protected abstract float getHandSwingProgress(T entity, float tickDelta);

    @Shadow protected abstract float getAnimationProgress(T entity, float tickDelta);

    @Shadow protected abstract void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta);

    @Shadow protected abstract void scale(T entity, MatrixStack matrices, float amount);

    @Shadow protected abstract boolean isVisible(T entity);

    @Shadow protected abstract float getAnimationCounter(T entity, float tickDelta);

    @Shadow @Nullable protected abstract RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);

    @Shadow @Final protected List<FeatureRenderer<T, M>> features;

    public LivingEntityRendererMixin(EntityRendererFactory.Context ctx, M model, float shadowRadius) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void renderShadowForm(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntity.hasStatusEffect(EffectRegistry.SHADOW)) {
            matrixStack.push();
            this.model.handSwingProgress = this.getHandSwingProgress(livingEntity, g);
            this.model.riding = livingEntity.hasVehicle();
            this.model.child = livingEntity.isBaby();
            float h = MathHelper.lerpAngleDegrees(g, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
            float j = MathHelper.lerpAngleDegrees(g, livingEntity.prevHeadYaw, livingEntity.headYaw);
            float k = j - h;
            float l;
            if (livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity) livingEntity.getVehicle();
                h = MathHelper.lerpAngleDegrees(g, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
                k = j - h;
                l = MathHelper.wrapDegrees(k);
                if (l < -85.0F) {
                    l = -85.0F;
                }

                if (l >= 85.0F) {
                    l = 85.0F;
                }

                h = j - l;
                if (l * l > 2500.0F) {
                    h += l * 0.2F;
                }

                k = j - h;
            }

            float m = MathHelper.lerp(g, livingEntity.prevPitch, livingEntity.getPitch());
            if (LivingEntityRenderer.shouldFlipUpsideDown(livingEntity)) {
                m *= -1.0F;
                k *= -1.0F;
            }

            float n;
            if (livingEntity.isInPose(EntityPose.SLEEPING)) {
                Direction direction = livingEntity.getSleepingDirection();
                if (direction != null) {
                    n = livingEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
                    matrixStack.translate((double) ((float) (-direction.getOffsetX()) * n), 0.0D, (double) ((float) (-direction.getOffsetZ()) * n));
                }
            }

            l = this.getAnimationProgress(livingEntity, g);
            this.setupTransforms(livingEntity, matrixStack, l, h, g);
            matrixStack.scale(-1.0F, -1.0F, 1.0F);
            this.scale(livingEntity, matrixStack, g);
            matrixStack.translate(0.0D, -1.5010000467300415D, 0.0D);
            n = 0.0F;
            float o = 0.0F;
            if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
                n = MathHelper.lerp(g, livingEntity.lastLimbDistance, livingEntity.limbDistance);
                o = livingEntity.limbAngle - livingEntity.limbDistance * (1.0F - g);
                if (livingEntity.isBaby()) {
                    o *= 3.0F;
                }

                if (n > 1.0F) {
                    n = 1.0F;
                }
            }

            this.model.animateModel(livingEntity, o, n, g);
            this.model.setAngles(livingEntity, o, n, l, k, m);
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            boolean bl = this.isVisible(livingEntity);
            boolean bl2 = !bl && !livingEntity.isInvisibleTo(minecraftClient.player);
            boolean bl3 = minecraftClient.hasOutline(livingEntity);
            RenderLayer renderLayer = this.getRenderLayer(livingEntity, bl, bl2, bl3);
            if (renderLayer != null) {
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
                int p = LivingEntityRenderer.getOverlay(livingEntity, this.getAnimationCounter(livingEntity, g));
                this.model.render(matrixStack, vertexConsumer, i, p, 1.0F, 1.0F, 1.0F, 0.5F);
            }

            if (!livingEntity.isSpectator()) {
                Iterator var23 = this.features.iterator();

                while (var23.hasNext()) {
                    FeatureRenderer<T, M> featureRenderer = (FeatureRenderer) var23.next();
                    featureRenderer.render(matrixStack, vertexConsumerProvider, i, livingEntity, o, n, g, l, k, m);
                }
            }

            matrixStack.pop();
            super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
            ci.cancel();
        }
    }
}
