package fuzs.vehicleupgrade.client.handler;

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

    public static void onGatherSystemInformation(List<String> lines) {
        Entity pickedEntity = Minecraft.getInstance().crosshairPickEntity;
        if (pickedEntity instanceof LivingEntity livingEntity) {
            String s = ChatFormatting.UNDERLINE + "Targeted Entity";
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).equals(s)) {
                    if ((i += 2) <= lines.size()) {
                        List<String> attributeLines = new ArrayList<>();
                        BuiltInRegistries.ATTRIBUTE.listElements().forEach((Holder.Reference<Attribute> holder) -> {
                            if (livingEntity.getAttributes().hasAttribute(holder)) {
                                double attributeValue = livingEntity.getAttributeValue(holder);
                                if (attributeValue != holder.value().getDefaultValue()) {
                                    double baseValue = livingEntity.getAttributeBaseValue(holder);
                                    ChatFormatting prefix;
                                    if (attributeValue > baseValue) {
                                        prefix = ChatFormatting.GREEN;
                                    } else if (attributeValue < baseValue) {
                                        prefix = ChatFormatting.RED;
                                    } else {
                                        prefix = ChatFormatting.RESET;
                                    }
                                    attributeLines.add(holder.getRegisteredName() + "=" + prefix + "%.3f".formatted(
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
}
