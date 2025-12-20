package fuzs.vehicleupgrade.client.handler;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class EntityAttributesHandler {
    private static final Identifier ARMOR_FULL_SPRITE = Identifier.withDefaultNamespace("hud/armor_full");
    private static final Identifier HEART_VEHICLE_FULL_SPRITE = VehicleUpgrade.id("hud/heart/vehicle_full");

    public static void onAfterBackground(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).mobAttributesInInventory) {
            return;
        }

        if (screen instanceof AbstractMountInventoryScreen<?> mountInventoryScreen
                && mountInventoryScreen.inventoryColumns == 0
                && mountInventoryScreen.mount instanceof LivingEntity livingEntity) {
            if (livingEntity.getHealth() > 0.0F) {
                submitAttributeComponent(screen,
                        guiGraphics,
                        getValueComponent(Mth.ceil(livingEntity.getHealth()),
                                Mth.ceil(livingEntity.getMaxHealth()),
                                HEART_VEHICLE_FULL_SPRITE),
                        124,
                        32);
            }

            if (livingEntity.getArmorValue() > 0) {
                submitAttributeComponent(screen,
                        guiGraphics,
                        getValueComponent(livingEntity.getArmorValue(), ARMOR_FULL_SPRITE),
                        124,
                        48);
            }
        }
    }

    private static Component getValueComponent(int value, Identifier identifier) {
        return Component.literal(value + "x")
                .append(Component.object(new AtlasSprite(AtlasIds.GUI, identifier)).withColor(-1));
    }

    private static Component getValueComponent(int value, int maxValue, Identifier identifier) {
        return Component.empty()
                .append(getValueComponent(value, identifier))
                .append(" / ")
                .append(getValueComponent(maxValue, identifier));
    }

    private static void submitAttributeComponent(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, Component component, int posX, int posY) {
        guiGraphics.drawString(screen.getFont(),
                component,
                screen.leftPos + posX - screen.getFont().width(component) / 2,
                screen.topPos + posY,
                0XFF404040,
                false);
    }
}
