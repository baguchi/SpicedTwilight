package baguchi.spiced_twilight.mixin;

import baguchi.spiced_twilight.attachment.BabyAttachment;
import baguchi.spiced_twilight.attachment.ModAttachments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.monster.Kobold;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;

import javax.annotation.Nullable;

@Mixin(Kobold.class)
public class KoboldMixin extends Monster {
    @Unique
    private static final ResourceLocation DAMAGE_MODIFIER_BABY_ID = ResourceLocation.withDefaultNamespace("baby");
    @Unique
    private static final AttributeModifier DAMAGE_MODIFIER_BABY = new AttributeModifier(
            DAMAGE_MODIFIER_BABY_ID, -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    );
    @Unique
    public boolean spicedTwilight$baby;
    private static final EntityDimensions BABY_DIMENSIONS = TFEntities.KOBOLD.get().getDimensions().scale(0.5F).withEyeHeight(0.45F);

    protected KoboldMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            BabyAttachment babyAttachment = this.getData(ModAttachments.BABY);
            if (babyAttachment != null && spicedTwilight$baby != babyAttachment.isBaby()) {
                babyAttachment.setBaby(spicedTwilight$baby);
                this.syncData(ModAttachments.BABY);

            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.isBaby()) {
            tag.putBoolean("IsBaby", true);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.getBoolean("IsBaby")) {
            this.setBaby(true);
        }
    }

    @Override
    public void setBaby(boolean baby) {
        super.setBaby(baby);
        this.spicedTwilight$baby = baby;
        BabyAttachment babyAttachment = this.getData(ModAttachments.BABY);
        if (babyAttachment != null) {
            babyAttachment.setBaby(baby);
            this.syncData(ModAttachments.BABY);
        }
        if (this.level() != null && !this.level().isClientSide) {
            AttributeInstance attributeinstance = this.getAttribute(Attributes.ATTACK_DAMAGE);
            attributeinstance.removeModifier(DAMAGE_MODIFIER_BABY_ID);
            if (baby) {
                attributeinstance.addTransientModifier(DAMAGE_MODIFIER_BABY);
            }
        }
    }

    @Override
    public boolean isBaby() {
        if (this.getData(ModAttachments.BABY) == null) {
            return super.isBaby();
        }

        return this.getData(ModAttachments.BABY).isBaby();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        if (!this.isBaby()) {
            if (this.level().getBiome(this.blockPosition()).is(TFBiomes.SNOWY_FOREST) || this.level().getBiome(this.blockPosition()).is(TFBiomes.GLACIER)) {
                if (this.random.nextFloat() < 0.5F) {
                    this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(TFItems.ARCTIC_HELMET.asItem()));
                }
                if (this.random.nextFloat() < 0.2F) {
                    this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(TFItems.ARCTIC_CHESTPLATE.asItem()));
                }
                if (this.random.nextFloat() < 0.25F) {
                    this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(TFItems.ARCTIC_LEGGINGS.asItem()));
                    this.setItemSlot(EquipmentSlot.FEET, new ItemStack(TFItems.ARCTIC_BOOTS.asItem()));
                }

            } else if (this.level().getBiome(this.blockPosition()).is(TFBiomes.UNDERGROUND)) {
                if (this.random.nextFloat() < 0.1) {
                    this.setItemSlot(EquipmentSlot.HEAD, Items.IRON_HELMET.getDefaultInstance());
                }
            } else if (this.level().getBiome(this.blockPosition()).is(TFBiomes.HIGHLANDS_UNDERGROUND)) {
                if (this.random.nextFloat() < 0.25) {
                    this.setItemSlot(EquipmentSlot.HEAD, TFItems.IRONWOOD_HELMET.toStack());
                }
            } else {
                if (this.random.nextFloat() < 0.025) {
                    if (this.random.nextBoolean()) {
                        this.setItemSlot(EquipmentSlot.FEET, Items.LEATHER_BOOTS.getDefaultInstance());
                    } else {
                        this.setItemSlot(EquipmentSlot.HEAD, Items.LEATHER_HELMET.getDefaultInstance());
                    }
                }
                if (this.random.nextFloat() < 0.025) {
                    if (this.random.nextBoolean()) {
                        this.setItemSlot(EquipmentSlot.HEAD, TFItems.STEELEAF_BOOTS.toStack());
                    } else {
                        this.setItemSlot(EquipmentSlot.HEAD, TFItems.STEELEAF_HELMET.toStack());
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource randomsource = level.getRandom();
        if (this.random.nextFloat() < 0.05F) {
            this.setBaby(true);
        }
        this.populateDefaultEquipmentSlots(randomsource, difficulty);
        this.populateDefaultEquipmentEnchantments(level, randomsource, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }
}

