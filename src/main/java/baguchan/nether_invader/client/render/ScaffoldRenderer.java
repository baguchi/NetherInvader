package baguchan.nether_invader.client.render;

import baguchan.nether_invader.client.model.TestModel;
import baguchan.nether_invader.client.render.state.ScaffoldingRenderState;
import baguchan.nether_invader.entity.Chainable;
import baguchan.nether_invader.entity.Scaffolding;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class ScaffoldRenderer extends LivingEntityRenderer<Scaffolding, ScaffoldingRenderState, TestModel> {
    public static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/boat/bamboo.png");
    public static final ResourceLocation CHAIN_TEXTURE = ResourceLocation.withDefaultNamespace("textures/item/chain.png");

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
                renderChain(p_433493_, p_434615_, p_433768_, entityrenderstate$chainState);
            }
        }
        super.submit(p_433493_, p_434615_, p_433768_, p_450931_);
    }


    @Override
    public ResourceLocation getTextureLocation(ScaffoldingRenderState p_114482_) {
        return TEXTURE;
    }

    public void renderChain(ScaffoldingRenderState chained, PoseStack poseStack, SubmitNodeCollector bufferIn, ScaffoldingRenderState.ChainState owner) {
            if (owner != null) {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = f1 * 0.5F % 1.0F;
                poseStack.pushPose();

                float yrot = -chained.yRot;

                Vec3 vec3 = owner.end;
                Vec3 vec31 = owner.start;
                Vec3 vec32 = vec3.subtract(vec31);
                float f4 = (float) (vec32.length());
                vec32 = vec32.normalize();
                float f5 = (float) Math.acos(vec32.y);
                float f6 = (float) Math.atan2(vec32.z, vec32.x);
                poseStack.mulPose(Axis.YP.rotationDegrees(((float) (Math.PI / 2) - f6) * (180.0F / (float) Math.PI)));
                poseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180.0F / (float) Math.PI)));
                //poseStack.mulPose(Axis.ZP.rotationDegrees(owner.getYRot()));
                int i = 1;
                float f7 = f1 * 0.05F * -1.5F;
                float f8 = f * f;
                int j = 64 + (int) (f8 * 191.0F);
                int k = 32 + (int) (f8 * 191.0F);
                int l = 128 - (int) (f8 * 64.0F);
                float f9 = 0.2F;
                float f10 = 0.282F;
                float f11 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
                float f12 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
                float f13 = Mth.cos(f7 + (float) (Math.PI / 4)) * 0.282F;
                float f14 = Mth.sin(f7 + (float) (Math.PI / 4)) * 0.282F;
                float f15 = Mth.cos(f7 + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
                float f16 = Mth.sin(f7 + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
                float f17 = Mth.cos(f7 + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
                float f18 = Mth.sin(f7 + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
                float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
                float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
                float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
                float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
                float f23 = Mth.cos(f7 + (float) (Math.PI / 2)) * 0.2F;
                float f24 = Mth.sin(f7 + (float) (Math.PI / 2)) * 0.2F;
                float f25 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
                float f26 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
                float f27 = 0.0F;
                float f28 = 0.4999F;
                float f29 = -1.0F + f2;
                float f30 = f4 * 2.5F + f29;
                bufferIn.submitCustomGeometry(poseStack, RenderType.entityCutoutNoCull(CHAIN_TEXTURE), (pose, vertexConsumer) -> {
                    PoseStack.Pose posestack$pose = pose;
                    vertex(vertexConsumer, posestack$pose, f19, f4, f20, j, k, l, 0.4999F, f30);
                    vertex(vertexConsumer, posestack$pose, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
                    vertex(vertexConsumer, posestack$pose, f21, 0.0F, f22, j, k, l, 0.0F, f29);
                    vertex(vertexConsumer, posestack$pose, f21, f4, f22, j, k, l, 0.0F, f30);
                    vertex(vertexConsumer, posestack$pose, f23, f4, f24, j, k, l, 0.4999F, f30);
                    vertex(vertexConsumer, posestack$pose, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
                    vertex(vertexConsumer, posestack$pose, f25, 0.0F, f26, j, k, l, 0.0F, f29);
                    vertex(vertexConsumer, posestack$pose, f25, f4, f26, j, k, l, 0.0F, f30);
                    float f31 = 0.0F;

                    vertex(vertexConsumer, posestack$pose, f11, f4, f12, j, k, l, 0.0F, f31 - 0.15F);
                    vertex(vertexConsumer, posestack$pose, f13, f4, f14, j, k, l, 1.15F, f31 - 0.15F);
                    vertex(vertexConsumer, posestack$pose, f17, f4, f18, j, k, l, 1.15F, f31);
                    vertex(vertexConsumer, posestack$pose, f15, f4, f16, j, k, l, -0.15F, f31);

                });
                poseStack.popPose();
            }
    }

    private static void vertex(
            VertexConsumer p_253637_,
            PoseStack.Pose p_323627_,
            float p_253994_,
            float p_254492_,
            float p_254474_,
            int p_254080_,
            int p_253655_,
            int p_254133_,
            float p_254233_,
            float p_253939_
    ) {
        p_253637_.addVertex(p_323627_, p_253994_, p_254492_, p_254474_)
                .setColor(p_254080_, p_253655_, p_254133_, 255)
                .setUv(p_254233_, p_253939_)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(p_323627_, 0.0F, 1.0F, 0.0F);
    }


    protected static int getBlockLightLevelTest(Entity p_114496_, BlockPos p_114497_) {
        return p_114496_.isOnFire() ? 15 : p_114496_.level().getBrightness(LightLayer.BLOCK, p_114497_);
    }
}
