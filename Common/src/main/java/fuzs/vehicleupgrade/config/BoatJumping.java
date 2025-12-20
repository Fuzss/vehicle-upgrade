package fuzs.vehicleupgrade.config;

import net.minecraft.world.entity.vehicle.boat.Boat;

public enum BoatJumping {
    NEVER {
        @Override
        public boolean canJump(Boat.Status status) {
            return false;
        }
    },
    ON_LAND {
        @Override
        public boolean canJump(Boat.Status status) {
            return status == Boat.Status.ON_LAND || status == Boat.Status.IN_AIR;
        }
    },
    ALWAYS {
        @Override
        public boolean canJump(Boat.Status status) {
            return true;
        }
    };

    public abstract boolean canJump(Boat.Status status);
}
