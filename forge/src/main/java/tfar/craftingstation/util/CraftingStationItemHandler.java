package tfar.craftingstation.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraftforge.items.ItemStackHandler;
import tfar.craftingstation.CraftingStationBlockEntity;

public class CraftingStationItemHandler extends ItemStackHandler {
  private final CraftingStationBlockEntity blockEntity;

  public CraftingStationItemHandler(int size, CraftingStationBlockEntity blockEntity){
    super(size);
    this.blockEntity = blockEntity;
  }

  public NonNullList<ItemStack> getContents(){
    return stacks;
  }

  public boolean isEmpty(){
    return getContents().stream().allMatch(ItemStack::isEmpty);
  }

  @Override
  protected void onContentsChanged(int slot) {
    this.blockEntity.setChanged();
    super.onContentsChanged(slot);
  }

}
