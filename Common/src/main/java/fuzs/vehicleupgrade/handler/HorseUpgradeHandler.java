package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.world.entity.ai.goal.HorseEatingGoal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class HorseUpgradeHandler {

    public static EventResult onEntityLoad(Entity entity, ServerLevel serverLevel, boolean isNewlySpawned) {
        if (entity instanceof AbstractHorse abstractHorse) {
            abstractHorse.goalSelector.addGoal(7, new HorseEatingGoal(abstractHorse));
        }

        return EventResult.PASS;
    }
}
