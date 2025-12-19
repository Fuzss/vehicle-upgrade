package fuzs.vehicleupgrade.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ClientConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.OptionalInt;

public class TranslucentMountHandler {
    public static final ContextKey<OptionalInt> VEHICLE_ALPHA_KEY = new ContextKey<>(VehicleUpgrade.id("vehicle_alpha"));

    public static void onExtractRenderState(Entity entity, EntityRenderState renderState, float partialTick) {
        if (!VehicleUpgrade.CONFIG.get(ClientConfig.class).translucentMount) {
            return;
        }

        if (!entity.getType().is(ModRegistry.TRANSLUCENT_MOUNTS_ENTITY_TYPE_TAG)) {
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

    public static <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> EventResult onBeforeRenderEntity(S renderState, LivingEntityRenderer<T, S, M> entityRenderer, float partialTick, PoseStack poseStack, SubmitNodeCollector nodeCollector) {
        // not good to modify the render state during rendering,
        // but we cannot access the render state earlier with the changes made to it by inventory rendering which we depend upon for detection
        if (isRenderingInInventory(renderState)) {
            RenderStateExtraData.remove(renderState, VEHICLE_ALPHA_KEY);
        }

        return EventResult.PASS;
    }

    /**
     * @see net.minecraft.client.gui.screens.inventory.InventoryScreen#renderEntityInInventory(GuiGraphics, int, int,
     *         int, int, float, Vector3f, Quaternionf, Quaternionf, LivingEntity)
     */
    private static boolean isRenderingInInventory(EntityRenderState renderState) {
        return renderState.lightCoords == 15728880 && renderState.hitboxesRenderState == null
                && renderState.shadowPieces.isEmpty() && renderState.outlineColor == 0;
    }
}
