package fuzs.vehicleupgrade.mixin.client;

import fuzs.vehicleupgrade.client.handler.TranslucentMountHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MultiBufferSource.BufferSource.class)
abstract class MultiBufferSource$BufferSourceMixin {

    @ModifyVariable(method = "getBuffer", at = @At("HEAD"), argsOnly = true)
    public RenderType getBuffer(RenderType renderType) {
        return TranslucentMountHandler.getTranslucentRenderType(renderType);
    }
}
