package fuzs.vehicleupgrade.common.mixin;

import fuzs.vehicleupgrade.common.handler.VehicleUpgradeHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingParticlesLeavesBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FallingParticlesLeavesBlock.class)
abstract class FallingParticlesLeavesBlockMixin extends LeavesBlock {

    public FallingParticlesLeavesBlockMixin(AmbientLeavesBlockSoundPlayer ambientLeavesBlockSoundPlayer, Properties properties) {
        super(ambientLeavesBlockSoundPlayer, properties);
    }

    @Override
    protected void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier effectApplier, boolean intersectsPosition) {
        super.entityInside(blockState, level, blockPos, entity, effectApplier, intersectsPosition);
        if (VehicleUpgradeHandler.isRidingTraversable(blockState, entity) && level.isClientSide()) {
            boolean moved = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
            if (moved && level.getRandom().nextInt(5) == 0) {
                this.spawnFallingLeavesParticle(level, blockPos, level.getRandom());
            }
        }
    }

    @Shadow
    protected abstract void spawnFallingLeavesParticle(Level level, BlockPos blockPos, RandomSource randomSource);
}
