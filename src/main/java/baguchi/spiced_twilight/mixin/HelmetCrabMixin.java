package baguchi.spiced_twilight.mixin;

import baguchi.spiced_twilight.entity.goal.HidingGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.monster.HelmetCrab;

@Mixin(HelmetCrab.class)
public abstract class HelmetCrabMixin extends Monster {
    protected HelmetCrabMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    protected void registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(0, new HidingGoal((HelmetCrab) (Object)this));
    }
}
