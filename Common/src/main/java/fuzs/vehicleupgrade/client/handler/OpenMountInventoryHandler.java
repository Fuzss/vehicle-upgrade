package fuzs.vehicleupgrade.client.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.config.VehicleInventory;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class OpenMountInventoryHandler {

    public static void onStartClientTick(Minecraft minecraft) {
        VehicleUpgrade.CONFIG.get(ClientConfig.class).switchVehicleInventory.tick();

        if (isServerControlledInventory(minecraft.player)) {
            while (minecraft.options.keyInventory.consumeClick()) {
                VehicleInventory vehicleInventory = VehicleInventory.getInventory(minecraft.gameMode);
                VehicleInventory.trigger(vehicleInventory, minecraft, null, true);
            }
        }
    }

    public static EventResult onBeforeKeyPress(Screen screen, KeyEvent keyEvent) {
        if (screen.minecraft.options.keyInventory.matches(keyEvent)) {
            VehicleInventory vehicleInventory = VehicleInventory.getInventory(screen);

            if (VehicleInventory.trigger(vehicleInventory, screen.minecraft, screen, false)) {
                return EventResult.INTERRUPT;
            }
        }

        return EventResult.PASS;
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
