package fuzs.vehicleupgrade.common.handler;

import fuzs.puzzleslib.common.api.event.v1.core.EventResult;
import fuzs.puzzleslib.common.api.event.v1.core.EventResultHolder;
import fuzs.vehicleupgrade.common.VehicleUpgrade;
import fuzs.vehicleupgrade.common.config.ServerConfig;
import fuzs.vehicleupgrade.common.init.ModRegistry;
import fuzs.vehicleupgrade.common.world.entity.ai.goal.HorseEatingGoal;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStandGoal;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class HorseUpgradeHandler {

    public static EventResult onEntityJoin(Entity entity, ServerLevel serverLevel, boolean isLoadedFromDisk, @Nullable EntitySpawnReason entitySpawnReason) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).smarterHorseBehavior) {
            return EventResult.PASS;
        }

        if (entity instanceof AbstractHorse horse) {
            horse.goalSelector.removeAllGoals((Goal goal) -> goal instanceof RandomStandGoal);
            if (horse.canEatGrass()) {
                // Priority is tied to an internal random chance to closely resemble vanilla behaviour.
                horse.goalSelector.addGoal(7, new HorseEatingGoal(horse));
            }
        }

        return EventResult.PASS;
    }

    public static EventResultHolder<InteractionResult> onUseEntity(Player player, Level level, InteractionHand interactionHand, Entity entity, Vec3 hitVector) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).shearsRemoveChests) {
            return EventResultHolder.pass();
        }

        if (entity instanceof AbstractChestedHorse chestedHorse && chestedHorse.hasChest()) {
            if (!player.isSecondaryUseActive() && chestedHorse.canShearEquipment(player)) {
                ItemStack itemInHand = player.getItemInHand(interactionHand);

                if (itemInHand.is(ModRegistry.SHEAR_TOOLS_ITEM_TAG)) {
                    shearChestEquipment(player, interactionHand, itemInHand, chestedHorse);
                    return EventResultHolder.interrupt(InteractionResult.SUCCESS);
                }
            }
        }

        return EventResultHolder.pass();
    }

    /**
     * @see Entity#attemptToShearEquipment(Player, InteractionHand, ItemStack, Mob)
     */
    private static void shearChestEquipment(Player player, InteractionHand interactionHand, ItemStack itemInHand, AbstractChestedHorse chestedHorse) {
        itemInHand.hurtAndBreak(1, player, interactionHand.asEquipmentSlot());
        Vec3 vec3 = chestedHorse.getAttachments().getAverage(EntityAttachment.PASSENGER);
        chestedHorse.setChest(false);
        chestedHorse.gameEvent(GameEvent.SHEAR, player);
        chestedHorse.playSound(SoundEvents.SHEARS_SNIP);
        if (chestedHorse.level() instanceof ServerLevel serverLevel) {
            ItemStack itemStack = new ItemStack(Items.CHEST);
            chestedHorse.spawnAtLocation(serverLevel, itemStack, vec3);
            CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.trigger((ServerPlayer) player, itemStack, chestedHorse);
        }
    }
}
