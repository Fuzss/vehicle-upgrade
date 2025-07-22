package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VehicleUpgradeHandler {

    public static EventResultHolder<InteractionResult> onUseEntity(Player player, Level level, InteractionHand interactionHand, Entity entity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).manuallyDismountPassengers) {
            return EventResultHolder.pass();
        }

        if (player.isSecondaryUseActive() && player.getItemInHand(interactionHand).isEmpty()) {
            if (entity.isPassenger() && !(entity instanceof Player) && entity.getVehicle() instanceof VehicleEntity) {
                entity.stopRiding();

                if (entity instanceof Mob mob) {
                    mob.getNavigation().stop();
                }

                return EventResultHolder.interrupt(InteractionResult.SUCCESS);
            }
        }

        return EventResultHolder.pass();
    }

    public static EventResult onStartRiding(Level level, Entity rider, Entity vehicle) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).rotateVehicleWithPlayer) {
            return EventResult.PASS;
        }

        if (!vehicle.hasControllingPassenger() && rider instanceof Player player) {
            vehicle.setYRot(player.getYRot());
            vehicle.yRotO = player.getYRot();

            if (vehicle instanceof LivingEntity livingEntity) {
                livingEntity.yBodyRot = livingEntity.yBodyRotO = player.yBodyRot;
                livingEntity.yHeadRot = livingEntity.yHeadRotO = player.yHeadRot;
            }
        }

        return EventResult.PASS;
    }

    public static Optional<VoxelShape> getRidingTraversableShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return collisionContext instanceof EntityCollisionContext context && isRidingTraversable(blockState,
                context.getEntity()) ? Optional.of(Shapes.empty()) : Optional.empty();
    }

    public static boolean isRidingTraversable(BlockState blockState, @Nullable Entity entity) {
        if (VehicleUpgrade.CONFIG.getHolder(ServerConfig.class).isAvailable() && VehicleUpgrade.CONFIG.get(ServerConfig.class).mountsPassThroughLeaves) {
            return false;
        } else if (entity != null && (entity.hasControllingPassenger() || entity.isPassenger())) {
            return blockState.is(ModRegistry.RIDING_TRAVERSABLE_BLOCK_TAG);
        } else {
            return false;
        }
    }
}
