import fuzs.multiloader.extension.packageName

plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

dependencies {
    modApi(sharedLibs.puzzleslib.common)
}

multiloader {
    mixins {
        plugin.set("${project.group}.${project.packageName}.mixin.MixinConfigPluginImpl")
        mixin(
            "AbstractBoatMixin",
            "AbstractHorseMixin",
            "BlockStateBaseMixin",
            "EntityMixin",
            "LeavesBlockMixin",
            "LivingEntityMixin"
        )
        clientMixin("AbstractMountInventoryScreenMixin", "LocalPlayerMixin", "SubmitNodeCollectionMixin")
    }
}
