package fuzs.vehicleupgrade.mixin.client;

import fuzs.vehicleupgrade.client.handler.TranslucentMountHandler;
import net.minecraft.client.model.Model;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Model.class)
abstract class ModelMixin {

    @ModifyVariable(method = "renderToBuffer", at = @At("HEAD"), argsOnly = true, ordinal = 2)
    public final int renderToBuffer(int color) {
        return TranslucentMountHandler.getColorWithAlpha(color);
    }
}
