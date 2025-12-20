package fuzs.vehicleupgrade.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractHorse.class)
abstract class AbstractHorseMixin extends Animal implements PlayerRideableJumping {

    protected AbstractHorseMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "isImmobile",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/world/entity/animal/equine/AbstractHorse;isEating()Z"))
    public boolean isImmobile$0(boolean isEating) {
        return !VehicleUpgrade.CONFIG.get(ServerConfig.class).smarterHorseBehavior;
    }

    @ModifyExpressionValue(method = "isImmobile",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/world/entity/animal/equine/AbstractHorse;isStanding()Z"))
    public boolean isImmobile$1(boolean isStanding) {
        return !VehicleUpgrade.CONFIG.get(ServerConfig.class).smarterHorseBehavior;
    }

    @ModifyExpressionValue(method = "aiStep",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/world/entity/animal/equine/AbstractHorse;canEatGrass()Z"))
    public boolean aiStep(boolean canEatGrass) {
        return !VehicleUpgrade.CONFIG.get(ServerConfig.class).smarterHorseBehavior;
    }

    @Override
    protected float getWaterSlowDown() {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).smarterHorseBehavior) {
            return super.getWaterSlowDown();
        } else {
            if (this.getType().is(EntityTypeTags.CAN_FLOAT_WHILE_RIDDEN)) {
                // By default, this value is 0.8; skeleton horses use 0.96.
                return 0.92F;
            } else {
                return super.getWaterSlowDown();
            }
        }
    }

    @Override
    public int getJumpCooldown() {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).smarterHorseBehavior) {
            return PlayerRideableJumping.super.getJumpCooldown();
        }

        if (this.getType().is(EntityTypeTags.CAN_FLOAT_WHILE_RIDDEN) && this.isInWater()) {
            // A negative number will still allow the normal jump bar to render but also prevents charging it.
            return -1;
        } else {
            return PlayerRideableJumping.super.getJumpCooldown();
        }
    }

    @ModifyExpressionValue(method = "standIfPossible",
                           at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isClientSide()Z"))
    public boolean standIfPossible(boolean isClientSide) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).smarterHorseBehavior) {
            return isClientSide;
        }

        return !this.isEffectiveAi();
    }

    @ModifyReturnValue(method = "getRiddenRotation", at = @At("TAIL"))
    protected Vec2 getRiddenRotation(Vec2 riddenRotation) {
        double amount = VehicleUpgrade.CONFIG.get(ServerConfig.class).lowerHorseHeadAmount;
        if (amount == 0.0) {
            return riddenRotation;
        } else {
            return riddenRotation.add(new Vec2((float) (60.0 * amount), 0.0F));
        }
    }
}
