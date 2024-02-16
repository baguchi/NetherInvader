package baguchan.nether_invader.client.model;// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class NetherSpreaderModel<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart bone;

    public NetherSpreaderModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(196, 48).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(196, 16).addBox(-8.0F, -32.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 196).addBox(-8.0F, -48.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(192, 164).addBox(-8.0F, -64.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(144, 180).addBox(-8.0F, -80.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(180, 84).addBox(-8.0F, -96.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(244, 64).addBox(8.0F, -32.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(244, 32).addBox(8.0F, -16.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(32, 244).addBox(8.0F, -48.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(244, 0).addBox(8.0F, -64.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(240, 180).addBox(8.0F, -80.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(224, 236).addBox(8.0F, -96.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(228, 100).addBox(-16.0F, -80.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 228).addBox(-16.0F, -96.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(192, 220).addBox(-16.0F, -64.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 220).addBox(-16.0F, -48.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(144, 212).addBox(-16.0F, -32.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 212).addBox(-16.0F, -16.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(216, 276).addBox(-8.0F, -16.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(276, 96).addBox(-8.0F, -32.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(48, 276).addBox(-8.0F, -48.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 276).addBox(-8.0F, -96.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(272, 236).addBox(-8.0F, -80.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(120, 268).addBox(-8.0F, -64.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(266, 132).addBox(-8.0F, -96.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(264, 260).addBox(-8.0F, -80.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(176, 260).addBox(-8.0F, -64.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(80, 252).addBox(-8.0F, -48.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(248, 212).addBox(-8.0F, -32.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(136, 244).addBox(-8.0F, -16.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(48, 180).addBox(-8.0F, -112.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 164).addBox(-8.0F, -128.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 164).addBox(-24.0F, -112.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(154, 116).addBox(-8.0F, -128.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(144, 148).addBox(8.0F, -128.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 148).addBox(8.0F, -128.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(148, 32).addBox(-8.0F, -128.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(148, 0).addBox(8.0F, -128.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 132).addBox(-8.0F, -112.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(202, 132).addBox(8.0F, -112.0F, -24.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(132, 68).addBox(8.0F, -112.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 132).addBox(8.0F, -112.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-8.0F, -112.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(192, 196).addBox(-24.0F, -112.0F, 8.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(106, 100).addBox(-24.0F, -128.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-24.0F, -128.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-24.0F, -128.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 196).addBox(-24.0F, -112.0F, -8.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(196, 0).addBox(-24.0F, -104.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(240, 156).addBox(-16.0F, -104.0F, -8.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(74, 42).addBox(-11.0F, -136.0F, -30.0F, 22.0F, 27.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(74, 0).addBox(-11.0F, -136.0F, 15.0F, 22.0F, 27.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(-31.0F, -136.0F, -11.0F, 15.0F, 27.0F, 22.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(15.0F, -136.0F, -11.0F, 15.0F, 27.0F, 22.0F, new CubeDeformation(0.0F))
                .texOffs(264, 284).addBox(-6.0F, -131.0F, -30.0F, 12.0F, 17.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(186, 284).addBox(-6.0F, -131.0F, 27.0F, 12.0F, 17.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(156, 280).addBox(27.0F, -131.0F, -6.0F, 3.0F, 17.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(96, 280).addBox(-31.0F, -131.0F, -6.0F, 3.0F, 17.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.bone;
    }
}