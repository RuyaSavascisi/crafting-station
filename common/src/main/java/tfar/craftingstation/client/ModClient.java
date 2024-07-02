package tfar.craftingstation.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.network.S2CSideSetSideContainerSlot;
import tfar.craftingstation.platform.Services;
import tfar.craftingstation.util.SideContainerWrapper;

public class ModClient {

    public static void updateLastRecipe(ResourceLocation rec) {
        if (Minecraft.getInstance().screen instanceof CraftingStationScreen) {
            Recipe<?> r = Minecraft.getInstance().level.getRecipeManager().byKey(rec).orElse(null);
            ((CraftingStationScreen) Minecraft.getInstance().screen).getMenu().updateLastRecipeFromServer((Recipe<CraftingContainer>) r);
        }
    }

    public static void setStackInSlot(S2CSideSetSideContainerSlot s2CSideSetSideContainerSlot) {
        if (Minecraft.getInstance().player.containerMenu instanceof CraftingStationMenu craftingStationMenu) {
            SideContainerWrapper current =craftingStationMenu.getCurrentHandler();
            SideContainerWrapper sideContainerWrapper = Services.PLATFORM.getWrapper(craftingStationMenu.blockEntityMap.get(s2CSideSetSideContainerSlot.direction));
            if (sideContainerWrapper == current) return;
            sideContainerWrapper.$setStack(s2CSideSetSideContainerSlot.slot,s2CSideSetSideContainerSlot.stack);
        }
    }
}
