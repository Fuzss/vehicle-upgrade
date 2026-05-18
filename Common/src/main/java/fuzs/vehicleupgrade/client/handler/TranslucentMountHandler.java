package fuzs.vehicleupgrade.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.util.v1.ARGB;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Optional;

public class TranslucentMountHandler {
    private static Optional<Float> vehicleAlpha = Optional.empty();

    public static <T extends LivingEntity, M extends EntityModel<T>> EventResult onBeforeRenderEntity(T entity, LivingEntityRenderer<T, M> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).translucentMount) {
            return EventResult.PASS;
        }

        if (isRenderingInInventory(packedLight)) {
            return EventResult.PASS;
        }

        if (Minecraft.getInstance().screen != null || !entity.getType()
                .is(ModRegistry.TRANSLUCENT_MOUNTS_ENTITY_TYPE_TAG)) {
            return EventResult.PASS;
        }

        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity != null && entity.isVehicle() && entity.hasPassenger(cameraEntity)) {
            float alphaValue = Mth.clamp((cameraEntity.getViewXRot(partialTick) - 15.0F) / 45.0F, 0.0F, 1.0F);
            vehicleAlpha = Optional.of(1.0F - 0.9F * alphaValue);
        }

        return EventResult.PASS;
    }

    public static <T extends LivingEntity, M extends EntityModel<T>> void onAfterRenderEntity(T entity, LivingEntityRenderer<T, M> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
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
            if (compositeRenderType.state.transparencyState == RenderStateShard.NO_TRANSPARENCY) {
                Optional<ResourceLocation> cutoutTexture = compositeRenderType.state.textureState.cutoutTexture();
                if (cutoutTexture.isPresent()) {
                    return RenderType.entityTranslucent(cutoutTexture.get());
                }
            }
        }

        return renderType;
    }

    /**
     * @see net.minecraft.client.gui.screens.inventory.InventoryScreen#renderEntityInInventory(GuiGraphics, float,
     *         float, float, Vector3f, Quaternionf, Quaternionf, LivingEntity)
     */
    private static boolean isRenderingInInventory(int packedLight) {
        return packedLight == 15728880;
    }
}
