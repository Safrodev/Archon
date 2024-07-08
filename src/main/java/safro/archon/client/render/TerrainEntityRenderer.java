package safro.archon.client.render;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import safro.archon.entity.projectile.TerrainEntity;

public class TerrainEntityRenderer extends EntityRenderer<TerrainEntity> {

    public TerrainEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(TerrainEntity projectile, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, projectile.prevYaw, projectile.getYaw())));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, projectile.prevPitch, projectile.getPitch())));
        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
        if (projectile.getCustomName() != null && projectile.getCustomName().getString().equals("NullShard")) {
            blockRenderManager.renderBlockAsEntity(Blocks.OBSIDIAN.getDefaultState(), matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
        } else {
            blockRenderManager.renderBlockAsEntity(projectile.getBlock().getDefaultState(), matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
        }
        matrixStack.pop();
        super.render(projectile, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(TerrainEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}
