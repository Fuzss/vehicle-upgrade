package fuzs.vehicleupgrade.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.MenuScreensContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.*;
import fuzs.puzzleslib.api.client.event.v1.renderer.ExtractRenderStateCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderLivingEvents;
import fuzs.vehicleupgrade.client.gui.screens.inventory.EquipmentInventoryScreen;
import fuzs.vehicleupgrade.client.handler.*;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class VehicleUpgradeClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandler();
    }

    private static void registerEventHandler() {
        ClientTickEvents.START.register(BoatItemViewHandler::onStartClientTick);
        ClientTickEvents.END.register(BoatItemViewHandler::onEndClientTick);
        GatherDebugInformationEvents.SYSTEM.register(EntityAttributesHandler::onGatherSystemInformation);
        ContainerScreenEvents.BACKGROUND.register(EntityAttributesHandler::onDrawBackground);
        ScreenEvents.afterInit(AbstractContainerScreen.class).register(MountInventoryButtonHandler::onAfterInit);
        ScreenMouseEvents.afterMouseClick(AbstractContainerScreen.class)
                .register(MountInventoryButtonHandler::onAfterMouseClick);
        ScreenMouseEvents.afterMouseRelease(AbstractContainerScreen.class)
                .register(MountInventoryButtonHandler::onAfterMouseRelease);
        ScreenKeyboardEvents.afterKeyPress(AbstractContainerScreen.class)
                .register(MountInventoryButtonHandler::onAfterKeyPress);
        ScreenEvents.remove(AbstractContainerScreen.class).register(MountInventoryButtonHandler::onRemove);
        ClientTickEvents.START.register(OpenMountInventoryHandler::onStartClientTick);
        ScreenKeyboardEvents.beforeKeyPress(Screen.class).register(OpenMountInventoryHandler::onBeforeKeyPress);
        ExtractRenderStateCallback.EVENT.register(TranslucentMountHandler::onExtractRenderState);
        RenderLivingEvents.BEFORE.register(TranslucentMountHandler::onBeforeRenderEntity);
        RenderLivingEvents.AFTER.register(TranslucentMountHandler::onAfterRenderEntity);
    }

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.EQUIPMENT_USER_MENU_TYPE.value(), EquipmentInventoryScreen::new);
    }
}
