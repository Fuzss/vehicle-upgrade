package fuzs.vehicleupgrade.mixin;

import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MixinConfigPluginImpl implements IMixinConfigPlugin {
    private final Set<String> removedMixins = new HashSet<>();

    @Override
    public void onLoad(String mixinPackage) {
        this.removeMixinsIf("moonrise", "EntityMixin");
    }

    private void removeMixinsIf(String modId, String... mixins) {
        if (ModLoaderEnvironment.INSTANCE.isModLoaded(modId)) {
            this.removedMixins.addAll(Arrays.asList(mixins));
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return !this.removedMixins.contains(mixinClassName.replaceAll(".+\\.mixin\\.", ""));
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // NO-OP
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // NO-OP
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // NO-OP
    }
}
