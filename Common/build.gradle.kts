plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

dependencies {
    modApi(libs.puzzleslib.common)
}

multiloader {
    mixins {
        plugin.set("${project.group}.mixin.MixinConfigPluginImpl")
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
