package fuzs.vehicleupgrade.client.gui.components.debug;

import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @see net.minecraft.client.gui.components.debug.DebugEntryLookingAtEntity
 */
public final class DebugEntryLookingAtAttributes implements DebugScreenEntry {
    private static final ResourceLocation GROUP = ResourceLocation.withDefaultNamespace("looking_at_entity");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = minecraft.crosshairPickEntity;
        List<String> attributeLines = new ArrayList<>();
        if (entity instanceof LivingEntity livingEntity) {
            BuiltInRegistries.ATTRIBUTE.listElements().forEach((Holder.Reference<Attribute> holder) -> {
                if (holder.is(ModRegistry.DEBUG_ATTRIBUTES_ATTRIBUTE_TAG) && livingEntity.getAttributes()
                        .hasAttribute(holder)) {
                    double attributeValue = livingEntity.getAttributeValue(holder);
                    if (attributeValue != holder.value().getDefaultValue()) {
                        double baseValue = livingEntity.getAttributeBaseValue(holder);
                        ChatFormatting chatFormatting = getValueFormatting(attributeValue, baseValue);
                        attributeLines.add(
                                holder.getRegisteredName() + "=" + chatFormatting + "%.3f".formatted(attributeValue));
                    }
                }
            });
        }

        displayer.addToGroup(GROUP, attributeLines);
    }

    private ChatFormatting getValueFormatting(double attributeValue, double baseValue) {
        if (attributeValue > baseValue) {
            return ChatFormatting.GREEN;
        } else if (attributeValue < baseValue) {
            return ChatFormatting.RED;
        } else {
            return ChatFormatting.RESET;
        }
    }
}
