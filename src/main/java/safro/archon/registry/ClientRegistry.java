package safro.archon.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.util.Identifier;
import safro.archon.client.render.ManaLeechEntityRenderer;
import safro.archon.client.render.SkeltEntityRenderer;
import safro.archon.client.render.WaterBoltEntityRenderer;

public class ClientRegistry {

    public static void init() {
        // Entity Renderers
        EntityRendererRegistry.register(EntityRegistry.WATER_BOLT, WaterBoltEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ICE_BALL, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SKELT, SkeltEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PRIME_SKELT, SkeltEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.OMEGA_SKELT, SkeltEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MANA_LEECH, ManaLeechEntityRenderer::new);

        // Block Renderers
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MANA_BERRY_BUSH, RenderLayer.getCutout());

        // Model Predicates
        FabricModelPredicateProviderRegistry.register(ItemRegistry.HEAT_RANGER, new Identifier("pull"),(stack, clientWorld, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });
        FabricModelPredicateProviderRegistry.register(ItemRegistry.HEAT_RANGER, new Identifier("pulling"), (stack, clientWorld, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);
    }
}
