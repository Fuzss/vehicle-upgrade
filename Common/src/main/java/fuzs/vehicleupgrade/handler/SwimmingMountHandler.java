package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class SwimmingMountHandler {

    public static EventResult onStartEntityTick(Entity entity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).mountsSwimInWater) {
            return EventResult.PASS;
        }

        if (entity instanceof Mob mob && entity.dismountsUnderwater()) {
            if (entity.isVehicle() && mob.isSaddled()) {
                if (entity.isInWater() && entity.getFluidHeight(FluidTags.WATER) > entity.getFluidJumpThreshold()
                        || entity.isInLava()) {
                    if (entity.getRandom().nextFloat() < 0.8F) {
                        // we cannot use LivingEntity::jumpInLiquid, as it only works for mobs that can float, which is only set on the server
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.04F, 0.0));

                        // prevents the horse from jumping right when entering land again if the jump key was pressed while in the water
                        if (entity instanceof AbstractHorse abstractHorse) {
                            abstractHorse.playerJumpPendingScale = 0.0F;
                        }
                    }
                }
            }
        }

        return EventResult.PASS;
    }

    public static EventResult onStopRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).mountsSwimInWater) {
            return EventResult.PASS;
        }

        if (level instanceof ServerLevel serverLevel && vehicleEntity.dismountsUnderwater()) {
            if (passengerEntity.isEyeInFluid(FluidTags.WATER) && !serverLevel.getBlockState(BlockPos.containing(
                    passengerEntity.getX(),
                    passengerEntity.getEyeY(),
                    passengerEntity.getZ())).is(Blocks.BUBBLE_COLUMN)) {
                return EventResult.INTERRUPT;
            }
        }

        return EventResult.PASS;
    }
}
