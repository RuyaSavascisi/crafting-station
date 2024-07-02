package tfar.craftingstation.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import tfar.craftingstation.menu.CraftingStationMenu;

public class C2SScrollPacket implements C2SModPacket{

    int firstSlot;

    public C2SScrollPacket(int firstSlot) {
        this.firstSlot = firstSlot;
    }

    public C2SScrollPacket(FriendlyByteBuf buf) {
        firstSlot = buf.readInt();
    }

    @Override
    public void handleServer(ServerPlayer player) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof CraftingStationMenu craftingStationMenu) {
            craftingStationMenu.setFirstSlot(firstSlot);
        }
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(firstSlot);
    }
}
