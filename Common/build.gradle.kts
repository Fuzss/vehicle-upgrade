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
            "LivingEntityMixin",
            "MobMixin"
        )
        clientMixin("LocalPlayerMixin", "ModelPartMixin", "MultiBufferSource\u0024BufferSourceMixin")
    }
}
