package baguchan.nether_invader.client.render;

import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;

public class AgressivePiglinRenderer extends HumanoidMobRenderer<Mob, PiglinModel<Mob>> {
    private static final ResourceLocation TEXTURES = ResourceLocation.withDefaultNamespace("textures/entity/piglin/piglin.png");

    private static final float PIGLIN_CUSTOM_HEAD_SCALE = 1.0019531F;

    public AgressivePiglinRenderer(
            EntityRendererProvider.Context p_174344_
    ) {
        super(p_174344_, new PiglinModel<>(p_174344_.bakeLayer(ModelLayers.PIGLIN)), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(
                new HumanoidArmorLayer<>(
                        this,
                        new HumanoidArmorModel(p_174344_.bakeLayer(ModelLayers.PIGLIN_INNER_ARMOR)),
                        new HumanoidArmorModel(p_174344_.bakeLayer(ModelLayers.PIGLIN_OUTER_ARMOR)),
                        p_174344_.getModelManager()
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(Mob p_115708_) {
        return TEXTURES;
    }

    @Override
    protected boolean isShaking(Mob p_115712_) {
        return super.isShaking(p_115712_) || p_115712_ instanceof AbstractPiglin && ((AbstractPiglin) p_115712_).isConverting();
    }
}