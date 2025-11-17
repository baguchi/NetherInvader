package baguchan.nether_invader.client.render.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class ScaffoldingRenderState extends LivingEntityRenderState {

    @Nullable
    public List<ChainState> chainStates;

    public static class ChainState {
        public Vec3 offset;
        public Vec3 start;
        public Vec3 end;
        public int startBlockLight;
        public int endBlockLight;
        public int startSkyLight;
        public int endSkyLight;

        public ChainState() {
            this.offset = Vec3.ZERO;
            this.start = Vec3.ZERO;
            this.end = Vec3.ZERO;
            this.startBlockLight = 0;
            this.endBlockLight = 0;
            this.startSkyLight = 15;
            this.endSkyLight = 15;
        }
    }
}
