package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DismountingRestrictionHandler {

    public static EventResult onEntityLoad(Entity entity, ServerLevel serverLevel, boolean isNewlySpawned) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).saddledMountsDoNotWander) {
            return EventResult.PASS;
        }

        if (entity instanceof PathfinderMob mob && entity.getType().is(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)) {
            for (WrappedGoal wrappedGoal : mob.goalSelector.getAvailableGoals()) {
                if (wrappedGoal.getGoal() instanceof RandomStrollGoal goal) {
                    mob.goalSelector.addGoal(wrappedGoal.getPriority() - 1,
                            new MoveTowardsRestrictionGoal(mob, goal.speedModifier));
                    return EventResult.PASS;
                }
            }

            mob.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(mob, 1.0));
        }

        return EventResult.PASS;
    }

    public static EventResult onStopRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).saddledMountsDoNotWander) {
            return EventResult.PASS;
        }

        if (passengerEntity instanceof Player) {
            if (vehicleEntity instanceof PathfinderMob mob && mob.getType()
                    .is(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)) {
                setHomePosition(mob);
            }
        }

        return EventResult.PASS;
    }

    public static void onLivingEquipmentChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack oldItemStack, ItemStack newItemStack) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).saddledMountsDoNotWander) {
            return;
        }

        if (equipmentSlot == EquipmentSlot.SADDLE) {
            if (livingEntity instanceof PathfinderMob mob && livingEntity.getType()
                    .is(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)) {
                setHomePosition(mob);
            }
        }
    }

    private static void setHomePosition(Mob mob) {
        if (mob instanceof Saddleable saddleable && saddleable.isSaddled()) {
            mob.setHomeTo(mob.blockPosition(), (int) (mob.leashElasticDistance() - 1.0));
            mob.getNavigation().stop();
        } else {
            mob.clearHome();
        }
    }
}
