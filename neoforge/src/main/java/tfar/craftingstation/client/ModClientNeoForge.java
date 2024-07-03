package tfar.craftingstation.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import tfar.craftingstation.init.ModBlockEntityTypes;
import tfar.craftingstation.init.ModMenuTypes;

public class ModClientNeoForge {

  public static void setup(IEventBus bus) {
    bus.addListener(ModClientNeoForge::doClientStuff);
  }

  static void doClientStuff(final FMLClientSetupEvent event) {
    MenuScreens.register(ModMenuTypes.crafting_station, CraftingStationScreen::new);
    BlockEntityRenderers.register(ModBlockEntityTypes.crafting_station, CraftingStationBlockEntityRenderer::new);
  }
}
