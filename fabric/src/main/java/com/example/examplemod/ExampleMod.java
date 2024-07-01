package com.example.examplemod;

import net.fabricmc.api.ModInitializer;
import tfar.craftingstation.CraftingStation;

public class ExampleMod implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        CraftingStation.LOG.info("Hello Fabric world!");
        CraftingStation.init();
    }
}
