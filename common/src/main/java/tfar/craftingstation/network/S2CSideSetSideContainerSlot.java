package tfar.craftingstation.network;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import tfar.craftingstation.client.ModClient;

public class S2CSideSetSideContainerSlot implements S2CModPacket{

    public final ItemStack stack;
    public final Direction direction;
    public final int slot;

    public S2CSideSetSideContainerSlot(ItemStack stack, Direction direction, int slot) {
        this.stack = stack;
        this.direction = direction;
        this.slot = slot;
    }

    public S2CSideSetSideContainerSlot(FriendlyByteBuf buf) {
        stack = buf.readItem();
        direction = buf.readEnum(Direction.class);
        slot = buf.readInt();
    }


    @Override
    public void handleClient() {
        ModClient.setStackInSlot(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeItem(stack);
        to.writeEnum(direction);
        to.writeInt(slot);
    }
}
