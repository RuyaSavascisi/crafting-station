package tfar.craftingstation.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import tfar.craftingstation.platform.Services;

public class ModClient {

    public static void updateLastRecipe(ResourceLocation rec) {
        if (Minecraft.getInstance().screen instanceof CraftingStationScreen) {
            Recipe<?> r = Minecraft.getInstance().level.getRecipeManager().byKey(rec).orElse(null);
            ((CraftingStationScreen) Minecraft.getInstance().screen).getMenu().updateLastRecipeFromServer((Recipe<CraftingContainer>) r);
        }
    }

}
