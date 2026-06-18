package fuzs.vehicleupgrade.common.data.tags;

import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagProvider;
import fuzs.vehicleupgrade.common.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

public class ModEntityTypeTagsProvider extends AbstractTagProvider<EntityType<?>> {

    public ModEntityTypeTagsProvider(DataProviderContext context) {
        super(Registries.ENTITY_TYPE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.CAN_FLOAT_WHILE_RIDDEN)
                .add(EntityTypeIds.LLAMA, EntityTypeIds.PIG, EntityTypeIds.TRADER_LLAMA);
        this.tag(EntityTypeTags.DISMOUNTS_UNDERWATER).removeTag(EntityTypeTags.CAN_FLOAT_WHILE_RIDDEN);
        this.tag(ModRegistry.EQUINE_ENTITY_TYPE_TAG)
                .add(EntityTypeIds.CAMEL,
                        EntityTypeIds.CAMEL_HUSK,
                        EntityTypeIds.DONKEY,
                        EntityTypeIds.HORSE,
                        EntityTypeIds.LLAMA,
                        EntityTypeIds.MULE,
                        EntityTypeIds.SKELETON_HORSE,
                        EntityTypeIds.TRADER_LLAMA,
                        EntityTypeIds.ZOMBIE_HORSE);
        this.tag(ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG).add(EntityTypeIds.PIG, EntityTypeIds.STRIDER);
        // Nautilus is not added to this, there is already some custom restriction handling in vanilla (AbstractNautilus::checkRestriction).
        this.tag(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)
                .addTag(ModRegistry.EQUINE_ENTITY_TYPE_TAG, ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.TRAVERSING_MOUNTS_ENTITY_TYPE_TAG)
                .addTag(ModRegistry.EQUINE_ENTITY_TYPE_TAG, ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.TRANSLUCENT_MOUNTS_ENTITY_TYPE_TAG)
                .add(EntityTypeIds.HAPPY_GHAST, EntityTypeIds.NAUTILUS, EntityTypeIds.ZOMBIE_NAUTILUS)
                .addTag(ModRegistry.EQUINE_ENTITY_TYPE_TAG, ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.SPRINTING_MOUNTS_ENTITY_TYPE_TAG)
                .add(EntityTypeIds.HAPPY_GHAST, EntityTypeIds.NAUTILUS, EntityTypeIds.ZOMBIE_NAUTILUS)
                .addTag(ModRegistry.EQUINE_ENTITY_TYPE_TAG, ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG)
                .add(EntityTypeIds.WOLF, EntityTypeIds.HAPPY_GHAST)
                .addTag(ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.OVER_SIZED_BOAT_PASSENGERS_ENTITY_TYPE_TAG)
                .add(EntityTypeIds.CAMEL,
                        EntityTypeIds.CAMEL_HUSK,
                        EntityTypeIds.DONKEY,
                        EntityTypeIds.HOGLIN,
                        EntityTypeIds.HORSE,
                        EntityTypeIds.MULE,
                        EntityTypeIds.PANDA,
                        EntityTypeIds.POLAR_BEAR,
                        EntityTypeIds.SKELETON_HORSE,
                        EntityTypeIds.SPIDER,
                        EntityTypeIds.TURTLE,
                        EntityTypeIds.ZOGLIN,
                        EntityTypeIds.ZOMBIE_HORSE);
        this.tag(ModRegistry.CAN_WEAR_WOLF_ARMOR_ENTITY_TYPE_TAG).add(EntityTypeIds.WOLF);
        this.tag(ModRegistry.CAN_EQUIP_CARPET_ENTITY_TYPE_TAG).add(EntityTypeIds.LLAMA, EntityTypeIds.TRADER_LLAMA);
        this.tag(ModRegistry.CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG)
                .addTag(EntityTypeTags.CAN_WEAR_HORSE_ARMOR,
                        EntityTypeTags.CAN_EQUIP_HARNESS,
                        ModRegistry.CAN_WEAR_WOLF_ARMOR_ENTITY_TYPE_TAG,
                        ModRegistry.CAN_EQUIP_CARPET_ENTITY_TYPE_TAG);
    }
}
