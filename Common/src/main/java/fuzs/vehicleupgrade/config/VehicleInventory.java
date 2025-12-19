package fuzs.vehicleupgrade.config;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.network.client.ServerboundOpenEquipmentInventoryMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import org.jetbrains.annotations.Nullable;

public enum VehicleInventory {
    PLAYER {
        @Override
        public void openInventory(Minecraft minecraft, @Nullable Screen screen) {
            // fix mouse cursor position resetting as the old screen is closed before the new one is opened
            double xpos = minecraft.mouseHandler.xpos();
            double ypos = minecraft.mouseHandler.ypos();
            if (screen != null) {
                screen.onClose();
            }

            minecraft.setScreen(new InventoryScreen(minecraft.player));
            if (screen != null && minecraft.screen != null) {
                minecraft.mouseHandler.xpos = xpos;
                minecraft.mouseHandler.ypos = ypos;
                InputConstants.grabOrReleaseMouse(minecraft.getWindow(), InputConstants.CURSOR_NORMAL, xpos, ypos);
            }
        }

        @Override
        public VehicleInventory getOpposite() {
            return VEHICLE;
        }
    },
    VEHICLE {
        @Override
        public void openInventory(Minecraft minecraft, @Nullable Screen screen) {
            // this resets the active container menu; but should not be necessary for the inventory, as that is handled fully client-side
//            if (screen != null) {
//                screen.onClose();
//            }

            if (minecraft.player != null && minecraft.player.isPassenger()) {
                Entity playerVehicle = minecraft.player.getVehicle();
                if (playerVehicle instanceof HasCustomInventoryScreen) {
                    minecraft.player.sendOpenInventory();
                } else if (playerVehicle.getType().is(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG)) {
                    MessageSender.broadcast(new ServerboundOpenEquipmentInventoryMessage(playerVehicle.getId()));
                }
            }
        }

        @Override
        public VehicleInventory getOpposite() {
            return PLAYER;
        }
    };

    public abstract void openInventory(Minecraft minecraft, @Nullable Screen screen);

    public abstract VehicleInventory getOpposite();

    public static boolean isPlayerInventory(Screen screen) {
        return screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen;
    }

    public static VehicleInventory getInventory(@Nullable MultiPlayerGameMode gameMode) {
        // do not alter vanilla behaviour;
        // we still open to the player inventory for mobs that have their inventory screen added by us
        if (gameMode != null && gameMode.isServerControlledInventory()) {
            return VehicleUpgrade.CONFIG.get(ClientConfig.class).defaultVehicleInventory;
        } else {
            return VehicleInventory.PLAYER;
        }
    }

    public static VehicleInventory getInventory(@Nullable Screen screen) {
        return isPlayerInventory(screen) ? PLAYER : VEHICLE;
    }

    public static boolean trigger(VehicleInventory vehicleInventory, Minecraft minecraft, @Nullable Screen screen, boolean openAlways) {
        InventorySwitch inventorySwitch = VehicleUpgrade.CONFIG.get(ClientConfig.class).switchVehicleInventory;
        boolean isActive = inventorySwitch.isActive();
        if (inventorySwitch.isActive()) {
            vehicleInventory = vehicleInventory.getOpposite();
        } else {
            inventorySwitch.reset();
        }

        if (openAlways || isActive) {
            vehicleInventory.openInventory(minecraft, screen);
            return true;
        } else {
            return false;
        }
    }
}
