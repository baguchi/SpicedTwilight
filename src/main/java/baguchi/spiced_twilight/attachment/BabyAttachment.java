package baguchi.spiced_twilight.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class BabyAttachment implements INBTSerializable<CompoundTag> {
    private boolean baby;
    private boolean needCheckSize;

    public void setBaby(boolean baby) {
        this.baby = baby;
        this.needCheckSize = true;
    }

    public void tick(Entity entity) {
        if (needCheckSize) {
            entity.refreshDimensions();
            needCheckSize = false;
        }
    }

    public boolean isBaby() {
        return baby;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putBoolean("baby", baby);
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        baby = nbt.getBoolean("baby");
    }
}