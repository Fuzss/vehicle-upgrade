package fuzs.vehicleupgrade;

import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.EntityAttributesContext;
import fuzs.puzzleslib.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import fuzs.puzzleslib.api.event.v1.entity.ChangeEntitySizeCallback;
import fuzs.puzzleslib.api.event.v1.entity.EntityRidingEvents;
import fuzs.puzzleslib.api.event.v1.entity.EntityTickEvents;
import fuzs.puzzleslib.api.event.v1.entity.ServerEntityLevelEvents;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingEquipmentChangeCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.CalculateBlockBreakSpeedCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.config.CommonConfig;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.handler.*;
import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.network.client.ServerboundOpenEquipmentInventoryMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
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
        ServerEntityLevelEvents.LOAD.register(DismountingRestrictionHandler::onEntityLoad);
        EntityRidingEvents.STOP.register(DismountingRestrictionHandler::onStopRiding);
        LivingEquipmentChangeCallback.EVENT.register(DismountingRestrictionHandler::onLivingEquipmentChange);
        ServerEntityLevelEvents.LOAD.register(HorseUpgradeHandler::onEntityLoad);
        PlayerInteractEvents.USE_ENTITY.register(HorseUpgradeHandler::onUseEntity);
        PlayerInteractEvents.USE_ENTITY.register(EventPhase.AFTER, MountInventoryHandler::onUseEntity);
        ChangeEntitySizeCallback.EVENT.register(OverSizedBoatPassengersHandler::onChangeEntitySize);
        EntityRidingEvents.START.register(OverSizedBoatPassengersHandler::onStartRiding);
        EntityRidingEvents.STOP.register(OverSizedBoatPassengersHandler::onStopRiding);
        EntityRidingEvents.START.register(PassengerInteractionRangeHandler::onStartRiding);
        EntityRidingEvents.STOP.register(PassengerInteractionRangeHandler::onStopRiding);
        EntityTickEvents.START.register(SwimmingMountHandler::onStartEntityTick);
        EntityRidingEvents.STOP.register(SwimmingMountHandler::onStopRiding);
        PlayerInteractEvents.USE_ENTITY.register(VehicleUpgradeHandler::onUseEntity);
        EntityRidingEvents.START.register(VehicleUpgradeHandler::onStartRiding);
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

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
