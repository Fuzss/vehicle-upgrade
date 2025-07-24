package fuzs.vehicleupgrade.config;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import org.joml.Vector2i;

public enum AnchorPoint {
    TOP_LEFT(-1, -1),
    TOP_CENTER(0, -1),
    TOP_RIGHT(1, -1),
    CENTER_LEFT(-1, 0),
    CENTER(0, 0),
    CENTER_RIGHT(1, 0),
    BOTTOM_LEFT(-1, 1),
    BOTTOM_CENTER(0, 1),
    BOTTOM_RIGHT(1, 1);

    private final int offsetX;
    private final int offsetY;

    AnchorPoint(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public boolean isLeft() {
        return this.offsetX == -1;
    }

    public boolean isCenter() {
        return this.offsetX == 0;
    }

    public boolean isRight() {
        return this.offsetX == 1;
    }

    public Positioner createPositioner(int guiWidth, int guiHeight, int elementWidth, int elementHeight) {
        return new PositionerImpl(this.offsetX, this.offsetY, guiWidth, guiHeight, elementWidth, elementHeight);
    }

    public interface Positioner {

        int getPosX(int posX);

        int getPosY(int posY);

        default Vector2i getPosition(int posX, int posY) {
            return new Vector2i(this.getPosX(posX), this.getPosY(posY));
        }

        ScreenRectangle getRectangle(int posX, int posY);
    }

    private record PositionerImpl(int offsetX,
                                  int offsetY,
                                  int guiWidth,
                                  int guiHeight,
                                  int elementWidth,
                                  int elementHeight) implements Positioner {

        @Override
        public int getPosX(int posX) {
            return Math.round(this.guiWidth / 2.0F + this.offsetX * (this.guiWidth / 2.0F - posX)
                    - (this.offsetX + 1) * this.elementWidth / 2.0F);
        }

        @Override
        public int getPosY(int posY) {
            return Math.round(this.guiHeight / 2.0F + this.offsetY * (this.guiHeight / 2.0F - posY)
                    - (this.offsetY + 1) * this.elementHeight / 2.0F);
        }

        @Override
        public ScreenRectangle getRectangle(int posX, int posY) {
            return new ScreenRectangle(this.getPosX(posX), this.getPosY(posY), this.elementWidth, this.elementHeight);
        }
    }
}
