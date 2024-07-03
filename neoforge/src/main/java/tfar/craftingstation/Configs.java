package tfar.craftingstation;

import net.neoforged.neoforge.common.ModConfigSpec;
import tfar.craftingstation.platform.MLConfig;

public class Configs implements MLConfig {

    @Override
    public boolean showItemsInTable() {
        return Client.showItemsInTable.get();
    }

    @Override
    public boolean sideContainers() {
        return Server.sideInventories.get();
    }

    public static class Client {

      public static ModConfigSpec.BooleanValue showItemsInTable;

    Client(ModConfigSpec.Builder builder) {
      builder.push("general");
      showItemsInTable = builder
              .comment("Display Items in Table?")
              .translation("text.craftingstation.config.displayitemsintable")
              .define("display items in table", true);
      builder.pop();
    }
  }

  public static class Server {

      public static ModConfigSpec.BooleanValue sideInventories;


      Server(ModConfigSpec.Builder builder) {
          builder.push("general");
          sideInventories = builder
                  .comment("Are side inventories displayed in the crafting grid?")
                  .translation("text.craftingstation.config.enable_side_inventories")
                  .define("display side inventories in crafting grid", true);
          builder.pop();
      }
  }

}
