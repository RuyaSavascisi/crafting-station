package tfar.craftingstation.network;

import tfar.craftingstation.platform.Services;

public class PacketHandler {

    public static void registerPackets() {
        registerClientPackets();
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientPacket(S2CLastRecipePacket.class,S2CLastRecipePacket::new);

    }

}
