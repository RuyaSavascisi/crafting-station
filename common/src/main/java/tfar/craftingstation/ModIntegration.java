package tfar.craftingstation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import tfar.craftingstation.platform.Services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModIntegration {

    public static final String POLYMORPH = "polymorph";
    private static final Method GET_TILE_ENTITY_METHOD;
    private static final Method GET_PLAYER_RECIPE;

    static {
        Method doubleSlabsGetTileEntity1 = null;
        if (Services.PLATFORM.isModLoaded("doubleslabs")) {
            try {
                Class<?> doubleSlabsFlags = Class.forName("cjminecraft.doubleslabs.api.Flags");
                doubleSlabsGetTileEntity1 = doubleSlabsFlags.getDeclaredMethod("getTileEntityAtPos", BlockPos.class, BlockGetter.class);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        GET_TILE_ENTITY_METHOD = doubleSlabsGetTileEntity1;

        Method getPlayerRecipe = null;

        if (Services.PLATFORM.isModLoaded(POLYMORPH)) {
            try {
                Class<?> recipeSelection = Class.forName("com.illusivesoulworks.polymorph.common.crafting.RecipeSelection");
                getPlayerRecipe= recipeSelection.getDeclaredMethod("getPlayerRecipe", AbstractContainerMenu.class, RecipeType.class, Container.class, Level.class, Player.class);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        GET_PLAYER_RECIPE = getPlayerRecipe;
    }

    public static BlockEntity getTileEntityAtPos(Level world, BlockPos pos) {
        try {
            return GET_TILE_ENTITY_METHOD != null ? (BlockEntity) GET_TILE_ENTITY_METHOD.invoke(null, pos, world) : world.getBlockEntity(pos);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            return world.getBlockEntity(pos);
        }
    }


}
