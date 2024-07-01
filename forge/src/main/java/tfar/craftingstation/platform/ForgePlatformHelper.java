package tfar.craftingstation.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.network.NetworkDirection;
import tfar.craftingstation.client.CraftingStationScreen;
import tfar.craftingstation.network.PacketHandlerForge;
import tfar.craftingstation.network.S2CModPacket;
import tfar.craftingstation.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Function;

public class ForgePlatformHelper implements IPlatformHelper {

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

    int i;

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapS2C());
    }

    @Override
    public void updateLastRecipeTemp(ResourceLocation rec) {
        if (Minecraft.getInstance().screen instanceof CraftingStationScreen) {
            Recipe<?> r = Minecraft.getInstance().level.getRecipeManager().byKey(rec).orElse(null);
            ((CraftingStationScreen) Minecraft.getInstance().screen).getMenu().updateLastRecipeFromServer((Recipe<CraftingContainer>) r);
        }
    }
}