package baguchi.spiced_twilight.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class HelmetCrabHideAttachment implements INBTSerializable<CompoundTag> {
    private boolean hide;
    private int hideCooldown;

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public void hide() {
        this.hideCooldown = 40;
        this.hide = true;
    }

    public void hideStop() {
        this.hideCooldown = 60;
        this.hide = false;
    }

    public void hideStopForce() {
        this.hideCooldown = 120;
        this.hide = false;
    }

    public void tick(Entity entity) {
        if (this.hideCooldown > 0) {
            --this.hideCooldown;
        }
    }

    public int getHideCooldown() {
        return hideCooldown;
    }

    public boolean isHide() {
        return hide;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putBoolean("hide", hide);
        compoundTag.putInt("hide_cooldown", hideCooldown);
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        hide = nbt.getBoolean("hide");
        hideCooldown = nbt.getInt("hide_cooldown");
    }
}