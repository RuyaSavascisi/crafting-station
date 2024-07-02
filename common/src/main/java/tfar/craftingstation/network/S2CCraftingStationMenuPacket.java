package tfar.craftingstation.network;

import net.minecraft.client.Minecraft;
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



  public Map<Direction, ItemStack> icons = new EnumMap<>(Direction.class);
  public Map<Direction, Component> names = new EnumMap<>(Direction.class);

  public S2CCraftingStationMenuPacket() {
  }

  public S2CCraftingStationMenuPacket(CraftingStationMenu craftingStationMenu) {
    icons = craftingStationMenu.blocks;
    names = craftingStationMenu.containerNames;
  }



  public S2CCraftingStationMenuPacket(FriendlyByteBuf buf) {
    int iconSize = buf.readInt();
    for (int i = 0; i < iconSize;i++) {
      Direction direction = Direction.values()[buf.readInt()];
      ItemStack stack = buf.readItem();
      icons.put(direction,stack);
    }
  }

  @Override
  public void handleClient() {
    ModClient.syncData(this);
  }

  @Override
  public void write(FriendlyByteBuf to) {
    to.writeInt(icons.size());
    for (Map.Entry<Direction,ItemStack> entry: icons.entrySet()) {
      to.writeInt(entry.getKey().ordinal());
      to.writeItem(entry.getValue());
    }
  }
}