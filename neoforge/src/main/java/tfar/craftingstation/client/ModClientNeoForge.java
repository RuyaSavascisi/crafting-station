package tfar.craftingstation.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModClientNeoForge {

  public static void setup(IEventBus bus) {
    bus.addListener(ModClientNeoForge::doClientStuff);
    bus.addListener(ModClientNeoForge::renderers);
  }

  public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
    ModClient.renderers();
  }

  static void doClientStuff(final FMLClientSetupEvent event) {

  }
}
