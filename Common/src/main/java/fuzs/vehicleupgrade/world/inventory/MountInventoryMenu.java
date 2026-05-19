package fuzs.vehicleupgrade.world.inventory;

import com.mojang.datafixers.util.Pair;
import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import fuzs.puzzleslib.api.container.v1.QuickMoveRuleSet;
import fuzs.puzzleslib.api.util.v1.CommonHelper;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.ticks.ContainerSingleItem;

public class MountInventoryMenu extends AbstractContainerMenu implements ContainerListener {
    private static final ResourceLocation SADDLE_SLOT_SPRITE = VehicleUpgrade.id("item/empty_slot_saddle");
    private static final ResourceLocation HORSE_ARMOR_SLOT_SPRITE = VehicleUpgrade.id("item/empty_slot_horse_armor");
    private static final ResourceLocation WOLF_ARMOR_SLOT_SPRITE = VehicleUpgrade.id("item/empty_slot_wolf_armor");

    public final LivingEntity mount;

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
        super(ModRegistry.EQUIPMENT_USER_MENU_TYPE.value(), containerId);
        this.mount = mount;
        if (mount instanceof Mob mob) {
            this.addMobInventorySlots(mob);
        }

        ContainerMenuHelper.addStandardInventorySlots(this, inventory, 8, 84);
        this.addSlotListener(this);
    }

    protected void addMobInventorySlots(Mob mob) {
        Container saddleContainer = this.createSaddleContainer(mob);
        this.addSlot(new Slot(saddleContainer, 0, 8, 18) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPlace(ItemStack item) {
                return item.is(Items.SADDLE);
            }

            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, SADDLE_SLOT_SPRITE);
            }

            @Override
            public boolean isActive() {
                return mob instanceof Saddleable;
            }
        });
        Container bodyContainer = this.createEquipmentSlotContainer(mob, EquipmentSlot.BODY);
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

    private Container createSaddleContainer(Mob mob) {
        if (mob instanceof Pig pig) {
            return this.createSteeringContainer(pig, pig.steering);
        } else if (mob instanceof Strider strider) {
            return this.createSteeringContainer(strider, strider.steering);
        } else {
            return new SimpleContainer(1);
        }
    }

    private Container createSteeringContainer(Mob mob, ItemBasedSteering steering) {
        return new ContainerSingleItem() {
            @Override
            public ItemStack getTheItem() {
                return steering.hasSaddle() ? new ItemStack(Items.SADDLE) : ItemStack.EMPTY;
            }

            /**
             * @see net.minecraft.world.level.block.entity.DecoratedPotBlockEntity#splitTheItem(int)
             */
            @Override
            public ItemStack splitTheItem(int amount) {
                ItemStack originalItem = this.getTheItem();
                ItemStack item = originalItem.split(amount);
                this.setTheItem(originalItem);
                return item;
            }

            @Override
            public void setTheItem(ItemStack item) {
                steering.setSaddle(item.is(Items.SADDLE));
                if (item.is(Items.SADDLE)) {
                    mob.setPersistenceRequired();
                }
            }

            @Override
            public void setChanged() {
                // NO-OP
            }

            @Override
            public boolean stillValid(Player player) {
                return player.getVehicle() == mob || player.canInteractWithEntity(mob, 4.0);
            }
        };
    }

    /**
     * Copied from {@code Mob::createEquipmentSlotContainer} in Minecraft 1.21.8.
     */
    private Container createEquipmentSlotContainer(Mob mob, EquipmentSlot slot) {
        return new ContainerSingleItem() {
            @Override
            public ItemStack getTheItem() {
                return mob.getItemBySlot(slot);
            }

            @Override
            public void setTheItem(ItemStack item) {
                mob.setItemSlot(slot, item);
                if (!item.isEmpty()) {
                    mob.setGuaranteedDrop(slot);
                    mob.setPersistenceRequired();
                }
            }

            @Override
            public void setChanged() {
                // NO-OP
            }

            @Override
            public boolean stillValid(Player player) {
                return player.getVehicle() == mob || player.canInteractWithEntity(mob, 4.0);
            }
        };
    }

    protected ResourceLocation getBodySlotSprite(EntityType<?> entityType) {
        if (entityType.is(ModRegistry.CAN_WEAR_WOLF_ARMOR_ENTITY_TYPE_TAG)) {
            return WOLF_ARMOR_SLOT_SPRITE;
        } else {
            return HORSE_ARMOR_SLOT_SPRITE;
        }
    }

    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int slotIndex, ItemStack itemStack) {
        if (this.mount != null) {
            if (slotIndex == 0) {
                if (!(this.mount instanceof Saddleable) || !itemStack.is(Items.SADDLE)) {
                    this.mount.ejectPassengers();
                }
            } else if (slotIndex == 1) {
                Equipable equippable = Equipable.get(itemStack);
                if (!this.getSlot(0).isActive() && (equippable == null
                        || equippable.getEquipmentSlot() != EquipmentSlot.BODY)) {
                    this.mount.ejectPassengers();
                }
            }
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu containerMenu, int slotIndex, int value) {
        // NO-OP
    }

    @Override
    public boolean stillValid(Player player) {
        return this.mount != null && this.mount.isAlive() && player.canInteractWithEntity(this.mount, 4.0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return QuickMoveRuleSet.of(this, this::moveItemStackTo)
                .addContainerSlotRule(0, 1)
                .addInventoryRules()
                .addInventoryCompartmentRules()
                .quickMoveStack(player, index);
    }

    public LivingEntity getMount() {
        return this.mount;
    }
}
