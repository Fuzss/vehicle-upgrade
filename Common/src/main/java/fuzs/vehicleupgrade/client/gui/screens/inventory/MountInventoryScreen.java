package fuzs.vehicleupgrade.client.gui.screens.inventory;

import fuzs.vehicleupgrade.world.inventory.MountInventoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class MountInventoryScreen extends AbstractContainerScreen<MountInventoryMenu> {
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot");
    private static final ResourceLocation HORSE_INVENTORY_LOCATION = ResourceLocation.withDefaultNamespace(
            "textures/gui/container/horse.png");

    public final LivingEntity mount;
    private float xMouse;
    private float yMouse;

    public MountInventoryScreen(MountInventoryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.mount = menu.getMount();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(HORSE_INVENTORY_LOCATION,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256);

        for (Slot slot : this.getMenu().slots) {
            if (slot.isActive() && !(slot.container instanceof Inventory)) {
                guiGraphics.blitSprite(SLOT_SPRITE, this.leftPos + slot.x - 1, this.topPos + slot.y - 1, 18, 18);
            }
        }

        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics,
                this.leftPos + 26,
                this.topPos + 18,
                this.leftPos + 78,
                this.topPos + 70,
                this.getMountSize(),
                0.0625F,
                this.xMouse,
                this.yMouse,
                this.mount);
    }

    private int getMountSize() {
        return Math.round(80.0F / (this.mount.getBbHeight() + 1.5F * this.mount.getBbWidth()));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.xMouse = (float) mouseX;
        this.yMouse = (float) mouseY;
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
