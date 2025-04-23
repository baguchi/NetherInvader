package baguchan.nether_invader.client.render;

import baguchan.nether_invader.entity.AgressivePiglin;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.PiglinRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CrossbowItem;

public class AgressivePiglinRenderer extends HumanoidMobRenderer<AgressivePiglin, PiglinRenderState, PiglinModel> {
    private static final ResourceLocation PIGLIN_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/piglin/piglin.png");
    public static final CustomHeadLayer.Transforms PIGLIN_CUSTOM_HEAD_TRANSFORMS = new CustomHeadLayer.Transforms(0.0F, 0.0F, 1.0019531F);

    public AgressivePiglinRenderer(
            EntityRendererProvider.Context p_174344_,
            ModelLayerLocation p_174345_,
            ModelLayerLocation p_174346_,
            ModelLayerLocation p_174347_,
            ModelLayerLocation p_361802_,
            ModelLayerLocation p_360639_,
            ModelLayerLocation p_362581_
    ) {
        super(p_174344_, new PiglinModel(p_174344_.bakeLayer(p_174345_)), new PiglinModel(p_174344_.bakeLayer(p_174346_)), 0.5F, PIGLIN_CUSTOM_HEAD_TRANSFORMS);
        this.addLayer(
                new HumanoidArmorLayer<>(
                        this,
                        new HumanoidArmorModel(p_174344_.bakeLayer(p_174347_)),
                        new HumanoidArmorModel(p_174344_.bakeLayer(p_361802_)),
                        new HumanoidArmorModel(p_174344_.bakeLayer(p_360639_)),
                        new HumanoidArmorModel(p_174344_.bakeLayer(p_362581_)),
                        p_174344_.getEquipmentRenderer()
                )
        );
    }

    public ResourceLocation getTextureLocation(PiglinRenderState p_363784_) {
        return PIGLIN_LOCATION;
    }

    public PiglinRenderState createRenderState() {
        return new PiglinRenderState();
    }

    public void extractRenderState(AgressivePiglin p_361113_, PiglinRenderState p_364996_, float p_362352_) {
        super.extractRenderState(p_361113_, p_364996_, p_362352_);
        p_364996_.isBrute = p_361113_.getType() == EntityType.PIGLIN_BRUTE;
        p_364996_.armPose = p_361113_.getArmPose();
        p_364996_.maxCrossbowChageDuration = CrossbowItem.getChargeDuration(p_361113_.getUseItem(), p_361113_);
        p_364996_.isConverting = p_361113_.isConverting();
    }

    protected boolean isShaking(PiglinRenderState p_360965_) {
        return super.isShaking(p_360965_) || p_360965_.isConverting;
    }
}