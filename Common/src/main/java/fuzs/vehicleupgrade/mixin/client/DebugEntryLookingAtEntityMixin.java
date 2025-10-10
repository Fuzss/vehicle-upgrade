package fuzs.vehicleupgrade.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.client.handler.EntityAttributesHandler;
import fuzs.vehicleupgrade.config.ClientConfig;
import net.minecraft.client.gui.components.debug.DebugEntryLookingAtEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugEntryLookingAtEntity.class)
abstract class DebugEntryLookingAtEntityMixin {

    @Inject(method = "display",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER),
            slice = @Slice(from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getType()Lnet/minecraft/world/entity/EntityType;")))
    public void display(CallbackInfo callback, @Local Entity entity, @Local List<String> list) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).debugEntityAttributes) {
            return;
        }

        // We do the injection via Mixin as we want to be part of the entity group without separately being toggled.
        // The lines, after all, can still be deactivated via the config option if not desired.
        list.addAll(EntityAttributesHandler.onGatherEntityAttributeLines(entity));
    }
}
