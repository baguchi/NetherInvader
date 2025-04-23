package baguchan.nether_invader.client;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.render.ChainedGhastRenderer;
import baguchan.nether_invader.client.render.ScaffoldRenderer;
import baguchan.nether_invader.registry.ModEntitys;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.PiglinRenderer;
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
        event.registerEntityRenderer(ModEntitys.CHAINED_GHAST.get(), ChainedGhastRenderer::new);
        event.registerEntityRenderer(ModEntitys.SCAFFOLDING.get(), ScaffoldRenderer::new);
        event.registerEntityRenderer(ModEntitys.AGRESSIVE_PIGLIN.get(), p_359282_ -> new PiglinRenderer(
                p_359282_,
                ModelLayers.PIGLIN,
                ModelLayers.PIGLIN_BABY,
                ModelLayers.PIGLIN_INNER_ARMOR,
                ModelLayers.PIGLIN_OUTER_ARMOR,
                ModelLayers.PIGLIN_BABY_INNER_ARMOR,
                ModelLayers.PIGLIN_BABY_OUTER_ARMOR
        ));
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {

    }
}