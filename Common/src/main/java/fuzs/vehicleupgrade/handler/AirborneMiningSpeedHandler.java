package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AirborneMiningSpeedHandler {
    static final AttributeModifier RIDING_ATTRIBUTE_MODIFIER = new AttributeModifier(VehicleUpgrade.id("riding"),
            4.0,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public static EventResult onCalculateBlockBreakSpeed(Player player, BlockState blockState, MutableFloat breakSpeed) {
        if (!player.onGround()) {
            breakSpeed.mapFloat((Float value) -> value * (float) (
                    player.getAttributeValue(ModRegistry.AIRBORNE_MINING_SPEED_ATTRIBUTE) * 5.0));
        }
        return EventResult.PASS;
    }

    public static EventResult onStartRiding(Level level, Entity rider, Entity vehicle) {
        if (rider instanceof Player player) {
            AttributeInstance attribute = player.getAttribute(ModRegistry.AIRBORNE_MINING_SPEED_ATTRIBUTE);
            if (!attribute.hasModifier(AirborneMiningSpeedHandler.RIDING_ATTRIBUTE_MODIFIER.id())) {
                attribute.addTransientModifier(AirborneMiningSpeedHandler.RIDING_ATTRIBUTE_MODIFIER);
            }
        }

        return EventResult.PASS;
    }

    public static EventResult onStopRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (passengerEntity instanceof Player player) {
            player.getAttribute(ModRegistry.AIRBORNE_MINING_SPEED_ATTRIBUTE)
                    .removeModifier(AirborneMiningSpeedHandler.RIDING_ATTRIBUTE_MODIFIER);
        }

        return EventResult.PASS;
    }
}
