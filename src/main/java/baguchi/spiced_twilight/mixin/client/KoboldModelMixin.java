package baguchi.spiced_twilight.mixin.client;

import baguchi.spiced_twilight.api.ISpicedArmor;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.model.entity.KoboldModel;
import twilightforest.entity.monster.Kobold;

@Mixin(KoboldModel.class)
public abstract class KoboldModelMixin extends HumanoidModel<Kobold> implements ISpicedArmor<Kobold> {
    public KoboldModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim(Ltwilightforest/entity/monster/Kobold;FFFFF)V", at = @At("TAIL"))
    public void setupAnim(Kobold entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        this.head.resetPose();
        if (entity.isBaby()) {
            this.head.y -= 4.0F;
        }
    }

    @Override
    public void translateToHead(Kobold kobold, ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
        poseStack.scale(0.8F, 0.8F, 0.8F);
    }

    @Override
    public void translateToChest(Kobold kobold, ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
        poseStack.scale(0.85F, 0.75F, 0.85F);
        poseStack.translate((4 / 16F), -(4 / 16F), (2 / 16F));
    }

    @Override
    public void translateToChestPat(Kobold kobold, ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
        poseStack.scale(0.75F, 0.6F, 0.75F);
    }

    @Override
    public void translateToLeg(Kobold kobold, ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
        poseStack.scale(0.75F, 0.6F, 0.75F);
    }

    @Override
    public Iterable<ModelPart> headPartArmors() {
        return ImmutableList.of(this.head);
    }

    @Override
    public Iterable<ModelPart> bodyPartArmors() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<ModelPart> rightHandArmors() {
        return ImmutableList.of(this.rightArm);
    }

    @Override
    public Iterable<ModelPart> leftHandArmors() {
        return ImmutableList.of(this.leftArm);
    }

    @Override
    public Iterable<ModelPart> rightLegPartArmors() {
        return ImmutableList.of(this.rightLeg);
    }

    @Override
    public Iterable<ModelPart> leftLegPartArmors() {
        return ImmutableList.of(this.leftLeg);
    }
}
