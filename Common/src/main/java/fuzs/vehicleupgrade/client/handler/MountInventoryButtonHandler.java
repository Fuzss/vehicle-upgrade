package fuzs.vehicleupgrade.client.handler;

import fuzs.puzzleslib.api.client.gui.v2.tooltip.TooltipBuilder;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.config.PlayerVehicleInventoryButton;
import fuzs.vehicleupgrade.config.VehicleInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class MountInventoryButtonHandler {
    public static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(VehicleUpgrade.id("container/inventory/button"),
            VehicleUpgrade.id("container/inventory/button_highlighted"));
    public static final WidgetSprites ICON_SPRITES = new WidgetSprites(VehicleUpgrade.id("container/inventory/chest"),
            VehicleUpgrade.id("container/inventory/chest_highlighted"));
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
        PlayerVehicleInventoryButton vehicleButton = VehicleUpgrade.CONFIG.get(ClientConfig.class).playerButton;
        Vector2i vector2i = vehicleButton.getButtonPositions(screen);
        Entity playerVehicle = screen.minecraft.player.getVehicle();
        if (vector2i != null && playerVehicle != null) {
            ItemStack itemStack = getVehicleEntityItem(playerVehicle);
            boolean isSaddle = itemStack.is(Items.SADDLE);
            Button button = new ImageButton(vector2i.x(),
                    vector2i.y(),
                    vehicleButton.width,
                    vehicleButton.height,
                    vehicleButton.sprites,
                    (Button buttonX) -> {
                        VehicleInventory.VEHICLE.openInventory(screen.minecraft, screen);
                    }) {

                @Override
                public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
                    if (vehicleButton.renderFakeItem()) {
                        guiGraphics.renderFakeItem(itemStack,
                                this.getX() + 1 + (isSaddle ? 0 : 1),
                                this.getY() + (isSaddle ? 0 : 1));
                    }
                }
            };
            TooltipBuilder.create(playerVehicle.getDisplayName()).build(button);
            return button;
        } else {
            return null;
        }
    }

    private static ItemStack getVehicleEntityItem(Entity entity) {
        if (entity instanceof VehicleEntity vehicleEntity) {
            return new ItemStack(vehicleEntity.getDropItem());
        } else if (entity instanceof Mob mob && mob.isSaddled()) {
            return mob.getItemBySlot(EquipmentSlot.SADDLE);
        } else {
            return new ItemStack(Items.SADDLE);
        }
    }

    private static @Nullable Button createVehicleInventoryButton(AbstractContainerScreen<?> screen) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).vehicleButton) {
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
                Vector2i vector2i = VehicleUpgrade.CONFIG.get(ClientConfig.class).playerButton.getButtonPositions(screen);
                Objects.requireNonNull(vector2i, "button positions is null");
                button.setX(vector2i.x());
            }
            if (screen instanceof CreativeModeInventoryScreen inventoryScreen) {
                button.visible = inventoryScreen.isInventoryOpen();
            }
        }
    }

    public static void onRemove(AbstractContainerScreen<?> screen) {
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            playerInventoryButton = null;
        }
    }
}
