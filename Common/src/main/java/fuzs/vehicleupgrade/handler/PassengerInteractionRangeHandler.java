package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PassengerInteractionRangeHandler {

    public static EventResult onStartRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).increasePassengerInteractionRange) {
            return EventResult.PASS;
        }

        if (passengerEntity instanceof Player player) {
            int passengerRidingOffset = getPassengerRidingOffset(passengerEntity, vehicleEntity);
            if (passengerRidingOffset > 0) {
                addRidingAttributeModifier(player, Attributes.ENTITY_INTERACTION_RANGE, passengerRidingOffset);
                addRidingAttributeModifier(player, Attributes.BLOCK_INTERACTION_RANGE, passengerRidingOffset);
            }
        }

        return EventResult.PASS;
    }

    private static int getPassengerRidingOffset(Entity passengerEntity, Entity vehicleEntity) {
        Vec3 vec3 = vehicleEntity.getPassengerRidingPosition(passengerEntity)
                .subtract(vehicleEntity.position())
                .subtract(passengerEntity.getVehicleAttachmentPoint(vehicleEntity));
        return Mth.ceil(vec3.y());
    }

    private static void addRidingAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, int value) {
        AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
        if (attributeInstance != null
                && !attributeInstance.hasModifier(AirborneMiningSpeedHandler.RIDING_ATTRIBUTE_MODIFIER_ID)) {
            attributeInstance.addTransientModifier(new AttributeModifier(AirborneMiningSpeedHandler.RIDING_ATTRIBUTE_MODIFIER_ID,
                    value,
                    AttributeModifier.Operation.ADD_VALUE));
        }
    }

    public static EventResult onStopRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).increasePassengerInteractionRange) {
            return EventResult.PASS;
        }

        if (passengerEntity instanceof Player player) {
            removeRidingAttributeModifier(player, Attributes.ENTITY_INTERACTION_RANGE);
            removeRidingAttributeModifier(player, Attributes.BLOCK_INTERACTION_RANGE);
        }

        return EventResult.PASS;
    }

    private static void removeRidingAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute) {
        AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
        if (attributeInstance != null) {
            attributeInstance.removeModifier(AirborneMiningSpeedHandler.RIDING_ATTRIBUTE_MODIFIER_ID);
        }
    }
}
