package tfar.craftingstation.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.network.S2CCraftingStationMenuPacket;
import tfar.craftingstation.platform.Services;

public class ModClient {

    public static void updateLastRecipe(ResourceLocation rec) {
        Services.PLATFORM.updateLastRecipeTemp(rec);
    }

    public static void syncData(S2CCraftingStationMenuPacket s2CraftingStationMenuPacket) {
        ((CraftingStationMenu) Minecraft.getInstance().player.containerMenu).setClientData(s2CraftingStationMenuPacket.icons);
    }

}
