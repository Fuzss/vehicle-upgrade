plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

dependencies {
    modApi(libs.puzzleslib.common)
}

multiloader {
    mixins {
        mixin(
            "AbstractBoatMixin",
            "AbstractHorseMixin",
            "BlockStateBaseMixin",
            "EntityMixin",
            "LeavesBlockMixin",
            "LivingEntityMixin"
        )
        clientMixin("LocalPlayerMixin", "SubmitNodeCollectionMixin")
    }
}
