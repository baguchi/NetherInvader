package baguchan.nether_invader.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class TestModel extends EntityModel<LivingEntityRenderState> {
    private static final String LEFT_PADDLE = "left_paddle";
    private static final String RIGHT_PADDLE = "right_paddle";
    private static final String BOTTOM = "bottom";
    private final ModelPart leftPaddle;
    private final ModelPart rightPaddle;
    private final ImmutableList<ModelPart> parts;

    public TestModel(ModelPart p_251383_) {
        super(p_251383_);
        this.leftPaddle = p_251383_.getChild("left_paddle");
        this.rightPaddle = p_251383_.getChild("right_paddle");
        this.parts = this.createPartsBuilder(p_251383_).build();
    }

    protected ImmutableList.Builder<ModelPart> createPartsBuilder(ModelPart p_250773_) {
        ImmutableList.Builder<ModelPart> builder = new ImmutableList.Builder<>();
        builder.add(p_250773_.getChild("bottom"), this.leftPaddle, this.rightPaddle);
        return builder;
    }

    public static void createChildren(PartDefinition p_250262_) {
        p_250262_.addOrReplaceChild(
                "bottom",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-14.0F, -11.0F, -4.0F, 28.0F, 20.0F, 4.0F)
                        .texOffs(0, 0)
                        .addBox(-14.0F, -9.0F, -8.0F, 28.0F, 16.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, -2.1F, 1.0F, 1.5708F, 0.0F, 0.0F)
        );
        int i = 20;
        int j = 7;
        int k = 6;
        float f = -5.0F;
        p_250262_.addOrReplaceChild(
                "left_paddle",
                CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(-1.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F),
                PartPose.offsetAndRotation(3.0F, -4.0F, 9.0F, 0.0F, 0.0F, (float) (Math.PI / 16))
        );
        p_250262_.addOrReplaceChild(
                "right_paddle",
                CubeListBuilder.create().texOffs(40, 24).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F),
                PartPose.offsetAndRotation(3.0F, -4.0F, -9.0F, 0.0F, (float) Math.PI, (float) (Math.PI / 16))
        );
    }

    public static LayerDefinition createBodyModel() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        createChildren(partdefinition);
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(LivingEntityRenderState p_364104_) {
        super.setupAnim(p_364104_);
        this.leftPaddle.skipDraw = true;
        this.rightPaddle.skipDraw = true;
    }

    public ImmutableList<ModelPart> parts() {
        return this.parts;
    }
}
