package fuzs.vehicleupgrade.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.MenuScreensContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenKeyboardEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenMouseEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.ExtractRenderStateCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.SubmitLivingEntityEvents;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.client.gui.components.debug.DebugEntryLookingAtAttributes;
import fuzs.vehicleupgrade.client.gui.screens.inventory.MountInventoryScreen;
import fuzs.vehicleupgrade.client.handler.*;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
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
        ScreenEvents.afterBackground(AbstractContainerScreen.class)
                .register(EntityAttributesHandler::onAfterBackground);
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
        ScreenEvents.remove(Screen.class).register(OpenMountInventoryHandler::onRemove);
        ScreenKeyboardEvents.beforeCharacterType(Screen.class)
                .register(OpenMountInventoryHandler::onBeforeCharacterType);
        ScreenKeyboardEvents.afterCharacterType(Screen.class).register(OpenMountInventoryHandler::onAfterCharacterType);
        ExtractRenderStateCallback.EVENT.register(TranslucentMountHandler::onExtractRenderState);
        SubmitLivingEntityEvents.BEFORE.register(TranslucentMountHandler::onBeforeSubmitLivingEntity);
    }

    @Override
    public void onClientSetup() {
        // Resource location path is important; it is used for sorting (namespace is not used, unfortunately).
        // We want to be sorted after vanilla, which has the id "looking_at_entity".
        DebugScreenEntries.register(VehicleUpgrade.id("looking_at_entity_attributes"),
                new DebugEntryLookingAtAttributes());
    }

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.EQUIPMENT_USER_MENU_TYPE.value(), MountInventoryScreen::new);
    }
}
