package tfar.craftingstation.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkHooks;
import tfar.craftingstation.Configs;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.network.C2SModPacket;
import tfar.craftingstation.network.PacketHandlerForge;
import tfar.craftingstation.network.S2CModPacket;
import tfar.craftingstation.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import tfar.craftingstation.util.Empty;
import tfar.craftingstation.util.SideContainerForge;
import tfar.craftingstation.util.SideContainerWrapper;

import java.util.function.Function;

public class ForgePlatformHelper implements IPlatformHelper {

    final MLConfig config = new Configs();

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        PacketHandlerForge.INSTANCE.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        PacketHandlerForge.INSTANCE.sendToServer(msg);
    }

    int i;

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapS2C());
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG>  packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapC2S());
    }

    @Override
    public void forgeHooks$setCraftingPlayer(Player player) {
        ForgeHooks.setCraftingPlayer(player);
    }

    @Override
    public void forgeEventFactory$firePlayerCraftingEvent(Player player, ItemStack stack, CraftingContainer craftingContainer) {
        ForgeEventFactory.firePlayerCraftingEvent(player,stack,craftingContainer);
    }

    @Override
    public boolean hasCapability(BlockEntity blockEntity) {
        return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).filter(IItemHandlerModifiable.class::isInstance).isPresent();
    }

    @Override
    public MLConfig getConfig() {
        return config;
    }

    @Override
    public MenuType<CraftingStationMenu> customMenu() {
        return IForgeMenuType.create((windowId, inv, data) -> new CraftingStationMenu(windowId,inv,data.readBlockPos()));
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider menuProvider, BlockPos pos) {
        NetworkHooks.openScreen(player, menuProvider, pos);
    }

    @Override
    public SideContainerWrapper getWrapper(BlockEntity blockEntity) {
        if (blockEntity == null) return Empty.EMPTY;

        IItemHandler handler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
        if (handler instanceof IItemHandlerModifiable iItemHandlerModifiable) {
            return new SideContainerForge(iItemHandlerModifiable);
        }
        return Empty.EMPTY;
    }
}