package tfar.craftingstation.rei;

import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.forge.REIPluginCommon;
import net.minecraft.client.renderer.Rect2i;
import tfar.craftingstation.client.CraftingStationScreen;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@REIPluginCommon
public class ReiPlugin implements REIServerPlugin {

  @Nonnull
 // @Override
  public List<Rect2i> getGuiExtraAreas(CraftingStationScreen containerScreen) {
    List<Rect2i> areas = new ArrayList<>();
    if (containerScreen.getMenu().hasSideContainers()){
      int x = (containerScreen.width - 140) / 2 - 140;
      int y = (containerScreen.height - 180) / 2 - 16;
      areas.add(new Rect2i(x, y, 140, 196));    }
    return areas;
  }
}
