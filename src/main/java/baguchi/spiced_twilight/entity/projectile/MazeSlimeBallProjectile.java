package baguchi.spiced_twilight.entity.projectile;

import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.SlimeBeetle;
import twilightforest.entity.projectile.SlimeProjectile;
import twilightforest.entity.projectile.TFThrowable;
import twilightforest.init.TFItems;

public class MazeSlimeBallProjectile extends TFThrowable implements ItemSupplier {
    public MazeSlimeBallProjectile(EntityType<? extends MazeSlimeBallProjectile> type, Level world) {
        super(type, world);
    }

    public MazeSlimeBallProjectile(EntityType<? extends MazeSlimeBallProjectile> type, Level world, LivingEntity thrower) {
        super(type, world, thrower);
    }

    @Override
    public void tick() {
        super.tick();
        this.makeTrail(new ItemParticleOption(ParticleTypes.ITEM, TFItems.MAZE_SLIME_BALL.toStack()), 2);
    }

    @Override
    protected double getDefaultGravity() {
        return (double)0.006F;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        super.hurt(source, amount);
        this.die();
        return true;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(ParticleTypes.ITEM_SLIME, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, this.random.nextDouble() * 0.2, this.random.nextGaussian() * 0.05);
            }
        } else {
            super.handleEntityEvent(id);
        }

    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        if (!this.level().isClientSide() && target instanceof LivingEntity) {
            target.hurt(this.damageSources().thrown(this, this.getOwner()), 2.0F);
        }
        this.die();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Vec3 projectileMovement = this.getDeltaMovement();
        if (projectileMovement.length() > 0.15) {
            Vec3i direction = result.getDirection().getNormal();
            switch (result.getDirection()) {
                case UP, SOUTH, EAST:
                    direction = direction.multiply(-1);
                default:
            }
            direction = new Vec3i(direction.getX() == 0 ? 1 : direction.getX(), direction.getY() == 0 ? 1 : direction.getY(), direction.getZ() == 0 ? 1 : direction.getZ());
            this.setDeltaMovement(projectileMovement.multiply(new Vec3(direction.getX(), direction.getY(), direction.getZ())).multiply(0.75, 0.65, 0.75));
            this.playSound(SoundEvents.SLIME_SQUISH, 0.4F, 1.0F);
        } else {
            this.die();
        }
    }

    private void die() {
        if (!this.level().isClientSide()) {
            this.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
            this.discard();
            this.level().broadcastEntityEvent(this, (byte)3);
        }

    }

    public ItemStack getItem() {
        return new ItemStack(TFItems.MAZE_SLIME_BALL.asItem());
    }
}
