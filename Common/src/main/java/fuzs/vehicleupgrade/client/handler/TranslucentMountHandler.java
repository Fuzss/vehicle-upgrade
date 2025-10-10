package fuzs.vehicleupgrade.client.handler;

import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;

import java.util.OptionalInt;

public class TranslucentMountHandler {
    public static final ContextKey<OptionalInt> VEHICLE_ALPHA_KEY = new ContextKey<>(VehicleUpgrade.id("vehicle_alpha"));

    public static void onExtractRenderState(Entity entity, EntityRenderState renderState, float partialTick) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).translucentMount) {
            return;
        }

        if (Minecraft.getInstance().screen != null || !entity.getType()
                .is(ModRegistry.TRANSLUCENT_MOUNTS_ENTITY_TYPE_TAG)) {
            return;
        }

        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity != null && entity.isVehicle() && entity.hasPassenger(cameraEntity)) {
            float alpha = Mth.clamp((cameraEntity.getXRot(partialTick) - 15.0F) / 45.0F, 0.0F, 1.0F);
            RenderStateExtraData.set(renderState,
                    VEHICLE_ALPHA_KEY,
                    OptionalInt.of(ARGB.as8BitChannel(1.0F - 0.9F * alpha)));
        }
    }
}
