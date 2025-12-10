package baguchan.nether_invader.client;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.model.BastionGeneralModel;
import baguchan.nether_invader.client.render.BastionGeneralRenderer;
import baguchan.nether_invader.client.render.ChainedGhastRenderer;
import baguchan.nether_invader.client.render.ScaffoldRenderer;
import baguchan.nether_invader.registry.ModEntities;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NetherInvader.MODID, value = Dist.CLIENT)
public class ClientRegistrar {

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CHAINED_GHAST.get(), ChainedGhastRenderer::new);
        event.registerEntityRenderer(ModEntities.SCAFFOLDING.get(), ScaffoldRenderer::new);
        event.registerEntityRenderer(ModEntities.AGRESSIVE_PIGLIN.get(), p_359282_ -> new PiglinRenderer(
                p_359282_,
                ModelLayers.PIGLIN,
                ModelLayers.PIGLIN_BABY,
                ModelLayers.PIGLIN_ARMOR,
                ModelLayers.PIGLIN_BABY_ARMOR
        ));
        event.registerEntityRenderer(ModEntities.BASTION_GENERAL.get(), BastionGeneralRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.BASTION_GENERAL, BastionGeneralModel::createBodyLayer);
    }
}