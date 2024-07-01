package tfar.craftingstation;

import net.fabricmc.api.ModInitializer;

public class CraftingStationFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CraftingStation.init();
    }
}
