package tfar.craftingstation.init;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import tfar.craftingstation.menu.CraftingStationMenu;

public class ModMenuTypes {
    public static final MenuType<CraftingStationMenu> crafting_station = new MenuType<>(CraftingStationMenu::new, FeatureFlags.VANILLA_SET);
}
