package fuzs.vehicleupgrade.world;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentUser;
import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.BooleanSupplier;

public class ItemProviderContainer implements Container {
    private final List<ContainerItemAdapter> adapters;

    public ItemProviderContainer(ItemBasedSteering itemBasedSteering) {
        this(ImmutableList.of(new PropertyItemProvider(Items.SADDLE,
                itemBasedSteering::hasSaddle,
                itemBasedSteering::setSaddle)));
    }

    public ItemProviderContainer(EquipmentUser equipmentUser, Item item) {
        this(ImmutableList.of(new EquipmentItemProvider(equipmentUser, item, EquipmentSlot.BODY)));
    }

    private ItemProviderContainer(List<ContainerItemAdapter> adapters) {
        this.adapters = adapters;
    }

    @Override
    public int getContainerSize() {
        return this.adapters.size();
    }

    @Override
    public boolean isEmpty() {
        for (ContainerItemAdapter adapter : this.adapters) {
            if (!adapter.getItem().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < this.getContainerSize() ? this.adapters.get(slot).getItem() : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot >= 0 && slot < this.getContainerSize()) {
            ContainerItemAdapter adapter = this.adapters.get(slot);
            ItemStack itemStack = adapter.getItem();
            if (amount > 0 && !itemStack.isEmpty()) {
                ItemStack removedItemStack = itemStack.split(amount);
                adapter.setItem(itemStack);
                this.setChanged();
                return removedItemStack;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot >= 0 && slot < this.getContainerSize()) {
            ContainerItemAdapter adapter = this.adapters.get(slot);
            ItemStack itemStack = adapter.getItem();
            adapter.setItem(ItemStack.EMPTY);
            return itemStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        if (slot >= 0 && slot < this.getContainerSize()) {
            ContainerItemAdapter adapter = this.adapters.get(slot);
            boolean isEmpty = adapter.getItem().isEmpty();
            adapter.setItem(itemStack);
            if (isEmpty != adapter.getItem().isEmpty()) {
                this.setChanged();
            }
        }
    }

    @Override
    public void setChanged() {
        // NO-OP
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.getContainerSize()) {
            return this.adapters.get(slot).isValidItem(stack);
        } else {
            return Container.super.canPlaceItem(slot, stack);
        }
    }

    @Override
    public void clearContent() {
        for (ContainerItemAdapter adapter : this.adapters) {
            adapter.setItem(ItemStack.EMPTY);
        }
        this.setChanged();
    }

    private interface ContainerItemAdapter {

        void setItem(ItemStack itemStack);

        ItemStack getItem();

        boolean isValidItem(ItemStack itemStack);
    }

    private record PropertyItemProvider(Item item,
                                        BooleanSupplier itemPropertyGetter,
                                        BooleanConsumer itemPropertySetter) implements ContainerItemAdapter {

        @Override
        public void setItem(ItemStack itemStack) {
            this.itemPropertySetter.accept(this.isValidItem(itemStack));
        }

        @Override
        public ItemStack getItem() {
            return this.itemPropertyGetter.getAsBoolean() ? new ItemStack(this.item) : ItemStack.EMPTY;
        }

        @Override
        public boolean isValidItem(ItemStack itemStack) {
            return itemStack.is(this.item);
        }
    }

    private record EquipmentItemProvider(EquipmentUser equipmentUser,
                                         Item item,
                                         EquipmentSlot equipmentSlot) implements ContainerItemAdapter {

        @Override
        public void setItem(ItemStack itemStack) {
            this.equipmentUser.setItemSlot(this.equipmentSlot, itemStack);
        }

        @Override
        public ItemStack getItem() {
            return this.equipmentUser.getItemBySlot(this.equipmentSlot);
        }

        @Override
        public boolean isValidItem(ItemStack itemStack) {
            return itemStack.is(this.item);
        }
    }
}
