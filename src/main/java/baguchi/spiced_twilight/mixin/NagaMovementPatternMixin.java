package baguchi.spiced_twilight.mixin;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.LongJumpUtil;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.ai.goal.NagaMovementPattern;
import twilightforest.entity.boss.Naga;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mixin(NagaMovementPattern.class)
public abstract class NagaMovementPatternMixin {
    @Unique
    private static final List<Integer> ALLOWED_ANGLES = Lists.newArrayList(55, 60, 65, 70, 75, 80);

    @Shadow
    @Final
    private Naga naga;

    @Unique
    private int spicedTwilight$groundTick;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tickTail(CallbackInfo ci) {
        if (this.naga.onGround() && spicedTwilight$groundTick++ > 1) {
            this.naga.setDiscardFriction(false);
        }
    }

    @Inject(method = "doCharge", at = @At("TAIL"))
    private void doCharge(boolean stunless, CallbackInfo ci) {
        if (this.naga.getTarget() != null && !stunless && this.naga.getRandom().nextBoolean()) {
            Vec3 vec3 = spicedTwilight$calculateOptimalJumpVector(this.naga, this.naga.getTarget().position());
            if (vec3 != null) {
                this.naga.setYRot(this.naga.yBodyRot);
                this.naga.setDiscardFriction(true);
                double d0 = vec3.length();
                double d1 = d0 + (double) this.naga.getJumpBoostPower();
                this.naga.setDeltaMovement(vec3.scale(d1 / d0));
                this.spicedTwilight$groundTick = 0;
            }
        }
    }

    @Unique
    @Nullable
    protected Vec3 spicedTwilight$calculateOptimalJumpVector(Mob mob, Vec3 target) {
        List<Integer> list = Lists.newArrayList(ALLOWED_ANGLES);
        Collections.shuffle(list);
        float f = (float) (mob.getAttributeValue(Attributes.JUMP_STRENGTH) * (double) 3F);

        for (int i : list) {
            Optional<Vec3> optional = LongJumpUtil.calculateJumpVectorForAngle(mob, target, f, i, true);
            if (optional.isPresent()) {
                return optional.get();
            }
        }

        return null;
    }
}
