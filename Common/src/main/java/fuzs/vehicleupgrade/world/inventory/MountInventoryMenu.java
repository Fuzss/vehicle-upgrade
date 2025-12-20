package fuzs.vehicleupgrade.world.inventory;

import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.MenuType;
import org.jspecify.annotations.Nullable;

public class MountInventoryMenu extends AbstractMountInventoryMenu {
    private static final Identifier SADDLE_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/saddle");
    private static final Identifier HORSE_ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace(
            "container/slot/horse_armor");
    private static final Identifier WOLF_ARMOR_SLOT_SPRITE = VehicleUpgrade.id("container/slot/wolf_armor");
    private static final Identifier HARNESS_SLOT_SPRITE = VehicleUpgrade.id("container/slot/harness");

    public MountInventoryMenu(int containerId, Inventory inventory, int entityId) {
        this(containerId, inventory, Minecraft.getInstance().level.getEntity(entityId) instanceof Mob mob ? mob : null);
    }

    public MountInventoryMenu(int containerId, Inventory inventory, @Nullable Mob mob) {
        super(containerId, inventory, new SimpleContainer(0), mob);
        if (mob != null) {
            this.addMobInventorySlots(mob);
        }

        this.addStandardInventorySlots(inventory, 8, 84);
    }

    protected void addMobInventorySlots(Mob mob) {
        Container saddleContainer = mob.createEquipmentSlotContainer(EquipmentSlot.SADDLE);
        this.addSlot(new ArmorSlot(saddleContainer, mob, EquipmentSlot.SADDLE, 0, 8, 18, SADDLE_SLOT_SPRITE) {
            @Override
            public boolean isActive() {
                return mob.canUseSlot(EquipmentSlot.SADDLE) && mob.getType().is(EntityTypeTags.CAN_EQUIP_SADDLE);
            }
        });
        Container bodyContainer = mob.createEquipmentSlotContainer(EquipmentSlot.BODY);
        this.addSlot(new ArmorSlot(bodyContainer,
                mob,
                EquipmentSlot.BODY,
                0,
                8,
                36,
                this.getBodySlotSprite(mob.getType())) {
            @Override
            public boolean isActive() {
                return mob.canUseSlot(EquipmentSlot.BODY) && mob.getType()
                        .is(ModRegistry.CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG);
            }
        });
    }

    protected Identifier getBodySlotSprite(EntityType<?> entityType) {
        if (entityType.is(EntityTypeTags.CAN_EQUIP_HARNESS)) {
            return HARNESS_SLOT_SPRITE;
        } else if (entityType.is(ModRegistry.CAN_WEAR_WOLF_ARMOR_ENTITY_TYPE_TAG)) {
            return WOLF_ARMOR_SLOT_SPRITE;
        } else {
            return HORSE_ARMOR_SLOT_SPRITE;
        }
    }

    @Override
    public MenuType<?> getType() {
        return ModRegistry.EQUIPMENT_USER_MENU_TYPE.value();
    }

    public @Nullable LivingEntity getMount() {
        // This can be null when the entity is not found on the client.
        return this.mount;
    }

    @Override
    protected boolean hasInventoryChanged(Container container) {
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.getMount() != null && super.stillValid(player);
    }
}
