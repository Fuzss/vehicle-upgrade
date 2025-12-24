package fuzs.vehicleupgrade.init;

import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.world.inventory.MountInventoryMenu;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
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
    public static final Holder.Reference<MenuType<MountInventoryMenu>> EQUIPMENT_USER_MENU_TYPE = REGISTRIES.registerMenuType(
            "equipment_user",
            MountInventoryMenu::new,
            ByteBufCodecs.INT);

    static final TagFactory TAGS = TagFactory.make(VehicleUpgrade.MOD_ID);
    /**
     * Blocks that can be passed through by mobs defined in {@link #TRAVERSABLE_MOUNTS_ENTITY_TYPE_TAG} while they are
     * ridden.
     *
     * @see fuzs.vehicleupgrade.config.ServerConfig#mountsPassThroughLeaves
     */
    public static final TagKey<Block> RIDING_TRAVERSABLE_BLOCK_TAG = TAGS.registerBlockTag("riding_traversable");
    /**
     * Items that can still be seen held in a hand while rowing in a boat.
     *
     * @see fuzs.vehicleupgrade.config.ClientConfig#holdItemsWhileRowing
     */
    public static final TagKey<Item> HOLDABLE_WHILE_ROWING_ITEM_TAG = TAGS.registerItemTag("holdable_while_rowing");
    /**
     * Mobs that are implemented similarly to horses (via extending
     * {@link net.minecraft.world.entity.animal.equine.AbstractHorse}).
     */
    public static final TagKey<EntityType<?>> HORSE_LIKE_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag("horse_like");
    /**
     * Mobs that are controlled holding a specific item (like carrot on a stick) when riding.
     */
    public static final TagKey<EntityType<?>> ITEM_STEERABLE_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "item_steerable");
    /**
     * Mobs that are required to stay at a given location after a riding player dismounts. The maximum distance from the
     * dismount location is provided via {@link Leashable#leashElasticDistance()}.
     * <p>
     * They also have an additional {@link net.minecraft.world.entity.ai.goal.Goal} added for returning to the dismount
     * location if forcefully removed.
     *
     * @see fuzs.vehicleupgrade.config.ServerConfig#saddledMountsDoNotWander
     */
    public static final TagKey<EntityType<?>> RESTRICTED_MOUNTS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "restricted_mounts");
    /**
     * Mobs that can pass through blocks defined in {@link #RIDING_TRAVERSABLE_BLOCK_TAG} when ridden by a player.
     *
     * @see fuzs.vehicleupgrade.config.ServerConfig#mountsPassThroughLeaves
     */
    public static final TagKey<EntityType<?>> TRAVERSABLE_MOUNTS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "traversable_mounts");
    /**
     * Mobs that turn translucent (i.e. begin to fade) when a riding player looks down on them.
     *
     * @see fuzs.vehicleupgrade.config.ClientConfig#translucentMount
     */
    public static final TagKey<EntityType<?>> TRANSLUCENT_MOUNTS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "translucent_mounts");
    /**
     * Mobs that can be made to sprint by a riding player, just like the player normally would when on foot.
     * <p>
     * In vanilla, this behavior is only enabled for {@link net.minecraft.world.entity.animal.equine.Llama llamas}.
     *
     * @see fuzs.vehicleupgrade.config.ServerConfig#sprintWhileRiding
     */
    public static final TagKey<EntityType<?>> SPRINTING_MOUNTS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "sprinting_mounts");
    /**
     * Mobs that have an inventory screen added for changing equipment like saddle or armor, similar to horses.
     *
     * @see fuzs.vehicleupgrade.config.ServerConfig#openMobInventoryByInteracting
     */
    public static final TagKey<EntityType<?>> CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "custom_equipment_user");
    /**
     * Mobs that will be able to fit onto boats despite having a larger hitbox than the boat itself. As a workaround the
     * mob hitbox is scaled down while the mob is a passenger in the boat.
     *
     * @see fuzs.vehicleupgrade.config.ServerConfig#shrinkOverSizedBoatPassengers
     */
    public static final TagKey<EntityType<?>> OVER_SIZED_BOAT_PASSENGERS_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "over_sized_boat_passengers");
    /**
     * Mobs that can equip {@link net.minecraft.world.item.Items#WOLF_ARMOR} in the
     * {@link net.minecraft.world.entity.EquipmentSlot#BODY} slot.
     * <p>
     * Used for picking the body slot icon in the mob inventory screen.
     */
    public static final TagKey<EntityType<?>> CAN_WEAR_WOLF_ARMOR_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "can_wear_wolf_armor");
    /**
     * Mobs that can equip {@link net.minecraft.tags.ItemTags#WOOL_CARPETS} in the
     * {@link net.minecraft.world.entity.EquipmentSlot#BODY} slot.
     * <p>
     * Used for picking the body slot icon in the mob inventory screen.
     */
    public static final TagKey<EntityType<?>> CAN_EQUIP_CARPET_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "can_equip_carpet");
    /**
     * Mobs that can equip an item in the {@link net.minecraft.world.entity.EquipmentSlot#BODY} slot.
     * <p>
     * Used for picking the body slot icon in the mob inventory screen.
     */
    public static final TagKey<EntityType<?>> CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG = TAGS.registerEntityTypeTag(
            "can_equip_body_item");
    /**
     * Attributes that render in the {@link net.minecraft.client.gui.components.DebugScreenOverlay} when targeting an
     * entity.
     */
    public static final TagKey<Attribute> DEBUG_ATTRIBUTES_ATTRIBUTE_TAG = TAGS.registerTagKey(Registries.ATTRIBUTE,
            "debug_attributes");
    public static final TagKey<Item> SHEAR_TOOLS_ITEM_TAG = TagFactory.COMMON.registerItemTag("tools/shear");

    public static void bootstrap() {
        // NO-OP
    }
}
