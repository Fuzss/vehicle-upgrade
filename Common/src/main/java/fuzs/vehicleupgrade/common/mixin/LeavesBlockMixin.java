package fuzs.vehicleupgrade.common.mixin;

import fuzs.vehicleupgrade.common.handler.VehicleUpgradeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeavesBlock.class)
abstract class LeavesBlockMixin extends Block {

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    protected void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier effectApplier, boolean intersectsPosition) {
        if (VehicleUpgradeHandler.isRidingTraversable(blockState, entity)) {
            entity.makeStuckInBlock(blockState, new Vec3(0.9, 1.5, 0.9));
        } else {
            super.entityInside(blockState, level, blockPos, entity, effectApplier, intersectsPosition);
        }
    }
}
