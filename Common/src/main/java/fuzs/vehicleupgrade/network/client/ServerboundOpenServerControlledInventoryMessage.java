package fuzs.vehicleupgrade.network.client;

import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.vehicleupgrade.init.ModRegistry;
import fuzs.vehicleupgrade.world.inventory.SteerableInventoryMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public record ServerboundOpenServerControlledInventoryMessage(int entityId) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundOpenServerControlledInventoryMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ServerboundOpenServerControlledInventoryMessage::entityId,
            ServerboundOpenServerControlledInventoryMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                Entity entity = context.level()
                        .getEntity(ServerboundOpenServerControlledInventoryMessage.this.entityId);
                if (entity != null) {
                    if (entity instanceof HasCustomInventoryScreen hasCustomInventoryScreen) {
                        hasCustomInventoryScreen.openCustomInventoryScreen(context.player());
                    } else if (entity instanceof Mob mob && mob.getType()
                            .is(ModRegistry.CUSTOM_EQUIPMENT_USER_ENTITY_TYPE_TAG)) {
                        openCustomInventoryScreen(context.player(), mob);
                    }
                }
            }
        };
    }

    public static void openCustomInventoryScreen(ServerPlayer serverPlayer, Mob mob) {
        if (mob.isSaddled() && (!mob.isVehicle() || mob.hasPassenger(serverPlayer))) {
            ContainerMenuHelper.openMenu(serverPlayer,
                    new SimpleMenuProvider((int containerId, Inventory inventory, Player player) -> {
                        return new SteerableInventoryMenu(containerId, inventory, mob);
                    }, mob.getDisplayName()),
                    mob.getId());
        }
    }
}
