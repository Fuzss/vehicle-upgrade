package fuzs.vehicleupgrade.mixin;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.handler.SprintingMountHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "setSprinting", at = @At("TAIL"))
    public void setSprinting(boolean isSprinting, CallbackInfo callback) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).sprintWhileRiding) {
            return;
        }

        if (this.isPassenger() && this.getVehicle() instanceof LivingEntity vehicle) {
            SprintingMountHandler.applySpeedModifier(vehicle, isSprinting);
        }
    }
}
