package fuzs.vehicleupgrade.mixin;

import fuzs.vehicleupgrade.handler.VehicleUpgradeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
abstract class LeavesBlockMixin extends Block {

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "animateTick", at = @At("TAIL"))
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random, CallbackInfo callback) {
        if (random.nextInt(VehicleUpgradeHandler.getLeafParticleChanceFromWeather(level)) == 0) {
            BlockState blockStateBelow = level.getBlockState(blockPos.below());
            if (!isFaceFull(blockStateBelow.getCollisionShape(level, blockPos.below()), Direction.UP)) {
                this.spawnFallingLeavesParticle(level, blockPos, random);
            }
        }
    }

    @Shadow
    protected abstract void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random);

    @Override
    protected void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier effectApplier) {
        if (VehicleUpgradeHandler.isRidingTraversable(blockState, entity)) {
            entity.makeStuckInBlock(blockState, new Vec3(0.9, 1.5, 0.9));
            if (level.isClientSide) {
                boolean moved = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
                if (moved && level.getRandom().nextInt(2) == 0) {
                    this.spawnFallingLeavesParticle(level, blockPos, level.getRandom());
                }
            }
        } else {
            super.entityInside(blockState, level, blockPos, entity, effectApplier);
        }
    }
}
