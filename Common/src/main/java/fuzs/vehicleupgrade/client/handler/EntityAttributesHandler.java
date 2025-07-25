package fuzs.vehicleupgrade.client.handler;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.client.gui.components.RenderableComponent;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EntityAttributesHandler {
    static final String TARGETED_ENTITY_LINE = ChatFormatting.UNDERLINE + "Targeted Entity";
    private static final ResourceLocation ARMOR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_full");
    private static final ResourceLocation HEART_VEHICLE_CONTAINER_SPRITE = ResourceLocation.withDefaultNamespace(
            "hud/heart/vehicle_container");
    private static final ResourceLocation HEART_VEHICLE_FULL_SPRITE = ResourceLocation.withDefaultNamespace(
            "hud/heart/vehicle_full");

    public static void onGatherSystemInformation(List<String> lines) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).debugEntityAttributes) return;
        Entity pickedEntity = Minecraft.getInstance().crosshairPickEntity;
        if (pickedEntity instanceof LivingEntity livingEntity) {
            for (int i = lines.size() - 1; i >= 0; i--) {
                if (lines.get(i).equals(TARGETED_ENTITY_LINE)) {
                    if ((i += 2) <= lines.size()) {
                        List<String> attributeLines = new ArrayList<>();
                        BuiltInRegistries.ATTRIBUTE.listElements().forEach((Holder.Reference<Attribute> holder) -> {
                            if (holder.is(ModRegistry.DEBUG_ATTRIBUTES_ATTRIBUTE_TAG) && livingEntity.getAttributes()
                                    .hasAttribute(holder)) {
                                double attributeValue = livingEntity.getAttributeValue(holder);
                                if (attributeValue != holder.value().getDefaultValue()) {
                                    double baseValue = livingEntity.getAttributeBaseValue(holder);
                                    ChatFormatting chatFormatting = getValueFormatting(attributeValue, baseValue);
                                    attributeLines.add(
                                            holder.getRegisteredName() + "=" + chatFormatting + "%.3f".formatted(
                                                    attributeValue));
                                }
                            }

                        });
                        lines.addAll(i, attributeLines);
                    }
                    break;
                }
            }
        }
    }

    private static ChatFormatting getValueFormatting(double attributeValue, double baseValue) {
        if (attributeValue > baseValue) {
            return ChatFormatting.GREEN;
        } else if (attributeValue < baseValue) {
            return ChatFormatting.RED;
        } else {
            return ChatFormatting.RESET;
        }
    }

    public static void onDrawBackground(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (screen instanceof HorseInventoryScreen horseInventoryScreen
                && horseInventoryScreen.horse.getInventoryColumns() == 0) {
            renderMobAttributes(screen, guiGraphics, horseInventoryScreen.horse);
        }
    }

    public static void renderMobAttributes(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, Mob mob) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).mobAttributesInInventory) {
            return;
        }

        renderMobAttribute(guiGraphics,
                screen.getFont(),
                Mth.ceil(mob.getHealth()),
                Mth.ceil(mob.getMaxHealth()),
                screen.leftPos + 124,
                screen.topPos + 32,
                HEART_VEHICLE_FULL_SPRITE,
                HEART_VEHICLE_CONTAINER_SPRITE);
        renderMobAttribute(guiGraphics,
                screen.getFont(),
                mob.getArmorValue(),
                -1,
                screen.leftPos + 124,
                screen.topPos + 48,
                ARMOR_FULL_SPRITE,
                null);
    }

    private static void renderMobAttribute(GuiGraphics guiGraphics, Font font, int value, int maxValue, int posX, int posY, ResourceLocation fullSprite, @Nullable ResourceLocation emptySprite) {
        if (value > 0) {
            List<RenderableComponent> list = new ArrayList<>();
            list.add(RenderableComponent.ofText(Component.literal(value + "x")));
            list.add(RenderableComponent.ofSprite(fullSprite, emptySprite, 9, 12));
            if (maxValue != -1) {
                list.add(RenderableComponent.ofText(Component.literal("/")));
                list.add(RenderableComponent.ofText(Component.literal(maxValue + "x")));
                list.add(RenderableComponent.ofSprite(fullSprite, emptySprite, 9, 12));
            }
            RenderableComponent.renderComponents(guiGraphics, font, posX, posY, list);
        }
    }
}
