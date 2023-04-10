package safro.archon.client.render.block;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import safro.archon.block.entity.SummoningPedestalBlockEntity;

public class SummoningPedestalBlockEntityRenderer implements BlockEntityRenderer<SummoningPedestalBlockEntity> {

    public SummoningPedestalBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(SummoningPedestalBlockEntity entity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        DefaultedList<ItemStack> defaultedList = entity.getInventory();
        int k = (int)entity.getPos().asLong();
        if (entity.isIdle()) {
            for (int l = 0; l < defaultedList.size(); ++l) {
                ItemStack itemStack = defaultedList.get(l);
                if (itemStack != ItemStack.EMPTY) {
                    matrixStack.push();
                    matrixStack.translate(0.5D, 1.05, 0.5D);
                    Direction direction2 = Direction.fromHorizontal(l % 4);
                    float g = -direction2.asRotation();
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                    matrixStack.translate(-0.3125D, -0.3125, 0.0D);
                    matrixStack.scale(0.375F, 0.375F, 0.375F);
                    MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, i, j, matrixStack, vertexConsumerProvider, k + l);
                    matrixStack.pop();
                }
            }
        }
    }
}
