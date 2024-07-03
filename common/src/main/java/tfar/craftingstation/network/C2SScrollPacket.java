package tfar.craftingstation.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import tfar.craftingstation.menu.CraftingStationMenu;

public record C2SScrollPacket(int firstSlot) implements C2SModPacket{

    public static final CustomPacketPayload.Type<C2SScrollPacket> TYPE = new CustomPacketPayload.Type<>(PacketHandler.packet(C2SScrollPacket.class));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SScrollPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            C2SScrollPacket::firstSlot,
            C2SScrollPacket::new);

    @Override
    public void handleServer(ServerPlayer player) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof CraftingStationMenu craftingStationMenu) {
            craftingStationMenu.setFirstSlot(firstSlot);
        }
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
