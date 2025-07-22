package fuzs.vehicleupgrade.client.handler;

import fuzs.puzzleslib.api.client.gui.v2.tooltip.TooltipBuilder;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.config.VehicleButton;
import fuzs.vehicleupgrade.network.client.ServerboundOpenServerControlledInventoryMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class MountInventoryButtonHandler {
    public static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(VehicleUpgrade.id("container/inventory/button"),
            VehicleUpgrade.id("container/inventory/button_highlighted"));
    public static final WidgetSprites ICON_SPRITES = new WidgetSprites(VehicleUpgrade.id("container/inventory/chest"),
            VehicleUpgrade.id("container/inventory/chest_highlighted"));

    @Nullable
    private static Button button;

    public static void onAfterInit(Minecraft minecraft, AbstractContainerScreen<?> screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, UnaryOperator<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget) {
        if (OpenMountInventoryHandler.isServerControlledInventory(minecraft.player) && minecraft.screen == screen) {
            Button button = MountInventoryButtonHandler.button = createServerControlledInventoryButton(screen);
            updateServerControlledInventoryButton(screen, button);
            if (button != null) {
                addWidget.apply(button);
            }
        }
    }

    private static @Nullable Button createServerControlledInventoryButton(AbstractContainerScreen<?> screen) {
        VehicleButton vehicleButton = VehicleUpgrade.CONFIG.get(ClientConfig.class).vehicleButton;
        Vector2i vector2i = vehicleButton.getButtonPositions(screen);
        Entity playerVehicle = screen.minecraft.player.getVehicle();
        if (vector2i != null && playerVehicle != null) {
            ItemStack itemStack = new ItemStack(getVehicleEntityItem(playerVehicle));
            Button button = new ImageButton(vector2i.x(),
                    vector2i.y(),
                    vehicleButton.width,
                    vehicleButton.height,
                    vehicleButton.sprites,
                    (Button buttonX) -> {
                        MessageSender.broadcast(new ServerboundOpenServerControlledInventoryMessage(playerVehicle.getId()));
                    }) {

                @Override
                public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
                    if (vehicleButton.renderFakeItem()) {
                        guiGraphics.renderFakeItem(itemStack, this.getX() + 2, this.getY() + 1);
                    }
                }
            };
            TooltipBuilder.create(playerVehicle.getDisplayName()).build(button);
            return button;
        } else {
            return null;
        }
    }

    private static Item getVehicleEntityItem(Entity entity) {
        return entity instanceof VehicleEntity vehicleEntity ? vehicleEntity.getDropItem() : Items.SADDLE;
    }

    public static void onAfterMouseClick(AbstractContainerScreen<?> screen, double mouseX, double mouseY, int mouseButton) {
        updateServerControlledInventoryButton(screen, button);
    }

    public static void onAfterKeyPress(AbstractContainerScreen<?> screen, int keyCode, int scanCode, int modifiers) {
        updateServerControlledInventoryButton(screen, button);
    }

    public static void onAfterMouseRelease(AbstractContainerScreen<?> screen, double mouseX, double mouseY, int mouseButton) {
        updateServerControlledInventoryButton(screen, button);
    }

    private static void updateServerControlledInventoryButton(AbstractContainerScreen<?> screen, @Nullable Button button) {
        if (button != null) {
            if (screen instanceof InventoryScreen) {
                button.setX(screen.leftPos + 104 + 20 + 8);
            }
            if (screen instanceof CreativeModeInventoryScreen inventoryScreen) {
                button.visible = inventoryScreen.isInventoryOpen();
            }
        }
    }

    public static void onRemove(AbstractContainerScreen<?> screen) {
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            button = null;
        }
    }
}
