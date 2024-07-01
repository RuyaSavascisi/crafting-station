package tfar.craftingstation.network;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import tfar.craftingstation.client.ModClient;
import tfar.craftingstation.menu.CraftingStationMenu;

import java.util.EnumMap;
import java.util.Map;

public class S2CCraftingStationMenuPacket implements S2CModPacket{



  Map<Direction, ItemStack> icons = new EnumMap<>(Direction.class);
  Map<Direction, Component> names = new EnumMap<>(Direction.class);

  public S2CCraftingStationMenuPacket() {
  }

  public S2CCraftingStationMenuPacket(CraftingStationMenu craftingStationMenu){

  }



  public S2CCraftingStationMenuPacket(FriendlyByteBuf buf) {
  }

  @Override
  public void handleClient() {
  }

  @Override
  public void write(FriendlyByteBuf to) {
  }
}