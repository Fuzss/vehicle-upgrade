package fuzs.vehicleupgrade.mixin;

import fuzs.puzzleslib.api.util.v1.CompoundTagHelper;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
abstract class MobMixin extends LivingEntity {
    @Shadow
    private BlockPos restrictCenter;
    @Shadow
    private float restrictRadius;

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag compound, CallbackInfo callback) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).saddledMountsDoNotWander) {
            return;
        }

        if (this.hasRestriction()) {
            compound.putFloat(VehicleUpgrade.id("home_radius").toString(), this.restrictRadius);
            RegistryOps<Tag> registryOps = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
            CompoundTagHelper.store(compound,
                    VehicleUpgrade.id("home_pos").toString(),
                    BlockPos.CODEC,
                    registryOps,
                    this.restrictCenter);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag compound, CallbackInfo callback) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).saddledMountsDoNotWander) {
            return;
        }

        this.restrictRadius = compound.contains(VehicleUpgrade.id("home_radius").toString(), Tag.TAG_FLOAT) ?
                compound.getFloat(VehicleUpgrade.id("home_radius").toString()) : -1.0F;
        if (this.restrictRadius >= 0.0F) {
            RegistryOps<Tag> registryOps = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
            this.restrictCenter = CompoundTagHelper.read(compound,
                    VehicleUpgrade.id("home_pos").toString(),
                    BlockPos.CODEC,
                    registryOps).orElse(BlockPos.ZERO);
        }
    }

    @Shadow
    public abstract boolean hasRestriction();
}
