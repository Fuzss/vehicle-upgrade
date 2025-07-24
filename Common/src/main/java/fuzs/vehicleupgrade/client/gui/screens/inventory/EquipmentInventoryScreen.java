package fuzs.vehicleupgrade.client.gui.screens.inventory;

import fuzs.vehicleupgrade.client.handler.DebugAttributesHandler;
import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.world.inventory.EquipmentInventoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;

public class EquipmentInventoryScreen extends AbstractContainerScreen<EquipmentInventoryMenu> {
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot");
    private static final ResourceLocation HORSE_INVENTORY_LOCATION = ResourceLocation.withDefaultNamespace(
            "textures/gui/container/horse.png");

    private float xMouse;
    private float yMouse;

    public EquipmentInventoryScreen(EquipmentInventoryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                HORSE_INVENTORY_LOCATION,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
        Mob mob = this.menu.getMob();

        if (mob.canUseSlot(EquipmentSlot.SADDLE) && mob.getType().is(EntityTypeTags.CAN_EQUIP_SADDLE)) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    SLOT_SPRITE,
                    this.leftPos + 7,
                    this.topPos + 35 - 18,
                    18,
                    18);
        }

        if (mob.canUseSlot(EquipmentSlot.BODY) && (mob.getType().is(ModRegistry.CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG))) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    SLOT_SPRITE,
                    this.leftPos + 7,
                    this.topPos + 35,
                    18,
                    18);
        }

        DebugAttributesHandler.renderMobAttributes(this, guiGraphics, mob);
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics,
                this.leftPos + 26,
                this.topPos + 18,
                this.leftPos + 78,
                this.topPos + 70,
                getMobScale(mob.getBbWidth(), mob.getBbHeight()),
                0.0625F,
                this.xMouse,
                this.yMouse,
                mob);
    }

    private static int getMobScale(float width, float height) {
        return Math.round(80.0F / (height + 1.5F * width));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.xMouse = (float) mouseX;
        this.yMouse = (float) mouseY;
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
