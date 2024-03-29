package safro.archon.client.render.block;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import safro.archon.block.entity.ScriptureTableBlockEntity;

public class ScriptureTableBlockEntityRenderer implements BlockEntityRenderer<ScriptureTableBlockEntity> {

    public ScriptureTableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(ScriptureTableBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int i, int j) {
        if (entity.getFuel() > 0) {
            ItemStack fuelStack = entity.getStack(4);
            matrixStack.push();
            matrixStack.translate(0.5D, 1.05, 0.5D);
            Direction direction2 = Direction.fromHorizontal(0);
            float g = -direction2.asRotation();
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            matrixStack.translate(-0.3125D, -0.3125, 0.0D);
            matrixStack.scale(0.375F, 0.375F, 0.375F);
            int k = (int)entity.getPos().asLong();
            MinecraftClient.getInstance().getItemRenderer().renderItem(fuelStack, ModelTransformationMode.FIXED, i, j, matrixStack, vertexConsumers, entity.getWorld(), k + 4);
            matrixStack.pop();
        }
    }
}
