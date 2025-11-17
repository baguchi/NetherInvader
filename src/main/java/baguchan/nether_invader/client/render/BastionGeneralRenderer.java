package baguchan.nether_invader.client.render;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.ModModelLayers;
import baguchan.nether_invader.client.model.BastionGeneralModel;
import baguchan.nether_invader.entity.BastionGeneral;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class BastionGeneralRenderer extends MobRenderer<BastionGeneral, BastionGeneralModel<BastionGeneral>> {
    private static final ResourceLocation TEXTURES = ResourceLocation.fromNamespaceAndPath(NetherInvader.MODID, "textures/entity/bastion_general.png");

    public BastionGeneralRenderer(
            EntityRendererProvider.Context p_174344_
    ) {
        super(p_174344_, new BastionGeneralModel<>(p_174344_.bakeLayer(ModModelLayers.BASTION_GENERAL)), 0.5F);
        this.addLayer(new ItemInHandLayer(this, p_174344_.getItemInHandRenderer()));

    }

    @Override
    public ResourceLocation getTextureLocation(BastionGeneral p_115708_) {
        return TEXTURES;
    }

    @Override
    protected boolean isShaking(BastionGeneral p_115712_) {
        return super.isShaking(p_115712_) || p_115712_.isConverting();
    }
}