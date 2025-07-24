package fuzs.vehicleupgrade.config;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.network.client.ServerboundOpenEquipmentInventoryMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import org.jetbrains.annotations.Nullable;

public enum VehiclePassengerInventory {
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

            if (screen != null) {
                minecraft.mouseHandler.xpos = xpos;
                minecraft.mouseHandler.ypos = ypos;
                InputConstants.grabOrReleaseMouse(minecraft.getWindow().getWindow(),
                        InputConstants.CURSOR_NORMAL,
                        xpos,
                        ypos);
            }
        }

        @Override
        public VehiclePassengerInventory getOpposite() {
            return VEHICLE;
        }
    },
    VEHICLE {
        @Override
        public void openInventory(Minecraft minecraft, @Nullable Screen screen) {
            if (screen != null) {
                screen.onClose();
            }

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
        public VehiclePassengerInventory getOpposite() {
            return PLAYER;
        }
    };

    public abstract void openInventory(Minecraft minecraft, @Nullable Screen screen);

    public abstract VehiclePassengerInventory getOpposite();
}
