package baguchan.nether_invader.client;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.model.NetherSpreaderModel;
import baguchan.nether_invader.entity.NetherSpreader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class NetherSpreaderRenderer<T extends NetherSpreader> extends MobRenderer<T, NetherSpreaderModel<T>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(NetherInvader.MODID, "textures/entity/nether_spreader.png");

    public NetherSpreaderRenderer(EntityRendererProvider.Context p_174289_) {
        super(p_174289_, new NetherSpreaderModel<>(p_174289_.bakeLayer(ModModelLayers.NETHER_SPREADER)), 1.0F);
    }

    @Override
    protected boolean isShaking(T p_115304_) {
        return p_115304_.getSpreaderProgress() > 0.75F;
    }

    @Override
    protected void setupRotations(T p_115317_, PoseStack p_115318_, float p_115319_, float p_115320_, float p_115321_) {
        super.setupRotations(p_115317_, p_115318_, p_115319_, p_115320_, p_115321_);
        p_115318_.translate(0.0, -6F + p_115317_.getSpreaderProgressScale(p_115321_) * 6F, 0.0);
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return TEXTURE;
    }
}
