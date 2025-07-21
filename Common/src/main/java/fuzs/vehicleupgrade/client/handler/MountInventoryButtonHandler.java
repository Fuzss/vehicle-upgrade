package fuzs.vehicleupgrade.client.handler;

import fuzs.puzzleslib.api.client.gui.v2.tooltip.TooltipBuilder;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.vehicleupgrade.VehicleUpgrade;
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
    public static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(VehicleUpgrade.id("button"),
            VehicleUpgrade.id("button_highlighted"));

    @Nullable
    private static Button button;

    public static void onAfterInit(Minecraft minecraft, AbstractContainerScreen<?> screen, int screenWidth, int screenHeight, List<AbstractWidget> widgets, UnaryOperator<AbstractWidget> addWidget, Consumer<AbstractWidget> removeWidget) {
        if (OpenMountInventoryHandler.isServerControlledInventory(minecraft.player) && minecraft.screen == screen) {
            Button button = createServerControlledInventoryButton(screen);
            updateServerControlledInventoryButton(screen, button);
            addWidget.apply(button);
            MountInventoryButtonHandler.button = button;
        }
    }

    private static @Nullable Button createServerControlledInventoryButton(AbstractContainerScreen<?> screen) {
        Vector2i vector2i = getButtonPositions(screen);
        Entity playerVehicle = screen.minecraft.player.getVehicle();
        if (vector2i != null && playerVehicle != null) {
            ItemStack itemStack = new ItemStack(getVehicleEntityItem(playerVehicle));
            Button button = new ImageButton(vector2i.x(), vector2i.y(), 20, 18, BUTTON_SPRITES, (Button buttonX) -> {
                MessageSender.broadcast(new ServerboundOpenServerControlledInventoryMessage(playerVehicle.getId()));
            }) {

                @Override
                public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
                    guiGraphics.renderFakeItem(itemStack, this.getX() + 2, this.getY() + 1);
                }
            };
            TooltipBuilder.create(playerVehicle.getDisplayName()).build(button);
            return button;
        } else {
            return null;
        }
    }

    private static @Nullable Vector2i getButtonPositions(AbstractContainerScreen<?> screen) {
        if (screen instanceof CreativeModeInventoryScreen) {
            return new Vector2i(screen.leftPos + 104 + 20 + 2 + 12, screen.height / 2 - 50);
        } else if (screen instanceof InventoryScreen) {
            return new Vector2i(screen.leftPos + 104 + 20 + 8, screen.height / 2 - 22);
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
