package baguchan.nether_invader.client.model;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchan.nether_invader.client.animation.PiglinWarriorAnimations;
import baguchan.nether_invader.client.render.state.PiglinWarriorRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class PiglinWarriorModel<T extends PiglinWarriorRenderState> extends EntityModel<T> implements ArmedModel<T> {

	private final ModelPart everything;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart snout;
	private final ModelPart leftEar;
	private final ModelPart rightEar;
	private final ModelPart rightEye;
	private final ModelPart leftEye;
	private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final KeyframeAnimation idleAnimation;
	private final KeyframeAnimation walkAnimation;
	private final KeyframeAnimation attackAnimation;
	private final KeyframeAnimation bartingAnimation;

	public PiglinWarriorModel(ModelPart root) {
        super(root);
		this.everything = root.getChild("everything");
		this.body = this.everything.getChild("body");
		this.head = this.body.getChild("head");
		this.snout = this.head.getChild("snout");
		this.leftEar = this.head.getChild("leftEar");
		this.rightEar = this.head.getChild("rightEar");
		this.rightEye = this.head.getChild("rightEye");
		this.leftEye = this.head.getChild("leftEye");
		this.leftArm = this.body.getChild("leftArm");
		this.rightArm = this.body.getChild("rightArm");
		this.leftLeg = this.everything.getChild("leftLeg");
		this.rightLeg = this.everything.getChild("rightLeg");
		this.idleAnimation = PiglinWarriorAnimations.idle.bake(root);
		this.attackAnimation = PiglinWarriorAnimations.swing.bake(root);
		this.walkAnimation = PiglinWarriorAnimations.walk.bake(root);
		this.bartingAnimation = PiglinWarriorAnimations.barter_loop.bake(root);
	}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition everything = partdefinition.addOrReplaceChild("everything", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 0.0F));

        PartDefinition body = everything.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-7.0F, -15.0F, -4.0F, 14.0F, 15.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 88).addBox(-5.0F, -11.0F, 6.0F, 10.0F, 11.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 117).addBox(-5.0F, -22.0F, 8.5F, 10.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(19, 117).addBox(-5.0F, -19.0F, 9.5F, 10.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(19, 117).mirror().addBox(-5.0F, -21.0F, 7.5F, 10.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 41).addBox(-7.0F, -15.0F, -4.0F, 14.0F, 21.0F, 10.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(28, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(2.0F, 0.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(-3.0F, 0.0F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -6.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(50, 0).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, -6.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition rightEye = head.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(126, 127).addBox(-0.5F, -1.0F, -0.01F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -3.0F, -4.0F));

        PartDefinition leftEye = head.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(126, 127).addBox(-0.5F, -1.0F, -0.01F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -3.0F, -4.0F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(108, 0).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -14.0F, 1.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(88, 0).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, -14.0F, 1.0F));

        PartDefinition leftLeg = everything.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(80, 27).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 1.0F));

        PartDefinition rightLeg = everything.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(104, 27).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(PiglinWarriorRenderState entity) {
		if (entity.isRiding) {
			this.rightArm.xRot = (-(float) Math.PI / 5F);
			this.rightArm.yRot = 0.0F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.xRot = (-(float) Math.PI / 5F);
			this.leftArm.yRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float) Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float) Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		} else {
			this.walkAnimation.applyWalk(entity.walkAnimationPos, entity.walkAnimationSpeed, 1.0F, 2.5F);
		}

		this.attackAnimation.apply(entity.attackAnimationState, entity.ageInTicks);
		if (!entity.idle) {
			this.idleAnimation.applyWalk(entity.ageInTicks, 1.0F, 2.0F, 2.5F);
		}
		if (entity.barting) {
			this.rightArm.resetPose();
			this.leftArm.resetPose();
			this.head.resetPose();

			this.bartingAnimation.applyWalk(entity.ageInTicks, 1.0F, 1.0F, 1.0F);
		}
	}

	private ModelPart getArm(HumanoidArm p_102923_) {
		return p_102923_ == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
	}

	@Override
	public void translateToHand(T entityRenderState, HumanoidArm humanoidArm, PoseStack poseStack) {
		this.everything.translateAndRotate(poseStack);
		this.body.translateAndRotate(poseStack);

		this.getArm(humanoidArm).translateAndRotate(poseStack);
	}
}