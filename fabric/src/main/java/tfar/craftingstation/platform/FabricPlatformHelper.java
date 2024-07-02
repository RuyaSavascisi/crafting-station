package tfar.craftingstation.platform;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.network.C2SModPacket;
import tfar.craftingstation.network.S2CModPacket;
import tfar.craftingstation.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import tfar.craftingstation.util.SideContainerWrapper;

import java.util.function.Function;

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
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {

    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {

    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {

    }

    @Override
    public void sendToServer(C2SModPacket msg) {

    }

    @Override
    public void forgeHooks$setCraftingPlayer(Player player) {

    }

    @Override
    public void forgeEventFactory$firePlayerCraftingEvent(Player player, ItemStack stack, CraftingContainer craftingContainer) {

    }

    @Override
    public boolean hasCapability(BlockEntity blockEntity) {
        return false;
    }

    @Override
    public MLConfig getConfig() {
        return new MLConfig() {
            @Override
            public boolean showItemsInTable() {
                return true;
            }

            @Override
            public boolean sideContainers() {
                return true;
            }
        };
    }


    @Override
    public MenuType<CraftingStationMenu> customMenu() {
        return new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> new CraftingStationMenu(syncId,inventory,buf.readBlockPos()));
    }

    @Override
    public SideContainerWrapper getWrapper(BlockEntity blockEntity) {
        return null;
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider menuProvider, BlockPos pos) {
        player.openMenu(menuProvider);
    }
}
