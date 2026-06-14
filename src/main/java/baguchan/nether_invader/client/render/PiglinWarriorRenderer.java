package baguchan.nether_invader.client.render;

import baguchan.nether_invader.NetherInvader;
import baguchan.nether_invader.client.ModModelLayers;
import baguchan.nether_invader.client.model.PiglinWarriorModel;
import baguchan.nether_invader.client.render.state.PiglinWarriorRenderState;
import baguchan.nether_invader.entity.PiglinWarrior;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;

public class PiglinWarriorRenderer extends MobRenderer<PiglinWarrior, PiglinWarriorRenderState, PiglinWarriorModel<PiglinWarriorRenderState>> {
    private static final Identifier TEXTURES = Identifier.fromNamespaceAndPath(NetherInvader.MODID, "textures/entity/piglin_warrior.png");

    public PiglinWarriorRenderer(
            EntityRendererProvider.Context p_174344_
    ) {
        super(p_174344_, new PiglinWarriorModel<>(p_174344_.bakeLayer(ModModelLayers.PIGLIN_WARRIOR)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public PiglinWarriorRenderState createRenderState() {
        return new PiglinWarriorRenderState();
    }

    @Override
    public void extractRenderState(PiglinWarrior bastionGeneral, PiglinWarriorRenderState renderState, float p_361157_) {
        super.extractRenderState(bastionGeneral, renderState, p_361157_);
        ArmedEntityRenderState.extractArmedEntityRenderState(bastionGeneral, renderState, this.itemModelResolver, p_361157_);
        renderState.attackAnimationState.copyFrom(bastionGeneral.attackAnimationState);
        renderState.converting = bastionGeneral.isConverting();
        renderState.isRiding = bastionGeneral.isPassenger();
        renderState.barting = bastionGeneral.getArmPose() == PiglinArmPose.ADMIRING_ITEM;
        renderState.idle = !bastionGeneral.attackAnimationState.isStarted();
    }

    @Override
    public Identifier getTextureLocation(PiglinWarriorRenderState p_115708_) {
        return TEXTURES;
    }

    @Override
    protected boolean isShaking(PiglinWarriorRenderState p_115712_) {
        return super.isShaking(p_115712_) || p_115712_.converting;
    }
}