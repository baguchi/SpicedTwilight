package baguchi.spiced_twilight.entity.goal;

import baguchi.spiced_twilight.attachment.ModAttachments;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import twilightforest.entity.monster.HelmetCrab;

import java.util.EnumSet;

public class HidingGoal extends Goal {
    public final HelmetCrab helmetCrab;
    public HidingGoal(HelmetCrab helmetCrab) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        this.helmetCrab = helmetCrab;
    }

    @Override
    public boolean canUse() {
        return this.helmetCrab.getData(ModAttachments.HELMET_CRAB_HIDE).isHide();
    }

    @Override
    public void tick() {
        super.tick();
        if(this.helmetCrab.getData(ModAttachments.HELMET_CRAB_HIDE).getHideCooldown() <= 0) {
            this.helmetCrab.getData(ModAttachments.HELMET_CRAB_HIDE).hideStop();
            this.helmetCrab.syncData(ModAttachments.HELMET_CRAB_HIDE);
        }
    }
}