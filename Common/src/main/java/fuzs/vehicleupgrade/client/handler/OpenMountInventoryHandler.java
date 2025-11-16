package fuzs.vehicleupgrade.client.handler;

import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.config.VehicleInventory;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class OpenMountInventoryHandler {
    private static boolean skipCharacterType;
    private static CharacterEvent consumedCharacterEvent;

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
        if (isServerControlledInventory(screen.minecraft.player) && screen.minecraft.options.keyInventory.matches(
                keyEvent)) {
            // if the character event has been consumed, ignore our keybind;
            // it has likely been entered into an edit box which we do not want to override
            if (consumedCharacterEvent != null) {
                consumedCharacterEvent = null;
                VehicleUpgrade.CONFIG.get(ClientConfig.class).switchVehicleInventory.stop();
            }

            VehicleInventory vehicleInventory = VehicleInventory.getInventory(screen);
            if (VehicleInventory.trigger(vehicleInventory, screen.minecraft, screen, false)) {
                // do not process the typed char when the key input has already been handled by the keybind
                skipCharacterType = true;
                return EventResult.INTERRUPT;
            }
        }

        return EventResult.PASS;
    }

    public static EventResult onBeforeCharacterType(Screen screen, CharacterEvent characterEvent) {
        if (KeyMappingHelper.matchesCodePoint(screen.minecraft.options.keyInventory, characterEvent.codepoint())) {
            // store this to check later if the character event has already been consumed (i.e. the after callback does not run)
            consumedCharacterEvent = characterEvent;
            // do not process the typed char when the key input has already been handled by the keybind
            if (skipCharacterType) {
                skipCharacterType = false;
                return EventResult.INTERRUPT;
            }
        }

        return EventResult.PASS;
    }

    public static void onAfterCharacterType(Screen screen, CharacterEvent characterEvent) {
        // reset this when the character event has not been consumed
        consumedCharacterEvent = null;
    }

    public static void onRemove(Screen screen) {
        // reset this when the screen changes
        consumedCharacterEvent = null;
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
