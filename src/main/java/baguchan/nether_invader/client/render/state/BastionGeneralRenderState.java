package baguchan.nether_invader.client.render.state;

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class BastionGeneralRenderState extends ArmedEntityRenderState {
    public boolean isRiding;
    public boolean converting;
    public boolean idle;
    public AnimationState attackAnimationState = new AnimationState();
    public final AnimationState spinAttackAnimationState = new AnimationState();
    public final AnimationState spinAttackPoseAnimationState = new AnimationState();
    public final AnimationState spinAttackStopAnimationState = new AnimationState();

}
