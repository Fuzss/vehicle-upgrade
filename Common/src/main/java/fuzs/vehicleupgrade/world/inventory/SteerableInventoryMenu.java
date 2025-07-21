package fuzs.vehicleupgrade.world.inventory;

import fuzs.puzzleslib.api.container.v1.QuickMoveRuleSet;
import fuzs.vehicleupgrade.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SteerableInventoryMenu extends AbstractContainerMenu implements ContainerListener {
    private static final ResourceLocation SADDLE_SLOT_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/slot/saddle");
    private static final ResourceLocation ARMOR_SLOT_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/slot/horse_armor");

    @Nullable
    private final Mob mob;

    public SteerableInventoryMenu(int containerId, Inventory inventory, int entityId) {
        this(containerId, inventory, Minecraft.getInstance().level.getEntity(entityId) instanceof Mob mob ? mob : null);
    }

    public SteerableInventoryMenu(int containerId, Inventory inventory, @Nullable Mob mob) {
        super(ModRegistry.ITEM_STEERABLE_MENU_TYPE.value(), containerId);
        this.mob = mob;
        Container container = mob.createEquipmentSlotContainer(EquipmentSlot.SADDLE);
        this.addSlot(new ArmorSlot(container, mob, EquipmentSlot.SADDLE, 0, 8, 18, SADDLE_SLOT_SPRITE) {
            @Override
            public boolean isActive() {
                return mob.canUseSlot(EquipmentSlot.SADDLE) && mob.getType().is(EntityTypeTags.CAN_EQUIP_SADDLE);
            }
        });
        Container container2 = mob.createEquipmentSlotContainer(EquipmentSlot.BODY);
        this.addSlot(new ArmorSlot(container2, mob, EquipmentSlot.BODY, 0, 8, 36, ARMOR_SLOT_SPRITE) {
            @Override
            public boolean isActive() {
                return mob.canUseSlot(EquipmentSlot.BODY) && mob.getType()
                        .is(ModRegistry.CAN_EQUIP_BODY_ITEM_ENTITY_TYPE_TAG);
            }
        });
        this.addStandardInventorySlots(inventory, 8, 84);
        this.addSlotListener(this);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return QuickMoveRuleSet.of(this, this::moveItemStackTo)
                .addContainerSlotRule(0, 1)
                .addInventoryRules()
                .addInventoryCompartmentRules()
                .quickMoveStack(player, index);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.mob != null && this.mob.isAlive() && player.canInteractWithEntity(this.mob, 4.0);
    }

    public Mob getMob() {
        Objects.requireNonNull(this.mob, "entity is null");
        return this.mob;
    }

    @Override
    public void slotChanged(AbstractContainerMenu containerToSend, int dataSlotIndex, ItemStack itemStack) {
        if (this.mob != null && dataSlotIndex == 0 && !itemStack.is(Items.SADDLE)) {
            this.mob.ejectPassengers();
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu containerMenu, int dataSlotIndex, int value) {
        // NO-OP
    }
}
