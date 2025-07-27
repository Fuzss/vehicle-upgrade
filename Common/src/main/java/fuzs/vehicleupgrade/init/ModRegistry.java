package fuzs.vehicleupgrade.init;

import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.world.inventory.EquipmentInventoryMenu;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(VehicleUpgrade.MOD_ID);
    public static final Holder.Reference<Attribute> AIRBORNE_MINING_SPEED_ATTRIBUTE = REGISTRIES.register(Registries.ATTRIBUTE,
            "airborne_mining_speed",
            () -> new RangedAttribute(Util.makeDescriptionId(Registries.elementsDirPath(Registries.ATTRIBUTE),
                    REGISTRIES.makeKey("airborne_mining_speed")), 0.2, 0.0, 20.0).setSyncable(true));
    public static final Holder.Reference<MenuType<EquipmentInventoryMenu>> EQUIPMENT_USER_MENU_TYPE = REGISTRIES.registerMenuType(
            "equipment_user",
            EquipmentInventoryMenu::new,
            ByteBufCodecs.INT);

    static final TagFactory TAGS = TagFactory.make(VehicleUpgrade.MOD_ID);
    public static final TagKey<Block> RIDING_TRAVERSABLE_BLOCK_TAG = TAGS.registerBlockTag("riding_traversable");
    public static final TagKey<Item> HOLDABLE_WHILE_ROWING_ITEM_TAG = TAGS.registerItemTag("holdable_while_rowing");
    public static final TagKey<EntityType<?>> HORSE_LIKE_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag("horse_like");
    public static final TagKey<EntityType<?>> RESTRICTED_MOUNTS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "restricted_mounts");
    public static final TagKey<EntityType<?>> TRAVERSABLE_MOUNTS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "traversable_mounts");
    public static final TagKey<EntityType<?>> TRANSLUCENT_MOUNTS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "translucent_mounts");
    public static final TagKey<EntityType<?>> CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "custom_equipment_user");
    public static final TagKey<EntityType<?>> OVER_SIZED_BOAT_PASSENGERS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "over_sized_boat_passengers");
    public static final TagKey<EntityType<?>> CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "can_equip_body_item");
    public static final TagKey<Attribute> DEBUG_ATTRIBUTES_ATTRIBUTE_TAG = TAGS.registerTagKey(Registries.ATTRIBUTE,
            "debug_attributes");
    public static final TagKey<Item> SHEAR_TOOLS_ITEM_TAG = TagFactory.COMMON.registerItemTag("tools/shear");

    public static void bootstrap() {
        // NO-OP
    }
}
