package tfar.craftingstation.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.platform.Services;

public class C2SScreenUpdatePacket implements C2SModPacket{

    public C2SScreenUpdatePacket(){}

    public C2SScreenUpdatePacket(FriendlyByteBuf buf) {

    }

    @Override
    public void handleServer(ServerPlayer player) {
        if (player.containerMenu instanceof CraftingStationMenu craftingStationMenu) {
            Services.PLATFORM.sendToClient(new S2CCraftingStationMenuPacket(craftingStationMenu),player);
        }
    }

    @Override
    public void write(FriendlyByteBuf to) {

    }
}
