package baguchan.nether_invader.client.render;

import baguchan.nether_invader.client.model.TestModel;
import baguchan.nether_invader.client.render.state.ScaffoldingRenderState;
import baguchan.nether_invader.entity.Chainable;
import baguchan.nether_invader.entity.Scaffolding;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

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
        if (entity != null) {
            float f = p_362733_.getPreciseBodyRotation(p_361157_) * (float) (Math.PI / 180.0);
            Vec3 vec3 = p_362733_.getLeashOffset(p_361157_).yRot(-f);
            BlockPos blockpos1 = BlockPos.containing(p_362733_.getPosition(p_361157_));
            BlockPos blockpos = BlockPos.containing(entity.getPosition(p_361157_));
            if (p_360515_.chainData == null) {
                p_360515_.chainData = new ScaffoldingRenderState.ChainState();
            }

            ScaffoldingRenderState.ChainState entityrenderstate$leashstate = p_360515_.chainData;
            entityrenderstate$leashstate.offset = vec3;
            entityrenderstate$leashstate.start = p_362733_.getPosition(p_361157_);
            entityrenderstate$leashstate.end = entity.getRopeHoldPosition(p_361157_);
            entityrenderstate$leashstate.startBlockLight = this.getBlockLightLevel(p_362733_, blockpos1);
            entityrenderstate$leashstate.endBlockLight = getBlockLightLevelTest(entity, blockpos);
            entityrenderstate$leashstate.startSkyLight = p_362733_.level().getBrightness(LightLayer.SKY, blockpos1);
            entityrenderstate$leashstate.endSkyLight = p_362733_.level().getBrightness(LightLayer.SKY, blockpos);
        } else {
            p_360515_.chainData = null;
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
    public void render(ScaffoldingRenderState p_115308_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_) {
        ScaffoldingRenderState.ChainState entity = p_115308_.chainData;
            if (entity != null) {
                this.renderChain(p_115308_, p_115311_, p_115312_, entity);
            }
        super.render(p_115308_, p_115311_, p_115312_, p_115313_);
    }

    @Override
    public ResourceLocation getTextureLocation(ScaffoldingRenderState p_114482_) {
        return TEXTURE;
    }

    public void renderChain(ScaffoldingRenderState chained, PoseStack poseStack, MultiBufferSource bufferIn, ScaffoldingRenderState.ChainState owner) {
        for (Vec3 vec34 : Chainable.QUAD_LEASH_ATTACHMENT_POINTS) {
            if (owner != null) {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = f1 * 0.5F % 1.0F;
                poseStack.pushPose();

                float yrot = -chained.yRot;

                Vec3 offset2 = new Vec3(vec34.x, 0, vec34.z).yRot(yrot * ((float) Math.PI / 180F));

                Vec3 offset = new Vec3(vec34.x * 2.455, -vec34.y, vec34.z * 2.455);
                poseStack.translate(offset2.x, vec34.y, offset2.z);
                Vec3 vec3 = owner.end.add(offset.yRot(yrot * ((float) Math.PI / 180F)));
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
                VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(CHAIN_TEXTURE));
                PoseStack.Pose posestack$pose = poseStack.last();
                vertex(vertexconsumer, posestack$pose, f19, f4, f20, j, k, l, 0.4999F, f30);
                vertex(vertexconsumer, posestack$pose, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
                vertex(vertexconsumer, posestack$pose, f21, 0.0F, f22, j, k, l, 0.0F, f29);
                vertex(vertexconsumer, posestack$pose, f21, f4, f22, j, k, l, 0.0F, f30);
                vertex(vertexconsumer, posestack$pose, f23, f4, f24, j, k, l, 0.4999F, f30);
                vertex(vertexconsumer, posestack$pose, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
                vertex(vertexconsumer, posestack$pose, f25, 0.0F, f26, j, k, l, 0.0F, f29);
                vertex(vertexconsumer, posestack$pose, f25, f4, f26, j, k, l, 0.0F, f30);
                float f31 = 0.0F;

                vertex(vertexconsumer, posestack$pose, f11, f4, f12, j, k, l, 0.0F, f31 - 0.15F);
                vertex(vertexconsumer, posestack$pose, f13, f4, f14, j, k, l, 1.15F, f31 - 0.15F);
                vertex(vertexconsumer, posestack$pose, f17, f4, f18, j, k, l, 1.15F, f31);
                vertex(vertexconsumer, posestack$pose, f15, f4, f16, j, k, l, -0.15F, f31);
                poseStack.popPose();
            }
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
