package baguchan.nether_invader.client.model;// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class NetherSpreaderModel<T extends Entity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart bone;
    private final ModelPart bone2;

    public NetherSpreaderModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.bone2 = root.getChild("bone2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 100).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -32.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -48.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -64.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -80.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -96.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -32.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -16.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -48.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -64.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -80.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -96.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -80.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -96.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -64.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -48.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -32.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -16.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -16.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -32.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -48.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -96.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -80.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -64.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -96.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -80.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -64.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -48.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -32.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -16.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -112.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -128.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-24.0F, -112.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -128.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(8.0F, -128.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(8.0F, -128.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-8.0F, -128.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(8.0F, -128.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-8.0F, -112.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(106, 100).addBox(8.0F, -112.0F, -24.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(8.0F, -112.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(8.0F, -112.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-8.0F, -112.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(106, 100).addBox(-24.0F, -112.0F, 8.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-24.0F, -128.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-24.0F, -128.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-24.0F, -128.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(106, 100).addBox(-24.0F, -112.0F, -8.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(106, 84).addBox(-24.0F, -104.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 132).addBox(-16.0F, -104.0F, -8.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(75, 42).addBox(-10.5F, -136.0F, -30.0F, 21.0F, 27.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(75, 0).addBox(-10.5F, -136.0F, 15.0F, 21.0F, 27.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(1, 50).addBox(-31.0F, -136.0F, -10.5F, 15.0F, 27.0F, 21.0F, new CubeDeformation(0.0F))
                .texOffs(1, 1).addBox(15.0F, -136.0F, -10.5F, 15.0F, 27.0F, 21.0F, new CubeDeformation(0.0F))
                .texOffs(228, 236).addBox(-5.5F, -131.0F, -30.0F, 11.0F, 17.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(228, 216).addBox(-5.5F, -131.0F, 27.0F, 11.0F, 17.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(228, 160).addBox(27.0F, -131.0F, -5.5F, 3.0F, 17.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(228, 188).addBox(-31.0F, -131.0F, -5.5F, 3.0F, 17.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 100).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -32.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -48.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -64.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -80.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -96.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -32.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -16.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -48.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -64.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -80.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 124).addBox(8.0F, -96.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -80.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -96.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -64.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -48.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -32.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(48, 116).addBox(-16.0F, -16.0F, -8.0F, 8.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -16.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -32.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -48.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -96.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -80.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(144, 124).addBox(-8.0F, -64.0F, -16.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -96.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -80.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -64.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -48.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -32.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(140, 76).addBox(-8.0F, -16.0F, 8.0F, 16.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -112.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -128.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-24.0F, -112.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(-8.0F, -128.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(8.0F, -128.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 100).addBox(8.0F, -128.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-8.0F, -128.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(8.0F, -128.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-8.0F, -112.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(106, 100).addBox(8.0F, -112.0F, -24.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(8.0F, -112.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(8.0F, -112.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-8.0F, -112.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(106, 100).addBox(-24.0F, -112.0F, 8.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-24.0F, -128.0F, -24.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-24.0F, -128.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(58, 84).addBox(-24.0F, -128.0F, 8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(106, 100).addBox(-24.0F, -112.0F, -8.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(106, 84).addBox(-24.0F, -104.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 132).addBox(-16.0F, -104.0F, -8.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(75, 42).addBox(-10.5F, -136.0F, -30.0F, 21.0F, 27.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(75, 0).addBox(-10.5F, -136.0F, 15.0F, 21.0F, 27.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(1, 50).addBox(-31.0F, -136.0F, -10.5F, 15.0F, 27.0F, 21.0F, new CubeDeformation(0.0F))
                .texOffs(1, 1).addBox(15.0F, -136.0F, -10.5F, 15.0F, 27.0F, 21.0F, new CubeDeformation(0.0F))
                .texOffs(228, 236).addBox(-5.5F, -131.0F, -30.0F, 11.0F, 17.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(228, 216).addBox(-5.5F, -131.0F, 27.0F, 11.0F, 17.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(228, 160).addBox(27.0F, -131.0F, -5.5F, 3.0F, 17.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(228, 188).addBox(-31.0F, -131.0F, -5.5F, 3.0F, 17.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }


    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        bone2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

}