package fuzs.vehicleupgrade.world.inventory;

import fuzs.puzzleslib.api.container.v1.QuickMoveRuleSet;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.jetbrains.annotations.Nullable;

public class EquipmentInventoryMenu extends AbstractContainerMenu implements ContainerListener {
    private static final ResourceLocation SADDLE_SLOT_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/slot/saddle");
    private static final ResourceLocation HORSE_ARMOR_SLOT_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/slot/horse_armor");
    private static final ResourceLocation WOLF_ARMOR_SLOT_SPRITE = VehicleUpgrade.id("container/slot/wolf_armor");
    private static final ResourceLocation HARNESS_SLOT_SPRITE = VehicleUpgrade.id("container/slot/harness");

    @Nullable
    public final Mob mob;

    public EquipmentInventoryMenu(int containerId, Inventory inventory, int entityId) {
        this(containerId, inventory, Minecraft.getInstance().level.getEntity(entityId) instanceof Mob mob ? mob : null);
    }

    public EquipmentInventoryMenu(int containerId, Inventory inventory, @Nullable Mob mob) {
        super(ModRegistry.EQUIPMENT_USER_MENU_TYPE.value(), containerId);
        this.mob = mob;
        if (mob != null) {
            this.addMobInventorySlots(mob);
        }

        this.addStandardInventorySlots(inventory, 8, 84);
        this.addSlotListener(this);
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

    protected ResourceLocation getBodySlotSprite(EntityType<?> entityType) {
        if (entityType.is(EntityTypeTags.CAN_EQUIP_HARNESS)) {
            return HARNESS_SLOT_SPRITE;
        } else if (entityType.is(ModRegistry.CAN_WEAR_WOLF_ARMOR_ENTITY_TYPE_TAG)) {
            return WOLF_ARMOR_SLOT_SPRITE;
        } else {
            return HORSE_ARMOR_SLOT_SPRITE;
        }
    }

    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack itemStack) {
        if (this.mob != null) {
            Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
            if (dataSlotIndex == 0) {
                if (equippable == null || equippable.slot() != EquipmentSlot.SADDLE) {
                    this.mob.ejectPassengers();
                }
            } else if (dataSlotIndex == 1) {
                if (!this.getSlot(0).isActive() && (equippable == null || equippable.slot() != EquipmentSlot.BODY)) {
                    this.mob.ejectPassengers();
                }
            }
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
        // NO-OP
    }

    @Override
    public boolean stillValid(Player player) {
        return this.mob != null && this.mob.isAlive() && player.canInteractWithEntity(this.mob, 4.0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return QuickMoveRuleSet.of(this, this::moveItemStackTo)
                .addContainerSlotRule(0, 1)
                .addInventoryRules()
                .addInventoryCompartmentRules()
                .quickMoveStack(player, index);
    }
}
