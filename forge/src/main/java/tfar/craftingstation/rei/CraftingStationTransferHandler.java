package tfar.craftingstation.rei;

import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.api.common.transfer.info.stack.VanillaSlotAccessor;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.platform.Services;
import tfar.craftingstation.util.SideContainerWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CraftingStationTransferHandler implements SimpleTransferHandler {

    private final Class<CraftingStationMenu> containerClass;
    private final CategoryIdentifier<DefaultCraftingDisplay<?>> categoryIdentifier;

    public CraftingStationTransferHandler(Class<CraftingStationMenu> containerClass,
                                          CategoryIdentifier<DefaultCraftingDisplay<?>> categoryIdentifier) {

        this.containerClass = containerClass;
        this.categoryIdentifier = categoryIdentifier;
    }

    @Override
    public ApplicabilityResult checkApplicable(Context context) {
        if (!containerClass.isInstance(context.getMenu())
                || !categoryIdentifier.equals(context.getDisplay().getCategoryIdentifier())
                || context.getContainerScreen() == null) {
            return ApplicabilityResult.createNotApplicable();
        } else {
            return ApplicabilityResult.createApplicable();
        }
    }

    @Override
    public Iterable<SlotAccessor> getInputSlots(Context context) {
        return IntStream.range(1, 10).mapToObj(id -> SlotAccessor.fromSlot(context.getMenu().getSlot(id))).collect(Collectors.toList());
    }

    @Override
    public Iterable<SlotAccessor> getInventorySlots(Context context) {
        LocalPlayer player = context.getMinecraft().player;

        List<SlotAccessor> list = new ArrayList<>();
        CraftingStationMenu craftingStationMenu = (CraftingStationMenu) context.getMenu();

        for (Map.Entry<Direction,BlockEntity> entry : craftingStationMenu.blockEntityMap.entrySet()) {
            SideContainerWrapper sideContainerWrapper = Services.PLATFORM.getWrapper(entry.getValue());
            for (int i = 0; i < sideContainerWrapper.$getSlotCount();i++) {
                list.add(new SideContainerSlotAccessor(sideContainerWrapper, i));
            }
        }

        for (int id = 10+CraftingStationMenu.MAX_SLOTS;id < 10 + CraftingStationMenu.MAX_SLOTS + 36;id++) {
            list.add(SlotAccessor.fromPlayerInventory(player, id - CraftingStationMenu.MAX_SLOTS - 10));
        }
        return list;
    }
}
