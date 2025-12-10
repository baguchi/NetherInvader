package baguchan.nether_invader.client.render.state;

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class BastionGeneralRenderState extends ArmedEntityRenderState {
    public boolean isRiding;
    public boolean converting;
    public AnimationState attackAnimationState = new AnimationState();
}
