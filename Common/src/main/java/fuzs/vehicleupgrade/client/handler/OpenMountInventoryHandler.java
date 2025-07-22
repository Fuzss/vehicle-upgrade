package fuzs.vehicleupgrade.client.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.network.client.ServerboundOpenServerControlledInventoryMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class OpenMountInventoryHandler {
    private static int inventoryTriggerTime;

    public static EventResultHolder<InteractionResult> onUseEntity(Player player, Level level, InteractionHand interactionHand, Entity entity) {
        if (player.isSecondaryUseActive()) {
            if (entity instanceof HasCustomInventoryScreen hasCustomInventoryScreen && (
                    !(entity instanceof AbstractHorse abstractHorse) || abstractHorse.isTamed()) && (
                    !(entity instanceof TamableAnimal tamableAnimal) || tamableAnimal.isTame())) {
                if (!level.isClientSide) {
                    hasCustomInventoryScreen.openCustomInventoryScreen(player);
                }
                return EventResultHolder.interrupt(InteractionResult.SUCCESS);
            } else if (entity.getType().is(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG)
                    && entity instanceof Mob mob && (mob.isSaddled() || mob.isWearingBodyArmor()) && (
                    !(entity instanceof TamableAnimal tamableAnimal) || tamableAnimal.isTame())) {
                if (!level.isClientSide) {
                    ServerboundOpenServerControlledInventoryMessage.openCustomInventoryScreen((ServerPlayer) player,
                            mob);
                }
                return EventResultHolder.interrupt(InteractionResultHelper.SUCCESS);
            }
        }
        return EventResultHolder.pass();
    }

    public static void onStartClientTick(Minecraft minecraft) {
        if (inventoryTriggerTime > 0) {
            inventoryTriggerTime--;
        }
        if (minecraft.gameMode != null && isServerControlledInventory(minecraft.player)) {
            while (minecraft.options.keyInventory.consumeClick()) {
                if (inventoryTriggerTime == 0) {
                    inventoryTriggerTime = 7;
                    Entity playerVehicle = minecraft.player.getVehicle();
                    MessageSender.broadcast(new ServerboundOpenServerControlledInventoryMessage(playerVehicle.getId()));
                } else {
                    minecraft.getTutorial().onOpenInventory();
                    minecraft.setScreen(new InventoryScreen(minecraft.player));
                }
            }
        }
    }

    public static EventResult onBeforeKeyPress(Screen screen, int keyCode, int scanCode, int modifiers) {
        if (inventoryTriggerTime > 0 && screen.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            screen.minecraft.getTutorial().onOpenInventory();
            screen.minecraft.setScreen(new InventoryScreen(screen.minecraft.player));
            return EventResult.INTERRUPT;
        } else {
            return EventResult.PASS;
        }
    }

    public static boolean isServerControlledInventory(@Nullable LocalPlayer player) {
        if (player != null && player.isPassenger()) {
            return player.getVehicle() instanceof HasCustomInventoryScreen || player.getVehicle()
                    .getType()
                    .is(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG);
        } else {
            return false;
        }
    }

    public static void sendOpenInventory(@Nullable LocalPlayer player) {
        if (player != null && player.isPassenger()) {
            Entity playerVehicle = player.getVehicle();
            if (playerVehicle instanceof HasCustomInventoryScreen) {
                player.sendOpenInventory();
            } else if (playerVehicle.getType().is(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG)) {
                MessageSender.broadcast(new ServerboundOpenServerControlledInventoryMessage(playerVehicle.getId()));
            }
        }
    }
}
