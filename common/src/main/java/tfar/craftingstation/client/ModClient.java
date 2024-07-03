package tfar.craftingstation.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import tfar.craftingstation.init.ModBlockEntityTypes;
import tfar.craftingstation.init.ModMenuTypes;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.network.S2CSideSetSideContainerSlot;
import tfar.craftingstation.platform.Services;
import tfar.craftingstation.util.SideContainerWrapper;

public class ModClient {

    public static void renderers() {
        MenuScreens.register(ModMenuTypes.crafting_station, CraftingStationScreen::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.crafting_station, CraftingStationBlockEntityRenderer::new);
    }

    public static void setStackInSlot(S2CSideSetSideContainerSlot s2CSideSetSideContainerSlot) {
        if (Minecraft.getInstance().player.containerMenu instanceof CraftingStationMenu craftingStationMenu) {
            SideContainerWrapper current =craftingStationMenu.getCurrentHandler();
            SideContainerWrapper sideContainerWrapper = Services.PLATFORM.getWrapper(craftingStationMenu.blockEntityMap.get(s2CSideSetSideContainerSlot.direction()));
            if (sideContainerWrapper == current) return;
            sideContainerWrapper.$setStack(s2CSideSetSideContainerSlot.slot(),s2CSideSetSideContainerSlot.stack());
        }
    }
}
