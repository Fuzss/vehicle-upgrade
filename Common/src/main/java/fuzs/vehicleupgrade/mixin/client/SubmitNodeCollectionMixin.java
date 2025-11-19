package fuzs.vehicleupgrade.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import fuzs.vehicleupgrade.client.handler.TranslucentMountHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;
import java.util.OptionalInt;

@Mixin(SubmitNodeCollection.class)
abstract class SubmitNodeCollectionMixin {

    @ModifyVariable(method = "submitModel", at = @At("HEAD"), argsOnly = true)
    public <S> RenderType submitModel(RenderType renderType, @Local(argsOnly = true) S renderState) {
        if (renderState instanceof EntityRenderState entityRenderState) {
            OptionalInt alpha = RenderStateExtraData.getOrDefault(entityRenderState,
                    TranslucentMountHandler.VEHICLE_ALPHA_KEY,
                    OptionalInt.empty());
            if (alpha.isPresent() && renderType instanceof RenderType.CompositeRenderType compositeRenderType) {
                if (compositeRenderType.renderPipeline.getBlendFunction().isEmpty()) {
                    Optional<ResourceLocation> cutoutTexture = compositeRenderType.state.textureState.cutoutTexture();
                    if (cutoutTexture.isPresent()) {
                        return RenderType.entityTranslucent(cutoutTexture.get());
                    }
                }
            }
        }

        return renderType;
    }

    @ModifyVariable(method = "submitModel", at = @At("HEAD"), ordinal = 2, argsOnly = true)
    public <S> int submitModel(int tintColor, @Local(argsOnly = true) S renderState) {
        if (renderState instanceof EntityRenderState entityRenderState) {
            OptionalInt alpha = RenderStateExtraData.getOrDefault(entityRenderState,
                    TranslucentMountHandler.VEHICLE_ALPHA_KEY,
                    OptionalInt.empty());
            if (alpha.isPresent()) {
                return ARGB.color(alpha.getAsInt(), tintColor);
            }
        }

        return tintColor;
    }
}
