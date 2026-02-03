package baguchi.spiced_twilight.mixin.client;

import baguchi.spiced_twilight.attachment.ModAttachments;
import baguchi.spiced_twilight.entity.goal.HidingGoal;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.model.entity.HelmetCrabModel;
import twilightforest.entity.monster.HelmetCrab;

import java.util.Optional;

@Mixin(HelmetCrabModel.class)
public abstract class HelmetCrabModelMixin extends HierarchicalModel<HelmetCrab> {

    @Shadow
    public abstract ModelPart root();

    @Inject(method = "setupAnim(Ltwilightforest/entity/monster/HelmetCrab;FFFFF)V", at = @At("HEAD"))
    protected void setAnim(HelmetCrab entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if(entity.getData(ModAttachments.HELMET_CRAB_HIDE).isHide()){
            this.root().getAllParts().forEach((part) -> {part.skipDraw = true;});
            Optional<ModelPart> optional = this.getAnyDescendantWithName("helmet");
            if(optional.isPresent()){
                optional.get().skipDraw = false;
                optional.get().resetPose();
                optional.get().z += 1F;
            }

            Optional<ModelPart> optional2 = this.getAnyDescendantWithName("horns");
            if(optional2.isPresent()){
                optional2.get().skipDraw = false;
                optional2.get().resetPose();
                optional2.get().z += 1F;
            }

            Optional<ModelPart> optional3 = this.getAnyDescendantWithName("right_horn_1");
            if(optional3.isPresent()){
                optional3.get().skipDraw = false;
                optional3.get().resetPose();
                optional3.get().z += 1F;
            }

            Optional<ModelPart> optional4 = this.getAnyDescendantWithName("left_horn_1");
            if(optional4.isPresent()){
                optional4.get().skipDraw = false;
                optional4.get().resetPose();
                optional4.get().z += 1F;
            }
        }else {
            this.root().getAllParts().forEach((part) -> {part.skipDraw = false;});
            Optional<ModelPart> optional = this.getAnyDescendantWithName("helmet");
            if(optional.isPresent()){
                optional.get().resetPose();
            }

            Optional<ModelPart> optional2 = this.getAnyDescendantWithName("horns");
            if(optional2.isPresent()){
                optional2.get().resetPose();
            }

            Optional<ModelPart> optional3 = this.getAnyDescendantWithName("right_horn_1");
            if(optional3.isPresent()){
                optional3.get().resetPose();
            }

            Optional<ModelPart> optional4 = this.getAnyDescendantWithName("left_horn_1");
            if(optional4.isPresent()){
                optional4.get().resetPose();
            }
        }
    }
}
