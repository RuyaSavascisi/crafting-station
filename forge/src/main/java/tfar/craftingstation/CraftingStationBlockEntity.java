package tfar.craftingstation;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import tfar.craftingstation.init.ModBlockEntityTypes;
import tfar.craftingstation.util.CraftingStationItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CraftingStationBlockEntity extends BlockEntity implements MenuProvider {

    public CraftingStationItemHandler input;

    private Component customName;
    private Direction currentContainer = Direction.NORTH;

    ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return currentContainer.ordinal();
        }

        @Override
        public void set(int pIndex, int pValue) {
            currentContainer = Direction.values()[pValue];
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public CraftingStationBlockEntity(BlockPos pPos, BlockState pState) {
        super(ModBlockEntityTypes.crafting_station, pPos, pState);
        this.input = new CraftingStationItemHandler(9, this);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        CompoundTag compound = this.input.serializeNBT();
        tag.put("inv", compound);
        if (this.customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        CompoundTag invTag = tag.getCompound("inv");
        input.deserializeNBT(invTag);
        if (tag.contains("CustomName", Tag.TAG_STRING)) {
            this.customName = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
        super.load(tag);
    }

    @Nonnull
    @Override
    public Component getDisplayName() {
        return getCustomName() != null ? getCustomName() : Component.translatable("title.crafting_station");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new CraftingStationMenu(id, playerInventory, ContainerLevelAccess.create(level,worldPosition), data);
    }

    public void setCustomName(@Nullable Component pName) {
        this.customName = pName;
    }

    @Nullable
    public Component getCustomName() {
        return this.customName;
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();    // okay to send entire inventory on chunk load
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}

