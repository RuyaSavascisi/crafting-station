
package tfar.craftingstation.slot;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import tfar.craftingstation.platform.Services;

import java.util.Collections;
import java.util.List;


/**
 * CraftResultSlotExt from FastWorkbench adapted for the Crafting Station container (no change in functionality)
 * See: https://github.com/Shadows-of-Fire/FastWorkbench/blob/1.20/src/main/java/dev/shadowsoffire/fastbench/util/CraftResultSlotExt.java
 * <p>
 * Basically it makes crafting less laggy
 */
public class SlotFastCraft extends ResultSlot {

    protected final Player player;
  protected final CraftingContainer matrix;
  protected final ResultContainer inv;

  public SlotFastCraft(Player player, CraftingContainer matrix, ResultContainer inv, int slotIndex, int xPosition, int yPosition) {
    super(player, matrix, inv, slotIndex, xPosition, yPosition);
    this.player = player;
    this.matrix = matrix;
    this.inv = inv;
  }

  @Override
  public ItemStack remove(int amount) {
    if (this.hasItem()) {
      this.removeCount += Math.min(amount, this.getItem().getCount());
    }
    return this.getItem().copy();
  }

  @Override
  protected void onSwapCraft(int numItemsCrafted) {
    super.onSwapCraft(numItemsCrafted);
    this.inv.setItem(0, this.getItem().copy()); // https://github.com/Shadows-of-Fire/FastWorkbench/issues/62 - Vanilla's SWAP action will leak this stack here.
  }

  @Override
  public void set(ItemStack stack) {}

  @Override
  protected void checkTakeAchievements(ItemStack stack) {
    if (this.removeCount > 0) {
      stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
      Services.PLATFORM.forgeEventFactory$firePlayerCraftingEvent(this.player, stack, matrix);
    }
    this.removeCount = 0;

    // Have to copy this code because vanilla nulls out the recipe, which shouldn't be done.
    Recipe<?> recipe = this.inv.getRecipeUsed();
    if (recipe != null) {
      this.player.triggerRecipeCrafted(this.inv.getRecipeUsed(), matrix.getItems());
      if (!recipe.isSpecial()) {
        this.player.awardRecipes(Collections.singleton(recipe));
      }
    }
  }
  
  @Override
  @SuppressWarnings({ "unchecked" })
  public void onTake(Player player, ItemStack stack) {
    this.checkTakeAchievements(stack);
    Services.PLATFORM.forgeHooks$setCraftingPlayer(player);
    List<ItemStack> list;
    Recipe<CraftingContainer> recipe = (Recipe<CraftingContainer>) this.inv.getRecipeUsed();
    if (recipe != null && recipe.matches(matrix, player.level())) list = recipe.getRemainingItems(matrix);
    else list = matrix.getItems();
    Services.PLATFORM.forgeHooks$setCraftingPlayer(null);

    for (int i = 0; i < list.size(); ++i) {
      ItemStack current = matrix.getItem(i);
      ItemStack remaining = list.get(i);

      if (!current.isEmpty()) {
        matrix.removeItem(i, 1);
        current = matrix.getItem(i);
      }

      if (!remaining.isEmpty()) {
        if (current.isEmpty()) {
          matrix.setItem(i, remaining);
        }
        else if (ItemStack.isSameItemSameTags(current, remaining)) {
          remaining.grow(current.getCount());
          matrix.setItem(i, remaining);
        }
        else if (!this.player.getInventory().add(remaining)) {
          this.player.drop(remaining, false);
        }
      }
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public ItemStack getItem() {
    if (player.level().isClientSide) return super.getItem();
    // Crafting Tweaks fakes 64x right click operations to right-click craft a stack to the "held" item, so we need to verify the recipe here.
    Recipe<CraftingContainer> recipe = (Recipe<CraftingContainer>) this.inv.getRecipeUsed();
    if (recipe != null && recipe.matches(matrix, player.level())) return super.getItem();
    return ItemStack.EMPTY;
  }
}