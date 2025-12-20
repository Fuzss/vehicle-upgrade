package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.world.inventory.MountInventoryMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

public class MountInventoryHandler {

    public static EventResultHolder<InteractionResult> onUseEntity(Player player, Level level, InteractionHand interactionHand, Entity entity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).openMobInventoryByInteracting) {
            return EventResultHolder.pass();
        }

        if (player.isSecondaryUseActive() && hasInventoryScreen(entity, player)) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (canBeLeashed(entity, player)) {
                    return EventResultHolder.pass();
                } else {
                    openInventoryScreen(entity, serverPlayer);
                }
            }

            return EventResultHolder.interrupt(InteractionResult.SUCCESS);
        } else {
            return EventResultHolder.pass();
        }
    }

    private static boolean canBeLeashed(Entity entity, Player player) {
        if (entity instanceof Leashable leashable && leashable.canBeLeashed() && entity.isAlive() && !(
                entity instanceof LivingEntity livingEntity && livingEntity.isBaby())) {
            List<Leashable> list = Leashable.leashableInArea(entity,
                    (Leashable leashableX) -> leashableX.getLeashHolder() == player);
            for (Leashable currentLeashable : list) {
                if (currentLeashable.canHaveALeashAttachedTo(entity)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasInventoryScreen(Entity vehicleEntity, Player player) {
        if (vehicleEntity instanceof AbstractHorse abstractHorse && !abstractHorse.isTamed()) {
            return false;
        } else if (vehicleEntity instanceof TamableAnimal tamableAnimal && !tamableAnimal.isTame()) {
            return false;
        } else if (false && vehicleEntity instanceof OwnableEntity ownableEntity
                && ownableEntity.getOwner() != player) {
            // disabled for now, vanilla only enforces this for wolves, but not horses, etc.
            return false;
        } else if (!canShearEquipment(player, player) && !vehicleEntity.hasPassenger(player)) {
            return false;
        } else if (vehicleEntity instanceof HasCustomInventoryScreen) {
            return true;
        } else {
            return hasEquipmentScreen(vehicleEntity);
        }
    }

    private static boolean canShearEquipment(Entity entity, Player player) {
        if (entity instanceof Mob mob) {
            return mob.canShearEquipment(player);
        } else {
            return !entity.isVehicle();
        }
    }

    private static boolean hasEquipmentScreen(Entity vehicleEntity) {
        return vehicleEntity instanceof Mob mob && mob.getType().is(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG)
                && (mob.isSaddled() || mob.isWearingBodyArmor());
    }

    public static void openInventoryScreen(Entity vehicleEntity, ServerPlayer serverPlayer) {
        if (vehicleEntity instanceof HasCustomInventoryScreen hasCustomInventoryScreen) {
            hasCustomInventoryScreen.openCustomInventoryScreen(serverPlayer);
        } else if (vehicleEntity instanceof Mob mob && vehicleEntity.getType()
                .is(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG)) {
            ContainerMenuHelper.openMenu(serverPlayer,
                    new SimpleMenuProvider((int containerId, Inventory inventory, Player player) -> {
                        return new MountInventoryMenu(containerId, inventory, mob);
                    }, mob.getDisplayName()),
                    mob.getId());
        }
    }
}
