package fuzs.vehicleupgrade.mixin;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.BoatJumping;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBoat.class)
abstract class AbstractBoatMixin extends VehicleEntity implements PlayerRideableJumping {
    @Shadow
    private Boat.Status status;

    public AbstractBoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @see net.minecraft.world.entity.animal.horse.AbstractHorse#onPlayerJump(int)
     */
    @Override
    public void onPlayerJump(int jumpPower) {
        if (this.onGround() || VehicleUpgrade.CONFIG.get(ServerConfig.class).jumpInBoats == BoatJumping.ALWAYS) {
            float playerJumpScale;
            if (jumpPower >= 90) {
                playerJumpScale = 1.0F;
            } else {
                playerJumpScale = 0.4F + 0.4F * Math.max(jumpPower, 0.0F) / 90.0F;
            }

            this.hasImpulse = true;
            // same as Entity::maxStepUp, allows for carpets, dirt path, soul sand, etc.
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, playerJumpScale * 0.325, 0.0));
        }
    }

    @Override
    public boolean canJump() {
        return VehicleUpgrade.CONFIG.get(ServerConfig.class).jumpInBoats.canJump(this.status);
    }

    @Override
    public void handleStartJump(int jumpPower) {
        // NO-OP
    }

    @Override
    public void handleStopJump() {
        // NO-OP
    }

    @Override
    public float maxUpStep() {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).increaseBoatStepHeight) {
            return super.maxUpStep();
        }

        // this translates directly to the block height, so just enough to step up carpets, dirt path, soul sand, etc.
        return BoatJumping.ON_LAND.canJump(this.status) ? 0.15F : super.maxUpStep();
    }

    @Inject(method = "hasEnoughSpaceFor", at = @At("HEAD"), cancellable = true)
    public void hasEnoughSpaceFor(Entity entity, CallbackInfoReturnable<Boolean> callback) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).shrinkOverSizedBoatPassengers) {
            return;
        }

        if (entity.getType().is(ModRegistry.OVER_SIZED_BOAT_PASSENGERS_ENTITY_TYPE_TAG)) {
            callback.setReturnValue(true);
        }
    }
}
