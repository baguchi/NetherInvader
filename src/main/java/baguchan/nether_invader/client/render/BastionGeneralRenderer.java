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
    public void extractRenderState(BastionGeneral bastionGeneral, BastionGeneralRenderState renderState, float p_361157_) {
        super.extractRenderState(bastionGeneral, renderState, p_361157_);
        ArmedEntityRenderState.extractArmedEntityRenderState(bastionGeneral, renderState, this.itemModelResolver, p_361157_);
        renderState.attackAnimationState.copyFrom(bastionGeneral.attackAnimationState);
        renderState.spinAttackAnimationState.copyFrom(bastionGeneral.spinAttackPoseAnimationState);
        renderState.spinAttackPoseAnimationState.copyFrom(bastionGeneral.spinAttackPoseAnimationState);
        renderState.spinAttackStopAnimationState.copyFrom(bastionGeneral.spinAttackStopAnimationState);
        renderState.converting = bastionGeneral.isConverting();
        renderState.isRiding = bastionGeneral.isPassenger();
        renderState.idle = !bastionGeneral.attackAnimationState.isStarted() && !bastionGeneral.isSpinAttack();
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