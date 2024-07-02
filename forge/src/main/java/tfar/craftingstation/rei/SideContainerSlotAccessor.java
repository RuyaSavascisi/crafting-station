package tfar.craftingstation.rei;

import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import net.minecraft.world.item.ItemStack;
import tfar.craftingstation.util.SideContainerWrapper;

public class SideContainerSlotAccessor implements SlotAccessor {

    private final SideContainerWrapper sideContainerWrapper;
    private final int slot;

    public SideContainerSlotAccessor(SideContainerWrapper sideContainerWrapper, int slot) {

        this.sideContainerWrapper = sideContainerWrapper;
        this.slot = slot;
    }

    @Override
    public ItemStack getItemStack() {
        return sideContainerWrapper.$getStack(slot);
    }

    @Override
    public void setItemStack(ItemStack stack) {
        sideContainerWrapper.$setStack(slot,stack);
    }

    @Override
    public ItemStack takeStack(int amount) {
        return sideContainerWrapper.$removeStack(slot,amount);
    }
}
