package baguchi.spiced_twilight.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.ai.goal.LichAbsorbMinionsGoal;
import twilightforest.entity.boss.Lich;
import twilightforest.entity.monster.LichMinion;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;
import twilightforest.item.LifedrainScepterItem;
import twilightforest.util.entities.EntityUtil;

import java.util.List;

@Mixin(LichAbsorbMinionsGoal.class)
public class LichAbsorbMinionsGoalMixin {

    @Shadow
    @Final
    private Lich lich;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/Goal;tick()V", shift = At.Shift.AFTER), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (this.lich.getTeleportInvisibility() <= 0) {
            if (this.lich.getScepterTimeLeft() <= 0) {
                List<LichMinion> minions = this.lich.level().getEntitiesOfClass(LichMinion.class, this.lich.getBoundingBox().inflate((double) 32.0F, (double) 16.0F, (double) 32.0F)).stream().filter((m) -> m.master == this.lich).toList();
                if (!minions.isEmpty() && this.lich.getRandom().nextInt(2) == 0) {
                    LichMinion minion = (LichMinion) minions.getFirst();
                    minion.convertTo(TFEntities.SKELETON_DRUID.get(), true);
                    LifedrainScepterItem.animateTargetShatter((ServerLevel) this.lich.level(), minion);
                    SoundEvent deathSound = EntityUtil.getDeathSound(minion);
                    if (deathSound != null) {
                        this.lich.level().playSound((Player) null, minion.blockPosition(), deathSound, SoundSource.HOSTILE, 1.0F, minion.getVoicePitch());
                    }

                    this.lich.playSound((SoundEvent) TFSounds.LICH_POP_MOB.get(), 3.0F, 0.4F + this.lich.getRandom().nextFloat() * 0.2F);
                    minion.playSound((SoundEvent) TFSounds.LICH_POP_MOB.get(), 3.0F, 0.4F + this.lich.getRandom().nextFloat() * 0.2F);
                    this.lich.makeMagicTrail(minion.getEyePosition(), this.lich.getEyePosition(), 1.0F, 0.5F, 0.5F);
                    this.lich.heal(minion.getHealth() / 2);
                    this.lich.swing(InteractionHand.MAIN_HAND);
                    this.lich.setPopCooldown(40);
                    this.lich.gameEvent(GameEvent.ENTITY_DIE);
                    ci.cancel();
                }

            }
        }
    }
}
