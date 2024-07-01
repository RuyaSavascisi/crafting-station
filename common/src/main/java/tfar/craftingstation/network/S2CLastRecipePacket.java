package tfar.craftingstation.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import tfar.craftingstation.client.ModClient;

public class S2CLastRecipePacket implements S2CModPacket{

  public static final ResourceLocation NULL = new ResourceLocation("null", "null");

  ResourceLocation rec;

  public S2CLastRecipePacket() {
  }

  public S2CLastRecipePacket(Recipe<CraftingContainer> toSend){
    this(toSend == null ? NULL : toSend.getId());
  }

  public S2CLastRecipePacket(ResourceLocation toSend) {
    rec = toSend;
  }


  public S2CLastRecipePacket(FriendlyByteBuf buf) {
    rec = buf.readResourceLocation();
  }

  @Override
  public void handleClient() {
    ModClient.updateLastRecipe(rec);
  }

  @Override
  public void write(FriendlyByteBuf to) {
    to.writeResourceLocation(rec);
  }
}