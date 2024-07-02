package tfar.craftingstation.util;

import net.minecraft.world.item.ItemStack;

public interface SideContainerWrapper {
    int $getSlotCount();
    ItemStack $getStack(int slot);
    void $setStack(int slot,ItemStack stack);
    ItemStack $removeStack(int slot, int count);
    default boolean $valid(int slot) {
        return slot >=0 && slot < $getSlotCount();
    }

}
