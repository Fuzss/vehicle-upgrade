package fuzs.vehicleupgrade.config;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.client.handler.MountInventoryButtonHandler;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

public enum PlayerVehicleInventoryButton {
    NONE(-1, -1, null) {
        @Override
        public @Nullable Vector2i getButtonPositions(AbstractContainerScreen<?> screen) {
            return null;
        }
    },
    BUTTON(20, 18, MountInventoryButtonHandler.BUTTON_SPRITES) {
        @Override
        public @Nullable Vector2i getButtonPositions(AbstractContainerScreen<?> screen) {
            if (screen instanceof CreativeModeInventoryScreen) {
                return new Vector2i(screen.leftPos + 104 + 20 + 2 + 12, screen.height / 2 - 50);
            } else if (screen instanceof InventoryScreen) {
                return new Vector2i(screen.leftPos + 104 + 20 + 8, screen.height / 2 - 22);
            } else {
                return null;
            }
        }

        @Override
        public boolean renderFakeItem() {
            return true;
        }
    },
    ICON(11, 11, MountInventoryButtonHandler.ICON_SPRITES) {
        @Override
        public @Nullable Vector2i getButtonPositions(AbstractContainerScreen<?> screen) {
            AnchorPoint anchorPoint = VehicleUpgrade.CONFIG.get(ClientConfig.class).vehicleIconAnchorPoint;
            if (screen instanceof CreativeModeInventoryScreen) {
                AnchorPoint.Positioner positioner = anchorPoint.createPositioner(32, 43, this.width, this.height);
                return new Vector2i(screen.leftPos + 73 + positioner.getPosX(1),
                        screen.topPos + 6 + positioner.getPosY(1));
            } else if (screen instanceof InventoryScreen) {
                AnchorPoint.Positioner positioner = anchorPoint.createPositioner(49, 70, this.width, this.height);
                return new Vector2i(screen.leftPos + 26 + positioner.getPosX(1),
                        screen.topPos + 8 + positioner.getPosY(1));
            } else {
                return null;
            }
        }
    },
    CROSS(13, 13, MountInventoryButtonHandler.CROSS_SPRITES) {
        @Override
        public @Nullable Vector2i getButtonPositions(AbstractContainerScreen<?> screen) {
            if (screen instanceof CreativeModeInventoryScreen) {
                return new Vector2i(screen.leftPos + screen.imageWidth - this.width - 7, screen.topPos + 3);
            } else if (screen instanceof InventoryScreen) {
                return new Vector2i(screen.leftPos + screen.imageWidth - this.width - 7, screen.topPos + 3);
            } else {
                return null;
            }
        }
    };

    public final int width;
    public final int height;
    public final WidgetSprites sprites;

    PlayerVehicleInventoryButton(int width, int height, WidgetSprites sprites) {
        this.width = width;
        this.height = height;
        this.sprites = sprites;
    }

    public abstract @Nullable Vector2i getButtonPositions(AbstractContainerScreen<?> screen);

    public boolean renderFakeItem() {
        return false;
    }
}
