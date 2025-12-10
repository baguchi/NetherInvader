package baguchan.nether_invader.client.render;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.ModModelLayers;
import baguchan.nether_invader.client.model.BastionGeneralModel;
import baguchan.nether_invader.client.render.state.BastionGeneralRenderState;
import baguchan.nether_invader.entity.BastionGeneral;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;

public class BastionGeneralRenderer extends MobRenderer<BastionGeneral, BastionGeneralRenderState, BastionGeneralModel<BastionGeneralRenderState>> {
    private static final Identifier TEXTURES = Identifier.fromNamespaceAndPath(NetherInvader.MODID, "textures/entity/bastion_general.png");

    public BastionGeneralRenderer(
            EntityRendererProvider.Context p_174344_
    ) {
        super(p_174344_, new BastionGeneralModel<>(p_174344_.bakeLayer(ModModelLayers.BASTION_GENERAL)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public BastionGeneralRenderState createRenderState() {
        return new BastionGeneralRenderState();
    }

    @Override
    public void extractRenderState(BastionGeneral p_362733_, BastionGeneralRenderState p_360515_, float p_361157_) {
        super.extractRenderState(p_362733_, p_360515_, p_361157_);
        ArmedEntityRenderState.extractArmedEntityRenderState(p_362733_, p_360515_, this.itemModelResolver, p_361157_);
        p_360515_.attackAnimationState = p_362733_.attackAnimationState;
        p_360515_.converting = p_362733_.isConverting();
        p_360515_.isRiding = p_362733_.isPassenger();
    }

    @Override
    public Identifier getTextureLocation(BastionGeneralRenderState p_115708_) {
        return TEXTURES;
    }

    @Override
    protected boolean isShaking(BastionGeneralRenderState p_115712_) {
        return super.isShaking(p_115712_) || p_115712_.converting;
    }
}