package fuzs.vehicleupgrade.network.client;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.vehicleupgrade.handler.MountInventoryHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

/**
 * @see net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket
 * @see net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action#OPEN_INVENTORY
 */
public record ServerboundOpenEquipmentInventoryMessage(int entityId) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundOpenEquipmentInventoryMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ServerboundOpenEquipmentInventoryMessage::entityId,
            ServerboundOpenEquipmentInventoryMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                Entity entity = context.level().getEntity(ServerboundOpenEquipmentInventoryMessage.this.entityId);
                if (entity != null) {
                    MountInventoryHandler.openInventoryScreen(context.level(), entity, context.player());
                }
            }
        };
    }
}
