package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.CommonConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AirborneMiningSpeedHandler {
    public static final Identifier RIDING_ATTRIBUTE_MODIFIER_ID = VehicleUpgrade.id("riding");
    private static final AttributeModifier RIDING_ATTRIBUTE_MODIFIER = new AttributeModifier(
            RIDING_ATTRIBUTE_MODIFIER_ID,
            4.0,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static EventResult onCalculateBlockBreakSpeed(Player player, BlockState blockState, MutableFloat breakSpeed) {
        if (!VehicleUpgrade.CONFIG.get(CommonConfig.class).removePassengerMiningSpeedMalus) {
            return EventResult.PASS;
        }

        if (!player.onGround()) {
            breakSpeed.mapAsFloat((Float value) -> value * (float) (
                    player.getAttributeValue(ModRegistry.AIRBORNE_MINING_SPEED_ATTRIBUTE) * 5.0));
        }

        return EventResult.PASS;
    }

    public static EventResult onStartRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(CommonConfig.class).removePassengerMiningSpeedMalus) {
            return EventResult.PASS;
        }

        if (passengerEntity instanceof Player player) {
            AttributeInstance attributeInstance = player.getAttribute(ModRegistry.AIRBORNE_MINING_SPEED_ATTRIBUTE);
            if (attributeInstance != null
                    && !attributeInstance.hasModifier(AirborneMiningSpeedHandler.RIDING_ATTRIBUTE_MODIFIER_ID)) {
                attributeInstance.addTransientModifier(AirborneMiningSpeedHandler.RIDING_ATTRIBUTE_MODIFIER);
            }
        }

        return EventResult.PASS;
    }

    public static EventResult onStopRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(CommonConfig.class).removePassengerMiningSpeedMalus) {
            return EventResult.PASS;
        }

        if (passengerEntity instanceof Player player) {
            AttributeInstance attributeInstance = player.getAttribute(ModRegistry.AIRBORNE_MINING_SPEED_ATTRIBUTE);
            if (attributeInstance != null) {
                attributeInstance.removeModifier(AirborneMiningSpeedHandler.RIDING_ATTRIBUTE_MODIFIER_ID);
            }
        }

        return EventResult.PASS;
    }
}
