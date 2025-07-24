package fuzs.vehicleupgrade.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Allows for rendering text and sprites together.
 */
public interface RenderableComponent {

    int getWidth(Font font);

    void renderComponent(GuiGraphics guiGraphics, Font font, int posX, int posY);

    static int getFullWidth(Stream<RenderableComponent> stream, Font font) {
        return stream.mapToInt((RenderableComponent component) -> component.getWidth(font)).sum();
    }

    static void renderComponents(GuiGraphics guiGraphics, Font font, int posX, int posY, Collection<RenderableComponent> list) {
        posX -= RenderableComponent.getFullWidth(list.stream(), font) / 2;
        for (RenderableComponent component : list) {
            component.renderComponent(guiGraphics, font, posX, posY);
            posX += component.getWidth(font);
        }
    }

    static RenderableComponent ofText(Component component) {
        return new RenderableComponent() {
            @Override
            public int getWidth(Font font) {
                return font.width(component);
            }

            @Override
            public void renderComponent(GuiGraphics guiGraphics, Font font, int posX, int posY) {
                guiGraphics.drawString(font, component, posX, posY, 0XFF404040, false);
            }
        };
    }

    static RenderableComponent ofSprite(ResourceLocation resourceLocation, int iconSize) {
        return ofSprite(resourceLocation, resourceLocation, iconSize, iconSize);
    }

    static RenderableComponent ofSprite(ResourceLocation foregroundLocation, @Nullable ResourceLocation backgroundLocation, int iconSize, int iconWidth) {
        int iconOffsetX = (iconWidth - iconSize) / 2;
        return new RenderableComponent() {
            @Override
            public int getWidth(Font font) {
                return iconWidth;
            }

            @Override
            public void renderComponent(GuiGraphics guiGraphics, Font font, int posX, int posY) {
                if (backgroundLocation != null) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                            backgroundLocation,
                            posX + iconOffsetX,
                            posY,
                            iconSize,
                            iconSize);
                }
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                        foregroundLocation,
                        posX + iconOffsetX,
                        posY,
                        iconSize,
                        iconSize);
            }
        };
    }
}
