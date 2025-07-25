package fuzs.vehicleupgrade.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.client.renderer.v1.RenderPropertyKey;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class TranslucentMountHandler {
    private static final RenderPropertyKey<Optional<Float>> VEHICLE_ALPHA_RENDER_PROPERTY = new RenderPropertyKey<>(
            VehicleUpgrade.id("vehicle_alpha"));

    private static Optional<Float> vehicleAlpha = Optional.empty();

    public static void onExtractRenderState(Entity entity, EntityRenderState renderState, float partialTick) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).translucentMount) {
            return;
        }

        if (Minecraft.getInstance().screen != null) {
            return;
        }

        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity != null && entity.isVehicle() && entity.hasPassenger(cameraEntity)) {
            float alphaValue = Mth.clamp((cameraEntity.getXRot(partialTick) - 15.0F) / 45.0F, 0.0F, 1.0F);
            RenderPropertyKey.set(renderState, VEHICLE_ALPHA_RENDER_PROPERTY, Optional.of(1.0F - 0.9F * alphaValue));
        }
    }

    public static <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> EventResult onBeforeRenderEntity(S entityRenderState, LivingEntityRenderer<T, S, M> entityRenderer, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (RenderPropertyKey.has(entityRenderState, VEHICLE_ALPHA_RENDER_PROPERTY)) {
            vehicleAlpha = RenderPropertyKey.getOrDefault(entityRenderState,
                    VEHICLE_ALPHA_RENDER_PROPERTY,
                    Optional.empty());
        }

        return EventResult.PASS;
    }

    public static <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> void onAfterRenderEntity(S entityRenderState, LivingEntityRenderer<T, S, M> entityRenderer, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        vehicleAlpha = Optional.empty();
    }

    @SuppressWarnings("OptionalIsPresent")
    public static int getColorWithAlpha(int color) {
        // avoid boxing
        if (vehicleAlpha.isPresent()) {
            return ARGB.color(vehicleAlpha.get(), color);
        } else {
            return color;
        }
    }

    public static RenderType getTranslucentRenderType(RenderType renderType) {
        if (vehicleAlpha.isPresent() && renderType instanceof RenderType.CompositeRenderType compositeRenderType) {
            if (compositeRenderType.renderPipeline.getBlendFunction().isEmpty()) {
                Optional<ResourceLocation> cutoutedTexture = compositeRenderType.state.textureState.cutoutTexture();
                if (cutoutedTexture.isPresent()) {
                    return RenderType.entityTranslucent(cutoutedTexture.get());
                }
            }
        }

        return renderType;
    }
}
