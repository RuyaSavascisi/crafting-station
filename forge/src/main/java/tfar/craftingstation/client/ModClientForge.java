package tfar.craftingstation.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfar.craftingstation.init.ModBlockEntityTypes;
import tfar.craftingstation.init.ModMenuTypes;

public class ModClientForge {

  public static void setup(IEventBus bus) {
    bus.addListener(ModClientForge::doClientStuff);
  }

  static void doClientStuff(final FMLClientSetupEvent event) {
    MenuScreens.register(ModMenuTypes.crafting_station, CraftingStationScreen::new);
    BlockEntityRenderers.register(ModBlockEntityTypes.crafting_station, CraftingStationBlockEntityRenderer::new);
  }
}
