package baguchi.spiced_twilight.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.apache.commons.lang3.math.Fraction;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CinderPouchItem extends Item {
    private static final int BAR_COLOR = Mth.color(1F, 0.5F, 0F);

    public CinderPouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) {
            return false;
        } else {
            BundleContents bundlecontents = stack.get(DataComponents.BUNDLE_CONTENTS);
            if (bundlecontents == null) {
                return false;
            } else {
                ItemStack itemstack = slot.getItem();
                BundleContents.Mutable bundlecontents$mutable = new BundleContents.Mutable(bundlecontents);
                if (itemstack.isEmpty()) {
                    this.playRemoveOneSound(player);
                    ItemStack itemstack1 = bundlecontents$mutable.removeOne();
                    if (itemstack1 != null) {
                        ItemStack itemstack2 = slot.safeInsert(itemstack1);
                        bundlecontents$mutable.tryInsert(itemstack2);
                    }
                } else if (itemstack.is(ModItems.FIRE_BEETLE_POWDER)) { // Neo: stack-aware placeability check
                    int i = bundlecontents$mutable.tryTransfer(slot, player);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(DataComponents.BUNDLE_CONTENTS, bundlecontents$mutable.toImmutable());
                return true;
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        BundleContents bundlecontents = itemstack.get(DataComponents.BUNDLE_CONTENTS);

        if (!bundlecontents.isEmpty()) {
            player.startUsingItem(usedHand);

            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() == newStack.getItem();
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration % 10 == 0) {
            BundleContents bundlecontents = stack.get(DataComponents.BUNDLE_CONTENTS);
            if (!bundlecontents.isEmpty()) {
                removeOneItem(bundlecontents);
            }
        }

        if (!level.isClientSide() && remainingUseDuration > 10) {
            Entity target = this.getHeadLookTarget(livingEntity);
            if (target != null && target != livingEntity && livingEntity.attackable() && !target.fireImmune() && target.hurt(TFDamageTypes.getEntityDamageSource(level, TFDamageTypes.SCORCHED, livingEntity), 2.0F)) {
                target.igniteForSeconds(10.0F);
            }
        } else {
            Vec3 look = livingEntity.getLookAngle();
            double dist = 0.9 * 3;
            double px = livingEntity.getX() + look.x() * dist;
            double py = livingEntity.getY() + (double) 0.25F + look.y() * dist;
            double pz = livingEntity.getZ() + look.z() * dist;

            for (int i = 0; i < 2; ++i) {
                double dx = look.x();
                double dy = look.y();
                double dz = look.z();
                double spread = (double) 5.0F + livingEntity.getRandom().nextDouble() * (double) 2.5F;
                double velocity = 0.15 + livingEntity.getRandom().nextDouble() * 0.15;
                dx += livingEntity.getRandom().nextGaussian() * 0.0075 * spread;
                dy += livingEntity.getRandom().nextGaussian() * 0.0075 * spread;
                dz += livingEntity.getRandom().nextGaussian() * 0.0075 * spread;
                dx *= velocity;
                dy *= velocity;
                dz *= velocity;
                level.addParticle(ParticleTypes.FLAME, px, py, pz, dx, dy, dz);
            }
        }
        livingEntity.playSound(TFSounds.FIRE_BEETLE_SHOOT.get(), livingEntity.getRandom().nextFloat() * 0.5F, livingEntity.getRandom().nextFloat() * 0.5F);
        livingEntity.gameEvent(GameEvent.PROJECTILE_SHOOT);
    }

    private @org.jetbrains.annotations.Nullable Entity getHeadLookTarget(LivingEntity living) {
        Entity pointedEntity = null;
        double range = 30.0F;
        double offset = 3.0F;
        Vec3 srcVec = new Vec3(living.getX(), living.getY() + (double) 0.25F, living.getZ());
        Vec3 lookVec = living.getViewVector(1.0F);
        Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);
        float var9 = 1.5F;
        List<Entity> possibleList = living.level().getEntities(living, living.getBoundingBox().move(lookVec.x() * offset, lookVec.y() * offset, lookVec.z() * offset).inflate(var9, var9, var9));
        double hitDist = 0.0F;
        if (living.isMultipartEntity()) {
            possibleList.removeAll(Arrays.asList((PartEntity[]) Objects.requireNonNull(living.getParts())));
        }

        for (Entity possibleEntity : possibleList) {
            if (possibleEntity.isPickable() && possibleEntity != living && EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE).test(possibleEntity)) {
                float borderSize = possibleEntity.getPickRadius();
                AABB collisionBB = possibleEntity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                Optional<Vec3> interceptPos = collisionBB.clip(srcVec, destVec);
                if (collisionBB.contains(srcVec)) {
                    if ((double) 0.0F < hitDist || hitDist == (double) 0.0F) {
                        pointedEntity = possibleEntity;
                        hitDist = 0.0F;
                    }
                } else if (interceptPos.isPresent()) {
                    double possibleDist = srcVec.distanceTo(interceptPos.get());
                    if (possibleDist < hitDist || hitDist == (double) 0.0F) {
                        pointedEntity = possibleEntity;
                        hitDist = possibleDist;
                    }
                }
            }
        }

        return pointedEntity;
    }


    static Fraction getWeight(ItemStack stack) {
        return Fraction.getFraction(1, stack.getMaxStackSize());
    }

    @Nullable
    public ItemStack removeOneItem(BundleContents bundleContents) {
        if (bundleContents.isEmpty()) {
            return null;
        } else {
            ItemStack copy = bundleContents.itemCopyStream().toList().get(0).copy();
            bundleContents.getItemUnsafe(0).shrink(1);

            bundleContents.weight().subtract(getWeight(copy).multiplyBy(Fraction.getFraction(1, 1)));
            return copy;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        BundleContents bundlecontents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return bundlecontents.weight().compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        BundleContents bundlecontents = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        return Math.min(1 + Mth.mulAndTruncate(bundlecontents.weight(), 12), 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(DataComponents.BUNDLE_CONTENTS)).map(BundleTooltip::new)
                : Optional.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        BundleContents bundlecontents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundlecontents != null) {
            int i = Mth.mulAndTruncate(bundlecontents.weight(), 64);
            tooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", i, 64).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        BundleContents bundlecontents = itemEntity.getItem().get(DataComponents.BUNDLE_CONTENTS);
        if (bundlecontents != null) {
            itemEntity.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
            ItemUtils.onContainerDestroyed(itemEntity, bundlecontents.itemsCopy());
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

}
