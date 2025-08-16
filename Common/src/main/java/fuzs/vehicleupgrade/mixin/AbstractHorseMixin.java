package fuzs.vehicleupgrade.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
abstract class AbstractHorseMixin extends Animal {

    protected AbstractHorseMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "isImmobile",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;isEating()Z"))
    public boolean isImmobile$0(boolean isEating) {
        return !VehicleUpgrade.CONFIG.get(ServerConfig.class).upgradeHorseAi;
    }

    @ModifyExpressionValue(method = "isImmobile",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;isStanding()Z"))
    public boolean isImmobile$1(boolean isStanding) {
        return !VehicleUpgrade.CONFIG.get(ServerConfig.class).upgradeHorseAi;
    }

    @ModifyExpressionValue(method = "aiStep",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;canEatGrass()Z"))
    public boolean aiStep(boolean canEatGrass) {
        return !VehicleUpgrade.CONFIG.get(ServerConfig.class).upgradeHorseAi;
    }

    @Override
    protected float getWaterSlowDown() {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).mountsSwimInWater) {
            return super.getWaterSlowDown();
        } else {
            return 0.92F;
        }
    }

    @Inject(method = {"onPlayerJump", "handleStartJump"}, at = @At("HEAD"), cancellable = true)
    public void handleStartJump(CallbackInfo callback) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).mountsSwimInWater) {
            return;
        }

        if (this.isInLiquid()) {
            callback.cancel();
        }
    }

    @ModifyExpressionValue(method = "standIfPossible",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z"))
    public boolean standIfPossible(boolean isClientSide) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).upgradeHorseAi) {
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
