package fuzs.vehicleupgrade.common.world.inventory;

import fuzs.puzzleslib.common.api.util.v1.CommonHelper;
import fuzs.vehicleupgrade.common.VehicleUpgrade;
import fuzs.vehicleupgrade.common.init.ModRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.MenuType;

public class MountInventoryMenu extends AbstractMountInventoryMenu {
    private static final Identifier SADDLE_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/saddle");
    private static final Identifier HORSE_ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace(
            "container/slot/horse_armor");
    private static final Identifier WOLF_ARMOR_SLOT_SPRITE = VehicleUpgrade.id("container/slot/wolf_armor");
    private static final Identifier HARNESS_SLOT_SPRITE = VehicleUpgrade.id("container/slot/harness");

    public MountInventoryMenu(int containerId, Inventory inventory, int mountId) {
        this(containerId,
                inventory,
                CommonHelper.getClientLevel().getEntity(mountId) instanceof Mob mount ? mount :
                        CommonHelper.getClientPlayer());
    }

    public MountInventoryMenu(int containerId, Inventory inventory, Mob mount) {
        this(containerId, inventory, (LivingEntity) mount);
    }

    private MountInventoryMenu(int containerId, Inventory inventory, LivingEntity mount) {
        super(containerId, inventory, new SimpleContainer(0), mount);
        if (mount instanceof Mob mob) {
            this.addMobInventorySlots(mob);
        }

        this.addStandardInventorySlots(inventory, 8, 84);
    }

    private void addMobInventorySlots(Mob mob) {
        Container saddleContainer = mob.createEquipmentSlotContainer(EquipmentSlot.SADDLE);
        this.addSlot(new ArmorSlot(saddleContainer, mob, EquipmentSlot.SADDLE, 0, 8, 18, SADDLE_SLOT_SPRITE) {
            @Override
            public boolean isActive() {
                return mob.canUseSlot(EquipmentSlot.SADDLE) && mob.is(EntityTypeTags.CAN_EQUIP_SADDLE);
            }
        });
        Container bodyContainer = mob.createEquipmentSlotContainer(EquipmentSlot.BODY);
        this.addSlot(new ArmorSlot(bodyContainer, mob, EquipmentSlot.BODY, 0, 8, 36, this.getBodySlotSprite(mob)) {
            @Override
            public boolean isActive() {
                return mob.canUseSlot(EquipmentSlot.BODY) && mob.is(ModRegistry.CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG);
            }
        });
    }

    private Identifier getBodySlotSprite(Entity entity) {
        if (entity.is(EntityTypeTags.CAN_EQUIP_HARNESS)) {
            return HARNESS_SLOT_SPRITE;
        } else if (entity.is(ModRegistry.CAN_WEAR_WOLF_ARMOR_ENTITY_TYPE_TAG)) {
            return WOLF_ARMOR_SLOT_SPRITE;
        } else {
            return HORSE_ARMOR_SLOT_SPRITE;
        }
    }

    @Override
    public MenuType<?> getType() {
        return ModRegistry.EQUIPMENT_USER_MENU_TYPE.value();
    }

    @Override
    protected boolean hasInventoryChanged(Container container) {
        return false;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.mount instanceof Mob && super.stillValid(player);
    }

    public LivingEntity getMount() {
        return this.mount;
    }
}
