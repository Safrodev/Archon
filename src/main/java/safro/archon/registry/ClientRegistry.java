package safro.archon.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import safro.archon.client.particle.InfernoLaserParticle;
import safro.archon.client.particle.WaterBallParticle;
import safro.archon.client.render.*;
import safro.archon.client.render.block.ScriptureTableBlockEntityRenderer;
import safro.archon.client.render.block.SummoningPedestalBlockEntityRenderer;
import safro.archon.client.screen.ExperiencePouchScreen;
import safro.archon.client.screen.ScriptureTableScreen;
import safro.saflib.client.render.EmptyEntityRenderer;

public class ClientRegistry {
    public static final KeyBinding IC_KEY = new KeyBinding("key.archon.infernal_coat", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PERIOD, "category.archon.archon");

    public static void init() {
        // Entity Renderers
        EntityRendererRegistry.register(EntityRegistry.WATER_BOLT, WaterBoltEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ICE_BALL, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.WIND_BALL, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SPELL_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.HELLBEAM, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CLOUDSHOT, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TERRAIN, TerrainEntityRenderer::new);

        EntityRendererRegistry.register(EntityRegistry.SKELT, SkeltEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.PRIME_SKELT, SkeltEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.OMEGA_SKELT, SkeltEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MANA_LEECH, ManaLeechEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.TAR, TarEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ALYA, AlyaEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.LEVEN, LevenEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.INIGO, InigoEntityRenderer::new);

        // Block Renderers
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MANA_BERRY_BUSH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.SUMMONING_PEDESTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.SCRIPTURE_TABLE, RenderLayer.getCutout());

        BlockEntityRendererFactories.register(BlockRegistry.SUMMONING_PEDESTAL_BE, SummoningPedestalBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockRegistry.SCRIPTURE_TABLE_BE, ScriptureTableBlockEntityRenderer::new);

        // Model Predicates
        ModelPredicateProviderRegistry.register(ItemRegistry.HEAT_RANGER, new Identifier("pull"),(stack, clientWorld, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });
        ModelPredicateProviderRegistry.register(ItemRegistry.HEAT_RANGER, new Identifier("pulling"), (stack, clientWorld, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F);

        // Screens
        HandledScreens.register(MiscRegistry.SCRIPTURE_TABLE_SH, ScriptureTableScreen::new);
        HandledScreens.register(MiscRegistry.EXPERIENCE_POUCH_SH, ExperiencePouchScreen::new);

        // Particles
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.WATER_BALL, WaterBallParticle.WaterBallFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.INFERNO_LASER, InfernoLaserParticle.Factory::new);

        // Keybinds
        KeyBindingHelper.registerKeyBinding(IC_KEY);
    }
}
