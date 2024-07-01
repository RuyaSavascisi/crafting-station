package tfar.craftingstation.init;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import tfar.craftingstation.CraftingStationMenu;

public class ModMenuTypes {
    public static final MenuType<CraftingStationMenu> crafting_station = new MenuType<>(CraftingStationMenu::new, FeatureFlags.VANILLA_SET);
}
