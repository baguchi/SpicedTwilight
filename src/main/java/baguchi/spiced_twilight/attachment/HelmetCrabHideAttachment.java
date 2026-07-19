package baguchi.spiced_twilight.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class HelmetCrabHideAttachment implements INBTSerializable<CompoundTag> {
    private boolean hide;
    private int hideCooldown;
    private float hideChance;

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

    public void setHideChance(float hideChance) {
        this.hideChance = hideChance;
    }

    public float getHideChance() {
        return hideChance;
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
        compoundTag.putBoolean("hide", this.hide);
        compoundTag.putFloat("hide_chance", this.hideChance);
        compoundTag.putInt("hide_cooldown", this.hideCooldown);
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.hide = nbt.getBoolean("hide");
        this.hideChance = nbt.getFloat("hide_chance");
        this.hideCooldown = nbt.getInt("hide_cooldown");
    }
}