package tfar.craftingstation.blockentity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import tfar.craftingstation.init.ModBlockEntityTypes;
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
import tfar.craftingstation.menu.CraftingStationMenu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CraftingStationBlockEntity extends BlockEntity implements MenuProvider {

    public SimpleContainer input;

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
        this.input = new SimpleContainer(9) {
            @Override
            public void setChanged() {
                super.setChanged();
                CraftingStationBlockEntity.this.setChanged();
            }
        };
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        ListTag compound = this.input.createTag();
        tag.put("inv", compound);
        if (this.customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        ListTag invTag = tag.getList("inv",Tag.TAG_COMPOUND);
        input.fromTag(invTag);
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
        return new CraftingStationMenu(id, playerInventory, data, input,worldPosition);
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

