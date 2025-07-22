package fuzs.vehicleupgrade.client.gui.screens.inventory;

import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.world.inventory.EquipmentInventoryMenu;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;

public class EquipmentInventoryScreen extends AbstractContainerScreen<EquipmentInventoryMenu> {
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot");
    private static final ResourceLocation HORSE_INVENTORY_LOCATION = ResourceLocation.withDefaultNamespace(
            "textures/gui/container/horse.png");
    private static final ResourceLocation ARMOR_EMPTY_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_empty");
    private static final ResourceLocation ARMOR_HALF_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_half");
    private static final ResourceLocation ARMOR_FULL_SPRITE = ResourceLocation.withDefaultNamespace("hud/armor_full");

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

        Gui.HeartType heartType = getHeartType(mob);
        renderCenteredAttribute(guiGraphics,
                this.font,
                (int) mob.getHealth(),
                (int) mob.getMaxHealth(),
                this.leftPos + 124,
                this.topPos + 32,
                heartType.getSprite(false, false, false),
                heartType.getSprite(false, true, false),
                Gui.HeartType.CONTAINER.getSprite(false, false, false),
                true);
        renderCenteredAttribute(guiGraphics,
                this.font,
                mob.getArmorValue(),
                (int) (Math.ceil(mob.getArmorValue() / 2.0) * 2.0),
                this.leftPos + 124,
                this.topPos + 48,
                ARMOR_FULL_SPRITE,
                ARMOR_HALF_SPRITE,
                ARMOR_EMPTY_SPRITE,
                false);

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

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.xMouse = (float) mouseX;
        this.yMouse = (float) mouseY;
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private static void renderCenteredAttribute(GuiGraphics guiGraphics, Font font, int value, int maxValue, int posX, int posY, ResourceLocation fullSprite, ResourceLocation halfSprite, ResourceLocation emptySprite, boolean alwaysDrawEmptySprite) {
        if (maxValue <= 20) {
            for (int i = 0; i < maxValue / 2; i++) {
                int x = posX - (maxValue / 2 * 9 - Math.max(0, maxValue / 2 - 1)) / 2 + i * 8;

                if (alwaysDrawEmptySprite || i * 2 + 1 > value) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, emptySprite, x, posY, 9, 9);
                }

                if (i * 2 + 1 < value) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, fullSprite, x, posY, 9, 9);
                }

                if (i * 2 + 1 == value) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, halfSprite, x, posY, 9, 9);
                }
            }
        } else {
            Component component = Component.literal(String.valueOf(value)).append("x");
            Component component1 = Component.literal("/");
            Component component2 = Component.literal(String.valueOf(maxValue)).append("x");
            int iconSize = 9;
            int iconWidth = 12;
            int iconOffsetX = (iconWidth - iconSize) / 2;
            int width = font.width(component) + iconWidth + font.width(component1) + font.width(component2) + iconWidth;
            posX -= width / 2;
            guiGraphics.drawString(font, component, posX, posY, -12566464, false);
            posX += font.width(component);
            if (alwaysDrawEmptySprite) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                        emptySprite,
                        posX + iconOffsetX,
                        posY,
                        iconSize,
                        iconSize);
            }
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    fullSprite,
                    posX + iconOffsetX,
                    posY,
                    iconSize,
                    iconSize);
            posX += iconWidth;
            guiGraphics.drawString(font, component1, posX, posY, -12566464, false);
            posX += font.width(component1);
            guiGraphics.drawString(font, component2, posX, posY, -12566464, false);
            posX += font.width(component2);
            if (alwaysDrawEmptySprite) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                        emptySprite,
                        posX + iconOffsetX,
                        posY,
                        iconSize,
                        iconSize);
            }
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    fullSprite,
                    posX + iconOffsetX,
                    posY,
                    iconSize,
                    iconSize);
            posX += iconWidth;
        }
    }

    private static int getMobScale(float width, float height) {
        return Math.round(80.0F / (height + 1.5F * width));
    }

    public static Gui.HeartType getHeartType(LivingEntity livingEntity) {
        if (livingEntity.hasEffect(MobEffects.POISON)) {
            return Gui.HeartType.POISIONED;
        } else if (livingEntity.hasEffect(MobEffects.WITHER)) {
            return Gui.HeartType.WITHERED;
        } else if (livingEntity.isFullyFrozen()) {
            return Gui.HeartType.FROZEN;
        } else {
            return Gui.HeartType.NORMAL;
        }
    }

    record AttributeSprites(ResourceLocation fullSprite, ResourceLocation halfSprite, ResourceLocation emptySprite) {

    }
}
