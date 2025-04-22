package baguchan.nether_invader.client;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.model.NetherSpreaderModel;
import baguchan.nether_invader.client.render.AgressivePiglinRenderer;
import baguchan.nether_invader.client.render.ChainedGhastRenderer;
import baguchan.nether_invader.client.render.NetherSpreaderRenderer;
import baguchan.nether_invader.client.render.ScaffoldRenderer;
import baguchan.nether_invader.registry.ModEntitys;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = NetherInvader.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntitys.NETHER_SPREADER.get(), NetherSpreaderRenderer::new);
        event.registerEntityRenderer(ModEntitys.CHAINED_GHAST.get(), ChainedGhastRenderer::new);
        event.registerEntityRenderer(ModEntitys.SCAFFOLDING.get(), ScaffoldRenderer::new);
        event.registerEntityRenderer(ModEntitys.AGRESSIVE_PIGLIN.get(), AgressivePiglinRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.NETHER_SPREADER, NetherSpreaderModel::createBodyLayer);

    }
}