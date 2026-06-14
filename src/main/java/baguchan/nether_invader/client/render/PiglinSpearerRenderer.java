package baguchan.nether_invader.client.render;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.ModModelLayers;
import baguchan.nether_invader.client.model.PiglinSpearerModel;
import baguchan.nether_invader.client.render.state.PiglinSpearerRenderState;
import baguchan.nether_invader.entity.PiglinSpearer;
import baguchi.bagus_lib.client.layer.CustomArmorLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;

public class PiglinSpearerRenderer extends MobRenderer<PiglinSpearer, PiglinSpearerRenderState, PiglinSpearerModel<PiglinSpearerRenderState>> {
    private static final Identifier TEXTURES = Identifier.fromNamespaceAndPath(NetherInvader.MODID, "textures/entity/piglin_spearer.png");

    public PiglinSpearerRenderer(
            EntityRendererProvider.Context context
    ) {
        super(context, new PiglinSpearerModel<>(context.bakeLayer(ModModelLayers.PIGLIN_SPEARER)), 0.5F);
        this.addLayer(new CustomArmorLayer<>(this, context));
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public PiglinSpearerRenderState createRenderState() {
        return new PiglinSpearerRenderState();
    }

    @Override
    public void extractRenderState(PiglinSpearer bastionGeneral, PiglinSpearerRenderState renderState, float p_361157_) {
        super.extractRenderState(bastionGeneral, renderState, p_361157_);
        ArmedEntityRenderState.extractArmedEntityRenderState(bastionGeneral, renderState, this.itemModelResolver, p_361157_);
        renderState.attackAnimationState.copyFrom(bastionGeneral.attackAnimationState);
        renderState.converting = bastionGeneral.isConverting();
        renderState.isRiding = bastionGeneral.isPassenger();
        renderState.barting = bastionGeneral.getArmPose() == PiglinArmPose.ADMIRING_ITEM;
        renderState.idle = !bastionGeneral.attackAnimationState.isStarted();
    }

    @Override
    public Identifier getTextureLocation(PiglinSpearerRenderState p_115708_) {
        return TEXTURES;
    }

    @Override
    protected boolean isShaking(PiglinSpearerRenderState p_115712_) {
        return super.isShaking(p_115712_) || p_115712_.converting;
    }
}
