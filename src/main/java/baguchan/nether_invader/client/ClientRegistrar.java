package baguchan.nether_invader.client;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.render.AgressivePiglinRenderer;
import baguchan.nether_invader.client.render.ChainedGhastRenderer;
import baguchan.nether_invader.client.render.ScaffoldRenderer;
import baguchan.nether_invader.registry.ModEntitys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = NetherInvader.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntitys.CHAINED_GHAST.get(), ChainedGhastRenderer::new);
        event.registerEntityRenderer(ModEntitys.SCAFFOLDING.get(), ScaffoldRenderer::new);
        event.registerEntityRenderer(ModEntitys.AGRESSIVE_PIGLIN.get(), AgressivePiglinRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
}