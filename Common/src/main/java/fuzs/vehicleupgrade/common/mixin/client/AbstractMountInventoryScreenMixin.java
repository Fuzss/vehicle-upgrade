package fuzs.vehicleupgrade.common.mixin.client;

import fuzs.vehicleupgrade.common.client.gui.screens.inventory.MountInventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractMountInventoryScreen.class)
abstract class AbstractMountInventoryScreenMixin<T extends AbstractMountInventoryMenu> extends AbstractContainerScreen<T> {
    @Shadow
    @Final
    public LivingEntity mount;

    public AbstractMountInventoryScreenMixin(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @ModifyArg(method = "extractBackground",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;extractEntityInInventoryFollowsMouse(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIIIIFFFLnet/minecraft/world/entity/LivingEntity;)V"),
               index = 5)
    protected int extractBackground(int size) {
        return MountInventoryScreen.class.isInstance(this) ?
                Math.round(80.0F / (this.mount.getBbHeight() + 1.5F * this.mount.getBbWidth())) : size;
    }

    @ModifyArg(method = "extractBackground",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;extractEntityInInventoryFollowsMouse(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIIIIFFFLnet/minecraft/world/entity/LivingEntity;)V"),
               index = 6)
    protected float extractBackground(float offsetY) {
        return MountInventoryScreen.class.isInstance(this) ? 0.0625F : offsetY;
    }
}
