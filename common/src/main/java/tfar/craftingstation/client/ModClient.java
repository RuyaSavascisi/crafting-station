package tfar.craftingstation.client;

import net.minecraft.resources.ResourceLocation;
import tfar.craftingstation.platform.Services;

public class ModClient {
    
    public static void updateLastRecipe(ResourceLocation rec) {
        Services.PLATFORM.updateLastRecipeTemp(rec);
    }
}
