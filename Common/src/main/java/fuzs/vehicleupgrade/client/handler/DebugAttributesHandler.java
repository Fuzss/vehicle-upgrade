package fuzs.vehicleupgrade.client.handler;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.ArrayList;
import java.util.List;

public class DebugAttributesHandler {
    static final String TARGETED_ENTITY_LINE = ChatFormatting.UNDERLINE + "Targeted Entity";

    public static void onGatherSystemInformation(List<String> lines) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).debugEntityAttributes) return;
        Entity pickedEntity = Minecraft.getInstance().crosshairPickEntity;
        if (pickedEntity instanceof LivingEntity livingEntity) {
            for (int i = lines.size() - 1; i >= 0; i--) {
                if (lines.get(i).equals(TARGETED_ENTITY_LINE)) {
                    if ((i += 2) <= lines.size()) {
                        List<String> attributeLines = new ArrayList<>();
                        BuiltInRegistries.ATTRIBUTE.listElements().forEach((Holder.Reference<Attribute> holder) -> {
                            if (livingEntity.getAttributes().hasAttribute(holder)) {
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
}
