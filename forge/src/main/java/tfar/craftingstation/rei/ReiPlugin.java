package tfar.craftingstation.rei;

import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessorRegistry;
import me.shedaniel.rei.api.common.transfer.info.stack.VanillaSlotAccessor;
import me.shedaniel.rei.forge.REIPluginCommon;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import tfar.craftingstation.CraftingStation;
import tfar.craftingstation.client.CraftingStationScreen;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.platform.Services;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@REIPluginCommon
public class ReiPlugin implements REIServerPlugin {

    @Override
    public void registerSlotAccessors(SlotAccessorRegistry registry) {
        registry.register(CraftingStation.id( "side_container"),
                slotAccessor -> slotAccessor instanceof SideContainerSlotAccessor,
                new SlotAccessorRegistry.Serializer() {
                    @Override
                    public SlotAccessor read(AbstractContainerMenu menu, Player player, CompoundTag tag) {
                        CraftingStationMenu craftingStationMenu = (CraftingStationMenu)menu;
                        Direction direction = Direction.values()[tag.getInt("direction")];
                        int slot = tag.getInt("slot");
                        return new SideContainerSlotAccessor(Services.PLATFORM.getWrapper(craftingStationMenu.blockEntityMap.get(direction)),slot,direction);
                    }

                    @Override
                    public CompoundTag save(AbstractContainerMenu menu, Player player, SlotAccessor accessor) {
                        SideContainerSlotAccessor slotAccessor = (SideContainerSlotAccessor)accessor;
                        CompoundTag tag = new CompoundTag();
                        tag.putInt("slot",slotAccessor.getSlot());
                        tag.putInt("direction",slotAccessor.getDirection().ordinal());
                        return tag;
                    }
                });
    }
}
