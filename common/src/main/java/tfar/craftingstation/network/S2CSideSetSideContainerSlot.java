package tfar.craftingstation.network;

import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import tfar.craftingstation.client.ModClient;

public record S2CSideSetSideContainerSlot(ItemStack stack, Direction direction, int slot) implements S2CModPacket {

    public static final CustomPacketPayload.Type<S2CSideSetSideContainerSlot> TYPE = new CustomPacketPayload.Type<>(PacketHandler.packet(S2CSideSetSideContainerSlot.class));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSideSetSideContainerSlot> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC,
            S2CSideSetSideContainerSlot::stack,
            Direction.STREAM_CODEC,
            S2CSideSetSideContainerSlot::direction,
            ByteBufCodecs.INT,
            S2CSideSetSideContainerSlot::slot,
            S2CSideSetSideContainerSlot::new);


    @Override
    public void handleClient() {
        ModClient.setStackInSlot(this);
    }

    @Override
    public Type<S2CSideSetSideContainerSlot> type() {
        return TYPE;
    }
}
