package fuzs.vehicleupgrade.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
abstract class AbstractHorseMixin extends Animal {

    protected AbstractHorseMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "isImmobile",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;isEating()Z")
    )
    public boolean isImmobile$0(boolean isEating) {
        return false;
    }

    @ModifyExpressionValue(
            method = "isImmobile",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;isStanding()Z")
    )
    public boolean isImmobile$1(boolean isStanding) {
        return false;
    }

    @ModifyExpressionValue(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;canEatGrass()Z")
    )
    public boolean aiStep(boolean canEatGrass) {
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.92F;
    }

    @Inject(method = "handleStartJump", at = @At("HEAD"), cancellable = true)
    public void handleStartJump(int jumpPower, CallbackInfo callback) {
        if (this.isInLiquid()) {
            callback.cancel();
        }
    }
}
