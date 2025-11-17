package baguchan.nether_invader.client.model;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchan.nether_invader.client.animations.BastionGeneralAnimations;
import baguchan.nether_invader.entity.BastionGeneral;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class BastionGeneralModel<T extends BastionGeneral> extends HierarchicalModel<T> implements ArmedModel {
    private final ModelPart root;
    private final ModelPart everything;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart head;
    private final ModelPart snout;
    private final ModelPart helmet;
    private final ModelPart leftEar;
    private final ModelPart rightEar;
    private final ModelPart leftEye;
    private final ModelPart rightEye;
    private final ModelPart front_cloth;
    private final ModelPart back_cloth;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public BastionGeneralModel(ModelPart root) {
        this.root = root;
        this.everything = root.getChild("everything");
        this.body = this.everything.getChild("body");
        this.leftArm = this.body.getChild("leftArm");
        this.rightArm = this.body.getChild("rightArm");
        this.head = this.body.getChild("head");
        this.snout = this.head.getChild("snout");
        this.helmet = this.head.getChild("helmet");
        this.leftEar = this.head.getChild("leftEar");
        this.rightEar = this.head.getChild("rightEar");
        this.leftEye = this.head.getChild("leftEye");
        this.rightEye = this.head.getChild("rightEye");
        this.front_cloth = this.body.getChild("front_cloth");
        this.back_cloth = this.body.getChild("back_cloth");
        this.leftLeg = this.everything.getChild("leftLeg");
        this.rightLeg = this.everything.getChild("rightLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition everything = partdefinition.addOrReplaceChild("everything", CubeListBuilder.create(), PartPose.offset(0.0F, 5.5F, 0.0F));

        PartDefinition body = everything.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 22).addBox(-8.0F, -19.0F, -7.0F, 16.0F, 19.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 77).addBox(-8.0F, -19.0F, -7.0F, 16.0F, 20.0F, 14.0F, new CubeDeformation(0.25F))
                .texOffs(64, 59).addBox(-3.0F, -6.5F, -9.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.5F, 0.0F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(104, 0).addBox(0.0F, -2.75F, -3.5F, 5.0F, 18.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -16.25F, -0.5F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(80, 0).addBox(-5.0F, -3.0F, -3.5F, 5.0F, 18.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -16.0F, -0.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -9.75F, -6.5F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.5F, -2.25F));

        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(37, 3).addBox(-3.0F, -2.25F, -2.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(5, 7).addBox(3.0F, -0.25F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 4).addBox(-4.0F, 0.75F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -6.5F));

        PartDefinition helmet = head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(0, 55).addBox(-6.0F, 0.25F, -6.5F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.25F))
                .texOffs(48, 51).addBox(0.0F, -2.5F, -7.5F, 0.0F, 9.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(65, 8).addBox(-0.6964F, 0.1228F, -3.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -6.25F, -0.5F, 0.0F, 0.0F, -0.6109F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(49, 8).addBox(-0.3036F, 0.1228F, -3.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -6.25F, -0.5F, 0.0F, 0.0F, 0.6109F));

        PartDefinition leftEye = head.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -0.01F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -3.75F, -6.5F));

        PartDefinition rightEye = head.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -0.01F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -3.75F, -6.5F));

        PartDefinition front_cloth = body.addOrReplaceChild("front_cloth", CubeListBuilder.create().texOffs(77, 85).addBox(-5.0F, 0.25F, 0.25F, 10.0F, 11.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -2.9F, -7.25F));

        PartDefinition back_cloth = body.addOrReplaceChild("back_cloth", CubeListBuilder.create().texOffs(106, 85).addBox(-5.0F, 0.25F, 0.25F, 10.0F, 11.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -2.85F, 7.25F));

        PartDefinition leftLeg = everything.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(63, 28).addBox(-3.5F, 0.0F, -4.0F, 7.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 9.5F, 0.0F));

        PartDefinition rightLeg = everything.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(93, 28).addBox(-3.5F, 0.0F, -4.0F, 7.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 9.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.attackAnimationState, BastionGeneralAnimations.swing, ageInTicks);
        this.animateWalk(BastionGeneralAnimations.walk, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        if (!entity.attackAnimationState.isStarted()) {
            this.animateWalk(BastionGeneralAnimations.idle, ageInTicks, 1.0F, 2.0F, 2.5F);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void translateToHand(HumanoidArm p_102925_, PoseStack p_102926_) {
        this.everything.translateAndRotate(p_102926_);
        this.body.translateAndRotate(p_102926_);
        this.getArm(p_102925_).translateAndRotate(p_102926_);
    }

    private ModelPart getArm(HumanoidArm p_102923_) {
        return p_102923_ == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }
}