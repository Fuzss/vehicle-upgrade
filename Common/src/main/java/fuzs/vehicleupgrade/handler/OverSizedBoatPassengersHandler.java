package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.util.v1.CommonHelper;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.level.Level;

public class OverSizedBoatPassengersHandler {

    public static EventResultHolder<EntityDimensions> onRefreshEntityDimensions(Entity entity, Pose pose, EntityDimensions entityDimensions) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).shrinkOverSizedBoatPassengers) {
            return EventResultHolder.pass();
        }

        if (entity.getVehicle() instanceof AbstractBoat && entity.getType()
                .is(ModRegistry.OVER_SIZED_BOAT_PASSENGERS_ENTITY_TYPE_TAG)) {
            // this is the ideal size where a player controlling the boat can still use items by not being inside the other passenger's hitbox
            return EventResultHolder.interrupt(entityDimensions.scale(Math.min(0.875F / entityDimensions.width(), 1.0F),
                    1.0F));
        } else {
            return EventResultHolder.pass();
        }
    }

    public static EventResult onStartRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        return onRiding(level, passengerEntity, vehicleEntity);
    }

    public static EventResult onStopRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        return onRiding(level, passengerEntity, vehicleEntity);
    }

    private static EventResult onRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).shrinkOverSizedBoatPassengers) {
            return EventResult.PASS;
        }

        if (vehicleEntity instanceof AbstractBoat && passengerEntity.getType()
                .is(ModRegistry.OVER_SIZED_BOAT_PASSENGERS_ENTITY_TYPE_TAG)) {
            BlockableEventLoop<? super TickTask> blockableEventLoop = CommonHelper.getBlockableEventLoop(level);
            blockableEventLoop.schedule(new TickTask(0, passengerEntity::refreshDimensions));
        }

        return EventResult.PASS;
    }
}
