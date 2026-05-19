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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class DismountingRestrictionHandler {

    public static EventResult onEntityJoin(Entity entity, ServerLevel serverLevel, boolean isLoadedFromDisk, @Nullable MobSpawnType spawnReason) {
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
        if (passengerEntity instanceof Player) {
            if (vehicleEntity instanceof PathfinderMob mob && mob.getType()
                    .is(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)) {
                setHomePosition(mob);
            }
        }

        return EventResult.PASS;
    }

//    public static void onLivingEquipmentChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack oldItemStack, ItemStack newItemStack) {
//        if (equipmentSlot == EquipmentSlot.SADDLE) {
//            if (livingEntity instanceof PathfinderMob mob && livingEntity.getType()
//                    .is(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)) {
//                setHomePosition(mob);
//            }
//        }
//    }

    /**
     * This must also be saved on the entity which it is not implement in vanilla 1.21.1.
     *
     * @see PathfinderMob#handleLeashAtDistance(Entity, float)
     */
    private static void setHomePosition(Mob mob) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).saddledMountsDoNotWander) {
            // Make sure this is cleared when the config option is disabled after being active previously.
            mob.clearRestriction();
            return;
        }

        if (mob instanceof Saddleable saddleable && saddleable.isSaddled()) {
            mob.restrictTo(mob.blockPosition(), 5);
            mob.getNavigation().stop();
        } else {
            mob.clearRestriction();
        }
    }
}
