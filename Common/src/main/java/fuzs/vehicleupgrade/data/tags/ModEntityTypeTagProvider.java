package fuzs.vehicleupgrade.data.tags;

import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTagProvider extends AbstractTagProvider<EntityType<?>> {

    public ModEntityTypeTagProvider(DataProviderContext context) {
        super(Registries.ENTITY_TYPE, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.add(ModRegistry.HORSE_LIKE_ENTITY_TYPE_TAG)
                .add(EntityType.CAMEL,
                        EntityType.DONKEY,
                        EntityType.HORSE,
                        EntityType.LLAMA,
                        EntityType.MULE,
                        EntityType.SKELETON_HORSE,
                        EntityType.TRADER_LLAMA,
                        EntityType.ZOMBIE_HORSE);
        this.add(ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG).add(EntityType.PIG, EntityType.STRIDER);
        this.add(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)
                .addTag(ModRegistry.HORSE_LIKE_ENTITY_TYPE_TAG, ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.add(ModRegistry.TRAVERSABLE_MOUNTS_ENTITY_TYPE_TAG)
                .addTag(ModRegistry.HORSE_LIKE_ENTITY_TYPE_TAG, ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.add(ModRegistry.TRANSLUCENT_MOUNTS_ENTITY_TYPE_TAG)
                .add(EntityType.HAPPY_GHAST)
                .addTag(ModRegistry.HORSE_LIKE_ENTITY_TYPE_TAG, ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.add(ModRegistry.SPRINTING_MOUNTS_ENTITY_TYPE_TAG)
                .add(EntityType.HAPPY_GHAST)
                .addTag(ModRegistry.HORSE_LIKE_ENTITY_TYPE_TAG, ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.add(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG)
                .add(EntityType.WOLF, EntityType.HAPPY_GHAST)
                .addTag(ModRegistry.ITEM_STEERABLE_ENTITY_TYPE_TAG);
        this.add(ModRegistry.OVER_SIZED_BOAT_PASSENGERS_ENTITY_TYPE_TAG)
                .add(EntityType.CAMEL,
                        EntityType.DONKEY,
                        EntityType.HOGLIN,
                        EntityType.HORSE,
                        EntityType.MULE,
                        EntityType.PANDA,
                        EntityType.POLAR_BEAR,
                        EntityType.SKELETON_HORSE,
                        EntityType.SPIDER,
                        EntityType.TURTLE,
                        EntityType.ZOGLIN,
                        EntityType.ZOMBIE_HORSE);
        this.add(ModRegistry.CAN_WEAR_WOLF_ARMOR_ENTITY_TYPE_TAG).add(EntityType.WOLF);
        this.add(ModRegistry.CAN_EQUIP_CARPET_ENTITY_TYPE_TAG).add(EntityType.LLAMA, EntityType.TRADER_LLAMA);
        this.add(ModRegistry.CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG)
                .addTag(EntityTypeTags.CAN_WEAR_HORSE_ARMOR,
                        EntityTypeTags.CAN_EQUIP_HARNESS,
                        ModRegistry.CAN_WEAR_WOLF_ARMOR_ENTITY_TYPE_TAG,
                        ModRegistry.CAN_EQUIP_CARPET_ENTITY_TYPE_TAG);
    }
}
