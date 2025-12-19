package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.util.v1.CommonHelper;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.LeavesMountCollisions;
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

import java.util.Objects;
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

    public static EventResult onStartRiding(Level level, Entity passengerEntity, Entity vehicleEntity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).rotateVehicleWithPlayer) {
            return EventResult.PASS;
        }

        if (!vehicleEntity.hasControllingPassenger() && passengerEntity instanceof Player player) {
            vehicleEntity.setYRot(player.getYRot());
            vehicleEntity.yRotO = player.getYRot();
            if (vehicleEntity instanceof LivingEntity livingEntity) {
                livingEntity.yBodyRot = livingEntity.yBodyRotO = player.yBodyRot;
                livingEntity.yHeadRot = livingEntity.yHeadRotO = player.yHeadRot;
            }
        }

        return EventResult.PASS;
    }

    public static Optional<VoxelShape> getRidingTraversableShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (collisionContext instanceof EntityCollisionContext context && isRidingTraversable(blockState,
                context.getEntity())) {
            if (VehicleUpgrade.CONFIG.get(ServerConfig.class).mountsPassThroughLeaves == LeavesMountCollisions.ALWAYS
                    || isTraversableAtHeight(blockGetter, blockPos, context)) {
                return Optional.of(Shapes.empty());
            }
        }

        return Optional.empty();
    }

    public static boolean isRidingTraversable(BlockState blockState, @Nullable Entity entity) {
        if (CommonHelper.getMinecraftServer() == null
                || VehicleUpgrade.CONFIG.get(ServerConfig.class).mountsPassThroughLeaves
                == LeavesMountCollisions.VANILLA) {
            return false;
        }

        if (blockState.is(ModRegistry.RIDING_TRAVERSABLE_BLOCK_TAG)) {
            return getTraversableEntity(entity) != null;
        } else {
            return false;
        }
    }

    private static boolean isTraversableAtHeight(BlockGetter blockGetter, BlockPos blockPos, EntityCollisionContext context) {
        Entity vehicleEntity = getTraversableEntity(context.getEntity());
        if (vehicleEntity == null) {
            return false;
        } else if (blockPos.getY() <= vehicleEntity.getBlockY()) {
            return false;
        } else if (blockPos.getY() > vehicleEntity.getBlockY() + 1) {
            return true;
        } else {
            // investigate the block above feet more closely as most mounts will try to step up
            BlockPos bottomPosition = blockPos.atY(vehicleEntity.getBlockY());
            if (Objects.equals(blockPos, bottomPosition)) {
                return false;
            } else {
                return blockGetter.getBlockState(bottomPosition)
                        .getCollisionShape(blockGetter, bottomPosition, context)
                        .isEmpty();
            }
        }
    }

    private static @Nullable Entity getTraversableEntity(@Nullable Entity entity) {
        if (entity != null) {
            if (entity.hasControllingPassenger() && entity.getType()
                    .is(ModRegistry.TRAVERSABLE_MOUNTS_ENTITY_TYPE_TAG)) {
                return entity;
            } else if (entity.isPassenger() && entity.getVehicle()
                    .getType()
                    .is(ModRegistry.TRAVERSABLE_MOUNTS_ENTITY_TYPE_TAG)) {
                return entity.getVehicle();
            }
        }

        return null;
    }
}
