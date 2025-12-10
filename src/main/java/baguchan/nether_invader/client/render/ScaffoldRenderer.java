package baguchan.nether_invader.client.render;

import baguchan.nether_invader.client.model.TestModel;
import baguchan.nether_invader.client.render.state.ScaffoldingRenderState;
import baguchan.nether_invader.entity.Chainable;
import baguchan.nether_invader.entity.Scaffolding;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class ScaffoldRenderer extends LivingEntityRenderer<Scaffolding, ScaffoldingRenderState, TestModel> {
    public static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/boat/bamboo.png");
    public static final Identifier CHAIN_TEXTURE = Identifier.withDefaultNamespace("textures/item/iron_chain.png");

    public ScaffoldRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new TestModel(p_174304_.bakeLayer(ModelLayers.BAMBOO_RAFT)), 0.5F);
    }

    @Override
    protected boolean shouldShowName(Scaffolding p_363517_, double p_365448_) {
        return false;
    }

    @Override
    public ScaffoldingRenderState createRenderState() {
        return new ScaffoldingRenderState();
    }

    @Override
    public void extractRenderState(Scaffolding p_362733_, ScaffoldingRenderState p_360515_, float p_361157_) {
        super.extractRenderState(p_362733_, p_360515_, p_361157_);
        Entity entity = p_362733_ instanceof Chainable leashable ? leashable.getChainHolder() : null;
        if (entity != null && p_362733_ instanceof Chainable chainable) {
            float f = p_362733_.getPreciseBodyRotation(p_361157_) * (float) (Math.PI / 180.0);
            float f1 = entity.getPreciseBodyRotation(p_361157_) * (float) (Math.PI / 180.0);
            boolean flag = entity.supportQuadLeashAsHolder() && chainable.supportQuadChain();
            int i1 = flag ? 4 : 1;
            if (p_360515_.chainStates == null || p_360515_.chainStates.size() != i1) {
                p_360515_.chainStates = new ArrayList<>(i1);

                for (int j1 = 0; j1 < i1; j1++) {
                    p_360515_.chainStates.add(new ScaffoldingRenderState.ChainState());
                }
            }
            BlockPos blockpos1 = BlockPos.containing(p_362733_.getPosition(p_361157_));
            BlockPos blockpos = BlockPos.containing(entity.getPosition(p_361157_));
            label83:
            {
                Vec3[] avec3 = p_362733_.getQuadChainOffsets();
                Vec3[] avec31 = entity.getQuadLeashHolderOffsets();
                Vec3 vec3 = entity.getPosition(p_361157_);
                int k1 = 0;

                while (true) {
                    if (k1 >= i1) {
                        break label83;
                    }
                    ScaffoldingRenderState.ChainState entityrenderstate$leashstate = p_360515_.chainStates.get(k1);
                    entityrenderstate$leashstate.offset = avec3[k1].yRot(-f);
                    entityrenderstate$leashstate.start = p_362733_.getPosition(p_361157_).add(entityrenderstate$leashstate.offset);
                    entityrenderstate$leashstate.end = vec3.add(avec31[k1].yRot(-f1));

                    entityrenderstate$leashstate.startBlockLight = this.getBlockLightLevel(p_362733_, blockpos1);
                    entityrenderstate$leashstate.endBlockLight = getBlockLightLevelTest(entity, blockpos);
                    entityrenderstate$leashstate.startSkyLight = p_362733_.level().getBrightness(LightLayer.SKY, blockpos1);
                    entityrenderstate$leashstate.endSkyLight = p_362733_.level().getBrightness(LightLayer.SKY, blockpos);
                    k1++;
                }
            }
        }
    }

    @Override
    protected void setupRotations(ScaffoldingRenderState p_364714_, PoseStack p_115318_, float p_115319_, float p_115320_) {
        super.setupRotations(p_364714_, p_115318_, p_115319_, p_115320_);
        p_115318_.translate(0.0F, -1F, 0.0F);
    }

    @Override
    public boolean shouldRender(Scaffolding p_114491_, Frustum p_114492_, double p_114493_, double p_114494_, double p_114495_) {
        if (super.shouldRender(p_114491_, p_114492_, p_114493_, p_114494_, p_114495_)) {
            return true;
        } else {
            if (p_114491_ instanceof Chainable leashable) {
                Entity entity = leashable.getChainHolder();
                if (entity != null) {
                    return p_114492_.isVisible(entity.getBoundingBox());
                }
            }

            return false;
        }
    }

    @Override
    public void submit(ScaffoldingRenderState p_433493_, PoseStack p_434615_, SubmitNodeCollector p_433768_, CameraRenderState p_450931_) {
        if (p_433493_.chainStates != null) {
            for (ScaffoldingRenderState.ChainState entityrenderstate$chainState : p_433493_.chainStates) {
                p_434615_.pushPose();
                renderChain(p_434615_, p_433768_, entityrenderstate$chainState);
                p_434615_.popPose();
            }
        }
        super.submit(p_433493_, p_434615_, p_433768_, p_450931_);
    }


    @Override
    public Identifier getTextureLocation(ScaffoldingRenderState p_114482_) {
        return TEXTURE;
    }

    private static void renderChain(PoseStack p_435977_, SubmitNodeCollector p_433388_, ScaffoldingRenderState.ChainState p_435610_) {
        float f = (float) (p_435610_.end.x - p_435610_.start.x);
        float f1 = (float) (p_435610_.end.y - p_435610_.start.y);
        float f2 = (float) (p_435610_.end.z - p_435610_.start.z);
        float f3 = Mth.invSqrt(f * f + f2 * f2) * 0.25F / 2.0F;
        float f4 = f2 * f3;
        float f5 = f * f3;
        p_435977_.pushPose();
        p_435977_.translate((float) p_435610_.offset.x, (float) p_435610_.offset.y, (float) p_435610_.offset.z);
        p_433388_.submitCustomGeometry(p_435977_, RenderTypes.entityCutoutNoCull(CHAIN_TEXTURE), (pose, vertexConsumer) -> {

            for (int i = 0; i <= 24; i++) {
                addVertexPair(vertexConsumer, pose, f, f1, f2, 0.5F, f4, f5, i, false, p_435610_);
            }

            for (int j = 24; j >= 0; j--) {
                addVertexPair(vertexConsumer, pose, f, f1, f2, 0.0F, f4, f5, j, true, p_435610_);
            }
        });
        p_435977_.popPose();
    }

    private static void addVertexPair(
            VertexConsumer p_434388_,
            PoseStack.Pose p_433395_,
            float p_433965_,
            float p_433396_,
            float p_433151_,
            float p_433167_,
            float p_433199_,
            float p_434961_,
            int p_436038_,
            boolean p_434670_,
            ScaffoldingRenderState.ChainState p_435559_
    ) {
        float f = p_436038_ / 24.0F;
        int i = (int) Mth.lerp(f, (float) p_435559_.startBlockLight, (float) p_435559_.endBlockLight);
        int j = (int) Mth.lerp(f, (float) p_435559_.startSkyLight, (float) p_435559_.endSkyLight);
        int k = LightTexture.pack(i, j);
        float f2 = 1F;
        float f3 = 1F;
        float f4 = 1F;
        float f5 = p_433965_ * f;
        float f6;
        /*if (p_435559_.slack) {
            f6 = p_433396_ > 0.0F ? p_433396_ * f * f : p_433396_ - p_433396_ * (1.0F - f) * (1.0F - f);
        } else {*/
        f6 = p_433396_ * f;
        //}

        float f7 = p_433151_ * f;
        p_434388_.addVertex(p_433395_, f5 - p_433167_, f6 - p_434961_, f7 - p_433199_).setColor(f2, f3, f4, 1.0F).setUv(0, 0).setLight(k)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(p_433395_, 0.0F, 1.0F, 0.0F);
        p_434388_.addVertex(p_433395_, f5 + p_433167_, f6 - p_434961_, f7 + p_433199_).setColor(f2, f3, f4, 1.0F).setUv(1, 0).setLight(k)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(p_433395_, 0.0F, 1.0F, 0.0F);
        p_434388_.addVertex(p_433395_, f5 + p_433167_, f6 + p_434961_, f7 - p_433199_).setColor(f2, f3, f4, 1.0F).setUv(1, 1).setLight(k)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(p_433395_, 0.0F, 1.0F, 0.0F);
        p_434388_.addVertex(p_433395_, f5 - p_433167_, f6 + p_434961_, f7 + p_433199_).setColor(f2, f3, f4, 1.0F).setUv(0, 1).setLight(k)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(p_433395_, 0.0F, 1.0F, 0.0F);
    }


    protected static int getBlockLightLevelTest(Entity p_114496_, BlockPos p_114497_) {
        return p_114496_.isOnFire() ? 15 : p_114496_.level().getBrightness(LightLayer.BLOCK, p_114497_);
    }
}
