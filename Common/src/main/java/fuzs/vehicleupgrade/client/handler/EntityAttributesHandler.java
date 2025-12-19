package fuzs.vehicleupgrade.client.handler;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class EntityAttributesHandler {
    private static final ResourceLocation ARMOR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_full");
    private static final ResourceLocation HEART_VEHICLE_FULL_SPRITE = VehicleUpgrade.id("hud/heart/vehicle_full");

    public static void onAfterBackground(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (screen instanceof HorseInventoryScreen horseInventoryScreen
                && horseInventoryScreen.horse.getInventoryColumns() == 0) {
            renderMobAttributes(screen, guiGraphics, horseInventoryScreen.horse);
        }
    }

    public static void renderMobAttributes(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, Mob mob) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).mobAttributesInInventory) {
            return;
        }

        if (mob.getHealth() > 0.0F) {
            submitAttributeComponent(screen,
                    guiGraphics,
                    getValueComponent(Mth.ceil(mob.getHealth()),
                            Mth.ceil(mob.getMaxHealth()),
                            HEART_VEHICLE_FULL_SPRITE),
                    124,
                    32);
        }

        if (mob.getArmorValue() > 0) {
            submitAttributeComponent(screen,
                    guiGraphics,
                    getValueComponent(mob.getArmorValue(), ARMOR_FULL_SPRITE),
                    124,
                    48);
        }
    }

    private static Component getValueComponent(int value, ResourceLocation resourceLocation) {
        return Component.literal(value + "x")
                .append(Component.object(new AtlasSprite(AtlasIds.GUI, resourceLocation)).withColor(-1));
    }

    private static Component getValueComponent(int value, int maxValue, ResourceLocation resourceLocation) {
        return Component.empty()
                .append(getValueComponent(value, resourceLocation))
                .append(" / ")
                .append(getValueComponent(maxValue, resourceLocation));
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
