package fuzs.vehicleupgrade.world.inventory;

import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.world.ItemProviderContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class SteerableInventoryMenu extends AbstractContainerMenu implements ContainerListener {
    static final Map<Class<?>, Function<LivingEntity, @Nullable ItemBasedSteering>> ITEM_BASED_STEERING_GETTERS = new IdentityHashMap<>();

    @Nullable
    public final Mob mob;

    public SteerableInventoryMenu(int containerId, Inventory inventory, int entityId) {
        this(containerId, inventory, Minecraft.getInstance().level.getEntity(entityId) instanceof Mob mob ? mob : null);
    }

    public SteerableInventoryMenu(int containerId, Inventory inventory, @Nullable Mob mob) {
        super(ModRegistry.EQUIPMENT_USER_MENU_TYPE.value(), containerId);
        // TODO bring this more inline with the updated menu
        this.mob = mob;
        Container container = this.createContainer(mob);
        this.addSlot(new Slot(container, 0, 8, 18) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return this.container.canPlaceItem(0, itemStack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        ContainerMenuHelper.addStandardInventorySlots(this, inventory, 8, 84);
        this.addSlotListener(this);
    }

    public int getContainerSlots() {
        return 1;
    }

    private Container createContainer(@Nullable LivingEntity livingEntity) {
        // TODO remove all the reflection hacks and just filter by entity class (pig & strider)
        if (livingEntity instanceof Saddleable) {
            ItemBasedSteering itemBasedSteering = ITEM_BASED_STEERING_GETTERS.computeIfAbsent(livingEntity.getClass(),
                    SteerableInventoryMenu::collectItemBasedSteeringGetter).apply(livingEntity);
            if (itemBasedSteering != null) {
                return new ItemProviderContainer(itemBasedSteering);
            }
        } else if (livingEntity instanceof Wolf wolf) {
            return new ItemProviderContainer(wolf, Items.WOLF_ARMOR);
        }

        return new SimpleContainer(this.getContainerSlots());
    }

    private static Function<LivingEntity, @Nullable ItemBasedSteering> collectItemBasedSteeringGetter(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == ItemBasedSteering.class) {
                field.setAccessible(true);
                try {
                    MethodHandle methodHandle = MethodHandles.lookup().unreflectGetter(field);
                    return (LivingEntity livingEntity) -> {
                        try {
                            return (ItemBasedSteering) methodHandle.invoke(livingEntity);
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    };
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }

        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Mob.class) {
            return collectItemBasedSteeringGetter(clazz.getSuperclass());
        } else {
            return (LivingEntity livingEntity) -> null;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Copied from HorseInventoryMenu::quickMoveStack
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < this.getContainerSlots()) {
                if (!this.moveItemStackTo(itemStack2, this.getContainerSlots(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemStack2) && !this.getSlot(0).hasItem()) {
                if (!this.moveItemStackTo(itemStack2, 0, this.getContainerSlots(), false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                int inventorySlots = this.getContainerSlots() + Inventory.INVENTORY_SIZE - Inventory.getSelectionSize();
                int hotbarSlots = this.getContainerSlots() + Inventory.INVENTORY_SIZE;
                if (index >= inventorySlots && index < hotbarSlots) {
                    if (!this.moveItemStackTo(itemStack2, this.getContainerSlots(), inventorySlots, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < inventorySlots) {
                    if (!this.moveItemStackTo(itemStack2, inventorySlots, hotbarSlots, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemStack2, inventorySlots, inventorySlots, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.mob != null && this.mob.isAlive() && player.canInteractWithEntity(this.mob, 4.0);
    }

    public LivingEntity getMob() {
        Objects.requireNonNull(this.mob, "entity is null");
        return this.mob;
    }

    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack itemStack) {
        if (this.mob instanceof Saddleable && dataSlotIndex == 0 && !itemStack.is(Items.SADDLE)) {
            this.mob.ejectPassengers();
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
        // NO-OP
    }
}
