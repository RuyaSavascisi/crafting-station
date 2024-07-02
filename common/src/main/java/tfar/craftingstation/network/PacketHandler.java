package tfar.craftingstation.network;

import tfar.craftingstation.platform.Services;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerServerPacket(C2SScreenUpdatePacket.class, C2SScreenUpdatePacket::new);
        registerClientPackets();
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientPacket(S2CLastRecipePacket.class,S2CLastRecipePacket::new);
        Services.PLATFORM.registerClientPacket(S2CCraftingStationMenuPacket.class,S2CCraftingStationMenuPacket::new);

    }

}
