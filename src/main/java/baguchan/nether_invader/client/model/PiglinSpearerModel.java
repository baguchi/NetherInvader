package baguchan.nether_invader.client.model;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchan.nether_invader.client.animation.PiglinSpearerAnimations;
import baguchan.nether_invader.client.render.state.PiglinSpearerRenderState;
import baguchi.bagus_lib.client.layer.CustomArmorRender;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.HumanoidArm;

public class PiglinSpearerModel<T extends PiglinSpearerRenderState> extends EntityModel<T> implements ArmedModel<T>, CustomArmorRender<T> {
    private final ModelPart root;
    private final ModelPart body_main;
    private final ModelPart body;
    private final ModelPart head_ext;
    private final ModelPart head;
    private final ModelPart ears;
    private final ModelPart ear_left;
    private final ModelPart ear_right;
    private final ModelPart leftEye;
    private final ModelPart rightEye;
    private final ModelPart arms;
    private final ModelPart arm_right;
    private final ModelPart rightItem;
    private final ModelPart root_item;
    private final ModelPart arm_left;
    private final ModelPart leftItem;
    private final ModelPart legs;
    private final ModelPart leg_right;
    private final ModelPart leg_left;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation walkAnimation;
    private final KeyframeAnimation attackAnimation;

    public PiglinSpearerModel(ModelPart root) {
        super(root);
        this.root = root.getChild("root");
        this.body_main = this.root.getChild("body_main");
        this.body = this.body_main.getChild("body");
        this.head_ext = this.body.getChild("head_ext");
        this.head = this.head_ext.getChild("head");
        this.ears = this.head.getChild("ears");
        this.ear_left = this.ears.getChild("ear_left");
        this.ear_right = this.ears.getChild("ear_right");
        this.leftEye = this.head.getChild("leftEye");
        this.rightEye = this.head.getChild("rightEye");
        this.arms = this.body.getChild("arms");
        this.arm_right = this.arms.getChild("arm_right");
        this.rightItem = this.arm_right.getChild("rightItem");
        this.root_item = this.arm_right.getChild("root_item");
        this.arm_left = this.arms.getChild("arm_left");
        this.leftItem = this.arm_left.getChild("leftItem");
        this.legs = this.body_main.getChild("legs");
        this.leg_right = this.legs.getChild("leg_right");
        this.leg_left = this.legs.getChild("leg_left");
        this.idleAnimation = PiglinSpearerAnimations.idle.bake(root);
        this.attackAnimation = PiglinSpearerAnimations.spear_melee.bake(root);
        this.walkAnimation = PiglinSpearerAnimations.walk.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body_main = root.addOrReplaceChild("body_main", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition body = body_main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-5.0F, -12.0F, -3.0F, 10.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(16, 34).addBox(-5.0F, -12.0F, -3.0F, 10.0F, 19.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_ext = body.addOrReplaceChild("head_ext", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition head = head_ext.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -7.0F, -4.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(31, 1).addBox(-2.0F, -3.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(2, 4).addBox(2.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(2, 0).addBox(-3.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition ear_left = ears.addOrReplaceChild("ear_left", CubeListBuilder.create().texOffs(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition ear_right = ears.addOrReplaceChild("ear_right", CubeListBuilder.create().texOffs(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition leftEye = head.addOrReplaceChild("leftEye", CubeListBuilder.create().texOffs(6, 7).addBox(-0.5F, -1.0F, -0.01F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -3.0F, -4.0F));

        PartDefinition rightEye = head.addOrReplaceChild("rightEye", CubeListBuilder.create().texOffs(6, 7).addBox(-0.5F, -1.0F, -0.01F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -3.0F, -4.0F));

        PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition arm_right = arms.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(48, 16).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 0.0F, 0.0F));

        PartDefinition rightItem = arm_right.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition root_item = arm_right.addOrReplaceChild("root_item", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.0F, 1.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition arm_left = arms.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(32, 59).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 0.0F, 0.0F));

        PartDefinition leftItem = arm_left.addOrReplaceChild("leftItem", CubeListBuilder.create(), PartPose.offset(-14.0F, 9.0F, 0.0F));

        PartDefinition legs = body_main.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg_right = legs.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

        PartDefinition leg_left = legs.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(16, 59).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 80, 80);
    }

    @Override
    public void setupAnim(T entity) {
        super.setupAnim(entity);
        if (entity.isRiding) {
            this.arm_right.xRot = (-(float) Math.PI / 5F);
            this.arm_right.yRot = 0.0F;
            this.arm_right.zRot = 0.0F;
            this.arm_left.xRot = (-(float) Math.PI / 5F);
            this.arm_left.yRot = 0.0F;
            this.arm_left.zRot = 0.0F;
            this.leg_right.xRot = -1.4137167F;
            this.leg_right.yRot = ((float) Math.PI / 10F);
            this.leg_right.zRot = 0.07853982F;
            this.leg_left.xRot = -1.4137167F;
            this.leg_left.yRot = (-(float) Math.PI / 10F);
            this.leg_left.zRot = -0.07853982F;
        } else {
            this.walkAnimation.applyWalk(entity.walkAnimationPos, entity.walkAnimationSpeed, 2.0F, 2.5F);
        }

        this.attackAnimation.apply(entity.attackAnimationState, entity.ageInTicks, 2.0F);
        if (entity.idle) {
            this.idleAnimation.applyWalk(entity.ageInTicks, 1.0F, 2.0F, 2.5F);
        }
        if (entity.barting) {
            this.arm_right.resetPose();
            this.arm_left.resetPose();
            this.head.resetPose();
            this.head.xRot = 0.5F;
            this.head.yRot = 0.0F;
            if (entity.mainArm == HumanoidArm.LEFT) {
                this.arm_right.yRot = -0.5F;
                this.arm_right.xRot = -0.9F;
            } else {
                this.arm_left.yRot = 0.5F;
                this.arm_left.xRot = -0.9F;
            }
        }
    }

    @Override
    public void translateToHand(T state, HumanoidArm arm, PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.body_main.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        this.arms.translateAndRotate(poseStack);
        if (arm == HumanoidArm.RIGHT) {
            this.arm_right.translateAndRotate(poseStack);
        } else {
            this.arm_left.translateAndRotate(poseStack);
        }
    }

    @Override
    public void translateToHead(T t, ModelPart modelPart, PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.body_main.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        this.head_ext.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChest(T t, ModelPart modelPart, PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.body_main.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToLeg(T t, ModelPart modelPart, PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.body_main.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        this.legs.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChestPat(T t, ModelPart modelPart, PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.body_main.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        this.arms.translateAndRotate(poseStack);
        modelPart.translateAndRotate(poseStack);
    }

    @Override
    public Iterable<ModelPart> rightHandArmors() {
        return ImmutableList.of(this.arm_right);
    }

    @Override
    public Iterable<ModelPart> leftHandArmors() {
        return ImmutableList.of(this.arm_left);
    }

    @Override
    public Iterable<ModelPart> rightLegPartArmors() {
        return ImmutableList.of(this.leg_right);
    }

    @Override
    public Iterable<ModelPart> leftLegPartArmors() {
        return ImmutableList.of(this.leg_left);
    }

    @Override
    public Iterable<ModelPart> bodyPartArmors() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<ModelPart> headPartArmors() {
        return ImmutableList.of(this.head);
    }
}
