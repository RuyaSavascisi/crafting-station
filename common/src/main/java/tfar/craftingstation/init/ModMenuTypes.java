package tfar.craftingstation.init;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.platform.Services;

public class ModMenuTypes {
    public static final MenuType<CraftingStationMenu> crafting_station = Services.PLATFORM.customMenu();
}
