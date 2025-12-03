plugins {
    id("fuzs.multiloader.conventions-common")
}

dependencies {
    modApi(libs.puzzleslib.common)
}

multiloader {
    mixins {
        mixin("AbstractBoatMixin")
        mixin("AbstractHorseMixin")
        mixin("BlockStateBaseMixin")
        mixin("EntityMixin")
        mixin("LeavesBlockMixin")
        mixin("LivingEntityMixin")
        clientMixin("LocalPlayerMixin")
        clientMixin("SubmitNodeCollectionMixin")
    }
}
