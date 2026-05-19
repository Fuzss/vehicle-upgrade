package fuzs.vehicleupgrade.common;

import fuzs.puzzleslib.common.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.core.v1.context.EntityAttributesContext;
import fuzs.puzzleslib.common.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.common.api.event.v1.core.EventPhase;
import fuzs.puzzleslib.common.api.event.v1.entity.EntityRidingEvents;
import fuzs.puzzleslib.common.api.event.v1.entity.RefreshEntityDimensionsCallback;
import fuzs.puzzleslib.common.api.event.v1.entity.ServerEntityEvents;
import fuzs.puzzleslib.common.api.event.v1.entity.ServerEntityLevelEvents;
import fuzs.puzzleslib.common.api.event.v1.entity.living.LivingEquipmentChangeCallback;
import fuzs.puzzleslib.common.api.event.v1.entity.player.CalculateBlockBreakSpeedCallback;
import fuzs.puzzleslib.common.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.vehicleupgrade.common.config.ClientConfig;
import fuzs.vehicleupgrade.common.config.CommonConfig;
import fuzs.vehicleupgrade.common.config.ServerConfig;
import fuzs.vehicleupgrade.common.handler.*;
import fuzs.vehicleupgrade.common.init.ModRegistry;
import fuzs.vehicleupgrade.common.network.client.ServerboundOpenEquipmentInventoryMessage;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleUpgrade implements ModConstructor {
    public static final String MOD_ID = "vehicleupgrade";
    public static final String MOD_NAME = "Vehicle Upgrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID)
            .client(ClientConfig.class)
            .common(CommonConfig.class)
            .server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandler();
    }

    private static void registerEventHandler() {
        CalculateBlockBreakSpeedCallback.EVENT.register(AirborneMiningSpeedHandler::onCalculateBlockBreakSpeed);
        EntityRidingEvents.START.register(AirborneMiningSpeedHandler::onStartRiding);
        EntityRidingEvents.STOP.register(AirborneMiningSpeedHandler::onStopRiding);
        ServerEntityEvents.LOAD.register(DismountingRestrictionHandler::onEntityJoin);
        EntityRidingEvents.STOP.register(DismountingRestrictionHandler::onStopRiding);
        LivingEquipmentChangeCallback.EVENT.register(DismountingRestrictionHandler::onLivingEquipmentChange);
        ServerEntityEvents.LOAD.register(HorseUpgradeHandler::onEntityJoin);
        PlayerInteractEvents.USE_ENTITY.register(HorseUpgradeHandler::onUseEntity);
        PlayerInteractEvents.USE_ENTITY.register(EventPhase.AFTER, MountInventoryHandler::onUseEntity);
        RefreshEntityDimensionsCallback.EVENT.register(OverSizedBoatPassengersHandler::onRefreshEntityDimensions);
        EntityRidingEvents.START.register(OverSizedBoatPassengersHandler::onStartRiding);
        EntityRidingEvents.STOP.register(OverSizedBoatPassengersHandler::onStopRiding);
        EntityRidingEvents.START.register(PassengerInteractionRangeHandler::onStartRiding);
        EntityRidingEvents.STOP.register(PassengerInteractionRangeHandler::onStopRiding);
        EntityRidingEvents.START.register(SprintingMountHandler::onStartRiding);
        EntityRidingEvents.STOP.register(SprintingMountHandler::onStopRiding);
        PlayerInteractEvents.USE_ENTITY.register(VehicleUpgradeHandler::onUseEntity);
        EntityRidingEvents.START.register(VehicleUpgradeHandler::onStartRiding);
    }

    @Override
    public void onCommonSetup() {
        if (CONFIG.get(CommonConfig.class).speedMobEffectsGrantFlyingSpeed) {
            MobEffects.SPEED.value()
                    .addAttributeModifier(Attributes.FLYING_SPEED,
                            Identifier.withDefaultNamespace("effect.flying_speed"),
                            0.2F,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            MobEffects.SLOWNESS.value()
                    .addAttributeModifier(Attributes.FLYING_SPEED,
                            Identifier.withDefaultNamespace("effect.flying_speed"),
                            -0.15F,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.playToServer(ServerboundOpenEquipmentInventoryMessage.class,
                ServerboundOpenEquipmentInventoryMessage.STREAM_CODEC);
    }

    @Override
    public void onRegisterEntityAttributes(EntityAttributesContext context) {
        if (VehicleUpgrade.CONFIG.get(CommonConfig.class).removePassengerMiningSpeedMalus) {
            context.registerAttribute(EntityType.PLAYER, ModRegistry.AIRBORNE_MINING_SPEED_ATTRIBUTE);
        }

        if (VehicleUpgrade.CONFIG.get(CommonConfig.class).increaseHorseStepHeight) {
            context.registerAttribute(EntityType.HORSE, Attributes.STEP_HEIGHT, 1.15);
            context.registerAttribute(EntityType.CAMEL, Attributes.STEP_HEIGHT, 1.15);
            context.registerAttribute(EntityType.SKELETON_HORSE, Attributes.STEP_HEIGHT, 1.15);
            context.registerAttribute(EntityType.ZOMBIE_HORSE, Attributes.STEP_HEIGHT, 1.15);
            context.registerAttribute(EntityType.DONKEY, Attributes.STEP_HEIGHT, 1.15);
            context.registerAttribute(EntityType.MULE, Attributes.STEP_HEIGHT, 1.15);
            context.registerAttribute(EntityType.LLAMA, Attributes.STEP_HEIGHT, 1.15);
            context.registerAttribute(EntityType.TRADER_LLAMA, Attributes.STEP_HEIGHT, 1.15);
        }
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
