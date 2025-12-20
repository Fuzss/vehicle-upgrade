package fuzs.vehicleupgrade.client.gui.screens.inventory;

import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.world.inventory.MountInventoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.Nullable;

public class MountInventoryScreen extends AbstractMountInventoryScreen<MountInventoryMenu> {
    private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
    private static final Identifier NAUTILUS_INVENTORY_LOCATION = Identifier.withDefaultNamespace(
            "textures/gui/container/nautilus.png");

    public MountInventoryScreen(MountInventoryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component, 0, menu.getMount());
    }

    public @Nullable LivingEntity getMount() {
        // This can be null when the entity is not found on the client.
        return this.mount;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        if (this.getMount() != null) {
            super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        }
    }

    @Override
    protected Identifier getBackgroundTextureLocation() {
        return NAUTILUS_INVENTORY_LOCATION;
    }

    @Override
    protected Identifier getSlotSpriteLocation() {
        return SLOT_SPRITE;
    }

    @Override
    protected @Nullable Identifier getChestSlotsSpriteLocation() {
        return null;
    }

    @Override
    protected boolean shouldRenderSaddleSlot() {
        return this.mount.canUseSlot(EquipmentSlot.SADDLE) && this.mount.getType().is(EntityTypeTags.CAN_EQUIP_SADDLE);
    }

    @Override
    protected boolean shouldRenderArmorSlot() {
        return this.mount.canUseSlot(EquipmentSlot.BODY) && this.mount.getType()
                .is(ModRegistry.CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG);
    }
}
