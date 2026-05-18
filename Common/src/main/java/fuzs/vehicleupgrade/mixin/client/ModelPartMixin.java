package fuzs.vehicleupgrade.mixin.client;

import fuzs.vehicleupgrade.client.handler.TranslucentMountHandler;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ModelPart.class)
abstract class ModelPartMixin {

    @ModifyVariable(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V",
                    at = @At("HEAD"),
                    argsOnly = true,
                    ordinal = 2)
    public final int renderToBuffer(int color) {
        return TranslucentMountHandler.getColorWithAlpha(color);
    }
}
