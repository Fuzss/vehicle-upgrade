package fuzs.vehicleupgrade.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
abstract class LocalPlayerMixin extends AbstractClientPlayer {

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @ModifyExpressionValue(method = "vehicleCanSprint",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;canSprint()Z"))
    private boolean vehicleCanSprint(boolean canSprint, Entity vehicle) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).sprintWhileRiding) {
            return canSprint;
        } else {
            return canSprint || vehicle.getType().is(ModRegistry.SPRINTING_MOUNTS_ENTITY_TYPE_TAG);
        }
    }
}
