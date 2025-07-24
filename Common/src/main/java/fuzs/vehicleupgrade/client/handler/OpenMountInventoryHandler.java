package fuzs.vehicleupgrade.client.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.config.VehiclePassengerInventory;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class OpenMountInventoryHandler {
    private static int inventoryTriggerTime;

    public static void onStartClientTick(Minecraft minecraft) {
        if (inventoryTriggerTime > 0) {
            if (VehicleUpgrade.CONFIG.get(ClientConfig.class).doubleTabToSwitchVehicleInventory) {
                inventoryTriggerTime--;
            } else {
                inventoryTriggerTime = 0;
            }
        }

        if (minecraft.gameMode != null && isServerControlledInventory(minecraft.player)) {
            switch (VehicleUpgrade.CONFIG.get(ClientConfig.class).defaultVehicleInventory) {
                case VEHICLE -> {
                    if (minecraft.options.keyInventory.isDown()) {
                        inventoryTriggerTime = 7;
                    }
                }
                case PLAYER -> {
                    while (minecraft.options.keyInventory.consumeClick()) {
                        if (inventoryTriggerTime == 0) {
                            inventoryTriggerTime = 7;
                            VehiclePassengerInventory.PLAYER.openInventory(minecraft, null);
                        } else {
                            VehiclePassengerInventory.VEHICLE.openInventory(minecraft, null);
                        }
                    }
                }
            }
        }
    }

    public static EventResult onBeforeKeyPress(Screen screen, int keyCode, int scanCode, int modifiers) {
        if (inventoryTriggerTime > 0 && screen.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            VehicleUpgrade.CONFIG.get(ClientConfig.class).defaultVehicleInventory.getOpposite()
                    .openInventory(screen.minecraft, screen);
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    /**
     * @see MultiPlayerGameMode#isServerControlledInventory()
     */
    public static boolean isServerControlledInventory(@Nullable Player player) {
        if (player != null && player.isPassenger()) {
            Entity playerVehicle = player.getVehicle();
            return playerVehicle != null && (playerVehicle instanceof HasCustomInventoryScreen
                    || playerVehicle.getType().is(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG));
        } else {
            return false;
        }
    }
}
