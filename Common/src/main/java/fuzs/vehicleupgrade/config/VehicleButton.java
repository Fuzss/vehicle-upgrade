package fuzs.vehicleupgrade.config;

import fuzs.vehicleupgrade.client.handler.MountInventoryButtonHandler;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

public enum VehicleButton {
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
            if (screen instanceof CreativeModeInventoryScreen) {
                return new Vector2i(screen.leftPos + 105 - 11, screen.height + 6);
            } else if (screen instanceof InventoryScreen) {
                return new Vector2i(screen.leftPos + 75 - 11, screen.height + 8);
            } else {
                return null;
            }
        }
    };

    public final int width;
    public final int height;
    public final WidgetSprites sprites;

    VehicleButton(int width, int height, WidgetSprites sprites) {
        this.width = width;
        this.height = height;
        this.sprites = sprites;
    }

    public abstract @Nullable Vector2i getButtonPositions(AbstractContainerScreen<?> screen);

    public boolean renderFakeItem() {
        return false;
    }
}
