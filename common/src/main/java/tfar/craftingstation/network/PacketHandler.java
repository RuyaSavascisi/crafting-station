package tfar.craftingstation.network;

import net.minecraft.resources.ResourceLocation;
import tfar.craftingstation.CraftingStation;
import tfar.craftingstation.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerServerPacket(C2SScrollPacket.TYPE,C2SScrollPacket.STREAM_CODEC);
        registerClientPackets();
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientPacket(S2CSideSetSideContainerSlot.TYPE,S2CSideSetSideContainerSlot.STREAM_CODEC);
    }

    public static ResourceLocation packet(Class<?> clazz) {
        return CraftingStation.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
