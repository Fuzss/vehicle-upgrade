package fuzs.vehicleupgrade.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.MenuScreensContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.GatherDebugInformationEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenKeyboardEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenMouseEvents;
import fuzs.vehicleupgrade.client.gui.screens.inventory.EquipmentInventoryScreen;
import fuzs.vehicleupgrade.client.handler.BoatItemViewHandler;
import fuzs.vehicleupgrade.client.handler.DebugAttributesHandler;
import fuzs.vehicleupgrade.client.handler.MountInventoryButtonHandler;
import fuzs.vehicleupgrade.client.handler.OpenMountInventoryHandler;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;

public class VehicleUpgradeClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandler();
    }

    private static void registerEventHandler() {
        ClientTickEvents.START.register(BoatItemViewHandler::onStartClientTick);
        ClientTickEvents.END.register(BoatItemViewHandler::onEndClientTick);
        GatherDebugInformationEvents.SYSTEM.register(DebugAttributesHandler::onGatherSystemInformation);
        ScreenEvents.afterInit(AbstractContainerScreen.class).register(MountInventoryButtonHandler::onAfterInit);
        ScreenMouseEvents.afterMouseClick(AbstractContainerScreen.class)
                .register(MountInventoryButtonHandler::onAfterMouseClick);
        ScreenMouseEvents.afterMouseRelease(AbstractContainerScreen.class)
                .register(MountInventoryButtonHandler::onAfterMouseRelease);
        ScreenKeyboardEvents.afterKeyPress(AbstractContainerScreen.class)
                .register(MountInventoryButtonHandler::onAfterKeyPress);
        ScreenEvents.remove(AbstractContainerScreen.class).register(MountInventoryButtonHandler::onRemove);
        ScreenKeyboardEvents.beforeKeyPress(Screen.class).register(OpenMountInventoryHandler::onBeforeKeyPress);
        ClientTickEvents.START.register(OpenMountInventoryHandler::onStartClientTick);
        ScreenEvents.afterRender(HorseInventoryScreen.class).register(DebugAttributesHandler::onAfterRender);
    }

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.EQUIPMENT_USER_MENU_TYPE.value(), EquipmentInventoryScreen::new);
    }
}
