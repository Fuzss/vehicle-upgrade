package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class SprintingMountHandler {

    public static EventResult onStartRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).preventVehicleCollisionsWhileRiding) {
            return EventResult.PASS;
        }

        if (passengerEntity.getControllingPassenger() instanceof Player) {
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    public static EventResult onStopRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).sprintWhileRiding) {
            return EventResult.PASS;
        }

        if (vehicleEntity instanceof LivingEntity livingEntity) {
            applySpeedModifier(livingEntity, false);
        }

        return EventResult.PASS;
    }

    public static void applySpeedModifier(LivingEntity vehicle, boolean isSprinting) {
        if (vehicleCanSprint(vehicle)) {
            applySpeedModifier(vehicle.getAttribute(Attributes.MOVEMENT_SPEED), isSprinting);
            applySpeedModifier(vehicle.getAttribute(Attributes.FLYING_SPEED), isSprinting);
        }
    }

    /**
     * @see net.minecraft.client.player.LocalPlayer#vehicleCanSprint(Entity)
     */
    private static boolean vehicleCanSprint(Entity vehicle) {
        return !vehicle.canSprint() && vehicle.getType().is(ModRegistry.SPRINTING_MOUNTS_ENTITY_TYPE_TAG)
                && vehicle.isLocalInstanceAuthoritative();
    }

    private static void applySpeedModifier(@Nullable AttributeInstance attributeInstance, boolean isSprinting) {
        if (attributeInstance != null) {
            attributeInstance.removeModifier(LivingEntity.SPEED_MODIFIER_SPRINTING.id());
            if (isSprinting) {
                attributeInstance.addTransientModifier(LivingEntity.SPEED_MODIFIER_SPRINTING);
            }
        }
    }
}
