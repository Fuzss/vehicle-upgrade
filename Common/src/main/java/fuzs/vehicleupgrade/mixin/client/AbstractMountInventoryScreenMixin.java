package fuzs.vehicleupgrade.mixin.client;

import fuzs.vehicleupgrade.client.gui.screens.inventory.MountInventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractMountInventoryScreen.class)
abstract class AbstractMountInventoryScreenMixin<T extends AbstractMountInventoryMenu> extends AbstractContainerScreen<T> {
    @Shadow
    public LivingEntity mount;

    public AbstractMountInventoryScreenMixin(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @ModifyArg(method = "renderBg",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;renderEntityInInventoryFollowsMouse(Lnet/minecraft/client/gui/GuiGraphics;IIIIIFFFLnet/minecraft/world/entity/LivingEntity;)V"),
               index = 5)
    protected int renderBg(int scale) {
        return MountInventoryScreen.class.isInstance(this) ?
                Math.round(80.0F / (this.mount.getBbHeight() + 1.5F * this.mount.getBbWidth())) : scale;
    }

    @ModifyArg(method = "renderBg",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;renderEntityInInventoryFollowsMouse(Lnet/minecraft/client/gui/GuiGraphics;IIIIIFFFLnet/minecraft/world/entity/LivingEntity;)V"),
               index = 6)
    protected float renderBg(float scale) {
        return MountInventoryScreen.class.isInstance(this) ? 0.0625F : scale;
    }
}
