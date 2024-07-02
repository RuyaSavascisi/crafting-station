package tfar.craftingstation.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
import tfar.craftingstation.platform.MLConfig;
import tfar.craftingstation.util.SideContainerWrapper;

import java.util.function.Function;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader);
    <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader);

    void sendToClient(S2CModPacket msg, ServerPlayer player);
    void sendToServer(C2SModPacket msg);

    void updateLastRecipeTemp(ResourceLocation rec);

    void forgeHooks$setCraftingPlayer(Player player);

    void forgeEventFactory$firePlayerCraftingEvent(Player player, ItemStack stack, CraftingContainer craftingContainer);

    boolean hasCapability(BlockEntity blockEntity);

    MLConfig getConfig();

    void openMenu(ServerPlayer player, MenuProvider menuProvider, BlockPos pos);

    MenuType<CraftingStationMenu> customMenu();

    SideContainerWrapper getWrapper(BlockEntity blockEntity);

}