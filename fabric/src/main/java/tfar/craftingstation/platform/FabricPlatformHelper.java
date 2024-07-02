package tfar.craftingstation.platform;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.MenuType;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }


    @Override
    public MenuType<CraftingStationMenu> customMenu() {
        return new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new CraftingStationMenu(syncId,inventory,buf.readBlockPos()));
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider menuProvider, BlockPos pos) {
        player.openMenu(menuProvider);
    }
}
