package baguchan.nether_invader.client.render.state;

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class PiglinWarriorRenderState extends ArmedEntityRenderState {
    public boolean isRiding;
    public boolean barting;
    public boolean idle;
    public boolean converting;
    public AnimationState attackAnimationState = new AnimationState();
    ;

}
