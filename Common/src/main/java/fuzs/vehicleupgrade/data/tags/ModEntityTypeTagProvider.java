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
        this.tag(ModRegistry.HORSE_LIKE_ENTITY_TYPE_TAG)
                .add(EntityType.HORSE,
                        EntityType.DONKEY,
                        EntityType.MULE,
                        EntityType.SKELETON_HORSE,
                        EntityType.ZOMBIE_HORSE,
                        EntityType.LLAMA,
                        EntityType.TRADER_LLAMA,
                        EntityType.CAMEL);
        this.tag(ModRegistry.RESTRICTED_MOUNTS_ENTITY_TYPE_TAG)
                .add(EntityType.PIG, EntityType.STRIDER)
                .addTag(ModRegistry.HORSE_LIKE_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.TRAVERSABLE_MOUNTS_ENTITY_TYPE_TAG)
                .add(EntityType.PIG, EntityType.STRIDER)
                .addTag(ModRegistry.HORSE_LIKE_ENTITY_TYPE_TAG);
        this.tag(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG)
                .add(EntityType.WOLF, EntityType.PIG, EntityType.STRIDER, EntityType.HAPPY_GHAST);
        this.tag(ModRegistry.OVER_SIZED_BOAT_PASSENGERS_ENTITY_TYPE_TAG)
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
        this.tag(ModRegistry.CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG)
                .add(EntityType.WOLF, EntityType.LLAMA, EntityType.TRADER_LLAMA)
                .addTag(EntityTypeTags.CAN_WEAR_HORSE_ARMOR, EntityTypeTags.CAN_EQUIP_HARNESS);
    }
}
