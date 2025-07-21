package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DismountingRestrictionHandler {

    public static EventResult onEntityLoad(Entity entity, ServerLevel serverLevel, boolean isNewlySpawned) {
        if (entity instanceof PathfinderMob mob && entity.getType().is(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)) {
            mob.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(mob, 0.7));
        }

        return EventResult.PASS;
    }

    public static EventResult onStopRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (passengerEntity instanceof Player && vehicleEntity instanceof PathfinderMob mob && mob.isSaddled()
                && vehicleEntity.getType().is(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)) {
            mob.setHomeTo(vehicleEntity.blockPosition(), 3);
            mob.getNavigation().stop();
        }

        return EventResult.PASS;
    }
}
