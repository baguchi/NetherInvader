package baguchan.nether_invader.client.render;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.entity.ChainedGhast;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.GhastModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChainedGhastRenderer extends MobRenderer<ChainedGhast, GhastModel<ChainedGhast>> {
    private static final ResourceLocation GHAST_LOCATION = new ResourceLocation(NetherInvader.MODID, "textures/entity/chained_ghast/chained_ghast.png");
    private static final ResourceLocation GHAST_SHOOTING_LOCATION = new ResourceLocation(NetherInvader.MODID, "textures/entity/chained_ghast/chained_ghast_shooting.png");

    public ChainedGhastRenderer(EntityRendererProvider.Context p_174129_) {
        super(p_174129_, new GhastModel<>(p_174129_.bakeLayer(ModelLayers.GHAST)), 1.5F);
    }

    public ResourceLocation getTextureLocation(ChainedGhast p_114755_) {
        return p_114755_.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LOCATION;
    }

    protected void scale(ChainedGhast p_114757_, PoseStack p_114758_, float p_114759_) {
        float f = 1.0F;
        float f1 = 4.5F;
        float f2 = 4.5F;
        p_114758_.scale(4.5F, 4.5F, 4.5F);
    }
}
