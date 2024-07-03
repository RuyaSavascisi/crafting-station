package tfar.craftingstation.client;

import net.minecraft.client.Minecraft;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.network.S2CSideSetSideContainerSlot;
import tfar.craftingstation.platform.Services;
import tfar.craftingstation.util.SideContainerWrapper;

public class ModClient {

    public static void setStackInSlot(S2CSideSetSideContainerSlot s2CSideSetSideContainerSlot) {
        if (Minecraft.getInstance().player.containerMenu instanceof CraftingStationMenu craftingStationMenu) {
            SideContainerWrapper current =craftingStationMenu.getCurrentHandler();
            SideContainerWrapper sideContainerWrapper = Services.PLATFORM.getWrapper(craftingStationMenu.blockEntityMap.get(s2CSideSetSideContainerSlot.direction()));
            if (sideContainerWrapper == current) return;
            sideContainerWrapper.$setStack(s2CSideSetSideContainerSlot.slot(),s2CSideSetSideContainerSlot.stack());
        }
    }
}
