package fuzs.vehicleupgrade.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.vehicleupgrade.VehicleUpgrade;
import fuzs.vehicleupgrade.config.ServerConfig;
import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.world.entity.ai.goal.HorseEatingGoal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStandGoal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HorseUpgradeHandler {

    public static EventResult onEntityJoin(Entity entity, ServerLevel serverLevel, boolean isLoadedFromDisk, @Nullable MobSpawnType spawnReason) {
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

    public static EventResultHolder<InteractionResult> onUseEntity(Player player, Level level, InteractionHand interactionHand, Entity entity) {
        if (!VehicleUpgrade.CONFIG.get(ServerConfig.class).shearsRemoveChests) {
            return EventResultHolder.pass();
        }

        if (entity instanceof AbstractChestedHorse chestedHorse && chestedHorse.hasChest()) {
            if (!player.isSecondaryUseActive() && !chestedHorse.isVehicle()) {
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
        itemInHand.hurtAndBreak(1, player, LivingEntity.getSlotForHand(interactionHand));
        Vec3 vec3 = chestedHorse.getAttachments().get(EntityAttachment.PASSENGER, 0, chestedHorse.getYRot());
        chestedHorse.setChest(false);
        chestedHorse.gameEvent(GameEvent.SHEAR, player);
        chestedHorse.playSound(SoundEvents.SNOW_GOLEM_SHEAR);
        if (chestedHorse.level() instanceof ServerLevel serverLevel) {
            ItemStack itemStack = new ItemStack(Items.CHEST);
            chestedHorse.spawnAtLocation(itemStack, (float) vec3.y());
//            CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.trigger((ServerPlayer) player, itemStack, chestedHorse);
        }
    }
}
