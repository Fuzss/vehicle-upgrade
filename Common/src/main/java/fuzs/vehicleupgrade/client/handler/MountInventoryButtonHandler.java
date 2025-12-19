package fuzs.vehicleupgrade.client.handler;

import fuzs.puzzleslib.api.client.gui.v2.components.tooltip.TooltipBuilder;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.config.VehicleInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTabs;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class MountInventoryButtonHandler {
    public static final WidgetSprites CROSS_SPRITES = new WidgetSprites(VehicleUpgrade.id("container/inventory/cross"),
            VehicleUpgrade.id("container/inventory/cross_highlighted"));
    /**
     * @see Inventory#getName()
     */
    static final Component INVENTORY_COMPONENT = Component.translatable("container.inventory");

    @Nullable
    private static Button playerInventoryButton;

    public static void onAfterInit(Minecraft minecraft, AbstractContainerScreen<?> screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, UnaryOperator<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget) {
        // check the screen, both inventory screens open consecutively for creative, survival will falsely override the creative button then
        if (minecraft.screen == screen && OpenMountInventoryHandler.isServerControlledInventory(minecraft.player)) {
            playerInventoryButton = createPlayerInventoryButton(screen);
            updatePlayerInventoryButtons(screen, playerInventoryButton);
            if (playerInventoryButton != null) {
                addWidget.apply(playerInventoryButton);
            }
            Button vehicleInventoryButton = createVehicleInventoryButton(screen);
            if (vehicleInventoryButton != null) {
                addWidget.apply(vehicleInventoryButton);
            }
        }
    }

    private static @Nullable Button createPlayerInventoryButton(AbstractContainerScreen<?> screen) {
        Vector2i vector2i = getButtonPositions(screen);
        Entity playerVehicle = screen.minecraft.player.getVehicle();
        if (vector2i != null && playerVehicle != null) {
            Button button = new ImageButton(vector2i.x(), vector2i.y(), 13, 13, CROSS_SPRITES, (Button buttonX) -> {
                VehicleInventory.VEHICLE.openInventory(screen.minecraft, screen);
            });
            TooltipBuilder.create(playerVehicle.getDisplayName()).build(button);
            return button;
        } else {
            return null;
        }
    }

    public static @Nullable Vector2i getButtonPositions(AbstractContainerScreen<?> screen) {
        if (screen instanceof CreativeModeInventoryScreen) {
            return new Vector2i(screen.leftPos + screen.imageWidth - 13 - 7,
                    screen.topPos + 3 + (hasAdditionalPages(screen.minecraft) ? 14 : 0));
        } else if (screen instanceof InventoryScreen) {
            return new Vector2i(screen.leftPos + screen.imageWidth - 13 - 7, screen.topPos + 3);
        } else {
            return null;
        }
    }

    /**
     * Adapted from {@code FabricCreativeInventoryScreen::hasAdditionalPages}.
     */
    private static boolean hasAdditionalPages(Minecraft minecraft) {
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isFabricLike()) {
            int tabs;

            if (minecraft.player.canUseGameMasterBlocks() && minecraft.options.operatorItemsTab().get()) {
                tabs = 14;
            } else {
                tabs = 13;
            }

            return CreativeModeTabs.tabs().size() > tabs;
        } else {
            return false;
        }
    }

    private static @Nullable Button createVehicleInventoryButton(AbstractContainerScreen<?> screen) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).vehicleInventoryButton) {
            return null;
        }

        if (!VehicleInventory.isPlayerInventory(screen)) {
            Button button = new ImageButton(screen.leftPos + screen.imageWidth - 13 - 7,
                    screen.topPos + 3,
                    13,
                    13,
                    CROSS_SPRITES,
                    (Button buttonX) -> {
                        VehicleInventory.PLAYER.openInventory(screen.minecraft, screen);
                    });
            TooltipBuilder.create(INVENTORY_COMPONENT).build(button);
            return button;
        } else {
            return null;
        }
    }

    public static void onAfterMouseClick(AbstractContainerScreen<?> screen, double mouseX, double mouseY, int mouseButton) {
        updatePlayerInventoryButtons(screen, playerInventoryButton);
    }

    public static void onAfterKeyPress(AbstractContainerScreen<?> screen, int keyCode, int scanCode, int modifiers) {
        updatePlayerInventoryButtons(screen, playerInventoryButton);
    }

    public static void onAfterMouseRelease(AbstractContainerScreen<?> screen, double mouseX, double mouseY, int mouseButton) {
        updatePlayerInventoryButtons(screen, playerInventoryButton);
    }

    private static void updatePlayerInventoryButtons(AbstractContainerScreen<?> screen, @Nullable Button button) {
        if (button != null) {
            if (screen instanceof InventoryScreen) {
                Vector2i vector2i = getButtonPositions(screen);
                Objects.requireNonNull(vector2i, "button positions is null");
                button.setX(vector2i.x());
            }

            if (screen instanceof CreativeModeInventoryScreen inventoryScreen && hasAdditionalPages(screen.minecraft)) {
                button.visible = inventoryScreen.isInventoryOpen();
            }
        }
    }

    public static void onRemove(AbstractContainerScreen<?> screen) {
        if (VehicleInventory.isPlayerInventory(screen)) {
            playerInventoryButton = null;
        }
    }
}
