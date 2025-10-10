package fuzs.vehicleupgrade.client.handler;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class BoatItemViewHandler {
    private static boolean isHandsBusy;
    private static ItemStack lastMainHandItem;
    private static ItemStack lastOffHandItem;

    public static void onStartClientTick(Minecraft minecraft) {
        if (VehicleUpgrade.CONFIG.get(ClientConfig.class).holdItemsWhileRowing && minecraft.player != null) {
            isHandsBusy = minecraft.player.isHandsBusy();
            lastMainHandItem = minecraft.gameRenderer.itemInHandRenderer.mainHandItem;
            lastOffHandItem = minecraft.gameRenderer.itemInHandRenderer.offHandItem;
        } else {
            isHandsBusy = false;
            lastMainHandItem = lastOffHandItem = ItemStack.EMPTY;
        }
    }

    public static void onEndClientTick(Minecraft minecraft) {
        if (minecraft.player != null && isHandsBusy) {
            ItemInHandRenderer itemInHandRenderer = minecraft.gameRenderer.itemInHandRenderer;
            if (lastMainHandItem.is(ModRegistry.HOLDABLE_WHILE_ROWING_ITEM_TAG)) {
                float mainHandHeight = itemInHandRenderer.oMainHandHeight;
                ItemStack mainHandItem = lastMainHandItem;
                ItemStack currentMainHandItem = minecraft.player.getMainHandItem();
                if (ItemStack.matches(mainHandItem, currentMainHandItem)) {
                    mainHandItem = currentMainHandItem;
                }
                float attackStrengthScale = minecraft.player.getAttackStrengthScale(1.0F);
                mainHandHeight += Mth.clamp((mainHandItem == currentMainHandItem ?
                                attackStrengthScale * attackStrengthScale * attackStrengthScale : 0.0F) - mainHandHeight,
                        -0.4F,
                        0.4F);
                if (mainHandHeight < 0.1F) {
                    mainHandItem = currentMainHandItem;
                }
                itemInHandRenderer.mainHandHeight = mainHandHeight;
                itemInHandRenderer.mainHandItem = mainHandItem;
            }
            if (lastOffHandItem.is(ModRegistry.HOLDABLE_WHILE_ROWING_ITEM_TAG)) {
                float offHandHeight = itemInHandRenderer.oOffHandHeight;
                ItemStack offHandItem = lastOffHandItem;
                ItemStack currentOffHandItem = minecraft.player.getOffhandItem();
                if (ItemStack.matches(offHandItem, currentOffHandItem)) {
                    offHandItem = currentOffHandItem;
                }
                offHandHeight += Mth.clamp((offHandItem == currentOffHandItem ? 1.0F : 0.0F) - offHandHeight,
                        -0.4F,
                        0.4F);
                if (offHandHeight < 0.1F) {
                    offHandItem = currentOffHandItem;
                }
                itemInHandRenderer.offHandHeight = offHandHeight;
                itemInHandRenderer.offHandItem = offHandItem;
            }
        }
    }
}
