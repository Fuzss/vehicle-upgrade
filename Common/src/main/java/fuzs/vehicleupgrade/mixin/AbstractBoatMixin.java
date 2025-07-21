package fuzs.vehicleupgrade.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBoat.class)
abstract class AbstractBoatMixin extends VehicleEntity implements PlayerRideableJumping {
    @Shadow
    private Boat.Status status;

    public AbstractBoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        if (this.onGround()) {
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
        return this.status == Boat.Status.ON_LAND || this.status == Boat.Status.IN_AIR;
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
        // this translates directly to the block height, so just enough to step up carpets, dirt path, soul sand, etc.
        return this.status == Boat.Status.ON_LAND || this.status == Boat.Status.IN_AIR ? 0.15F : super.maxUpStep();
    }
}
