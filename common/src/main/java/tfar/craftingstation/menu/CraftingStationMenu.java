package tfar.craftingstation.menu;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.*;
import tfar.craftingstation.CommonTagUtil;
import tfar.craftingstation.CraftingStation;
import tfar.craftingstation.ModIntegration;
import tfar.craftingstation.PersistantCraftingContainer;
import tfar.craftingstation.blockentity.CraftingStationBlockEntity;
import tfar.craftingstation.init.ModMenuTypes;
import tfar.craftingstation.network.S2CLastRecipePacket;
import tfar.craftingstation.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import tfar.craftingstation.slot.SlotFastCraft;
import tfar.craftingstation.util.SideContainerWrapper;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CraftingStationMenu extends AbstractContainerMenu {

    public static final int MAX_SLOTS = 54;

    public final PersistantCraftingContainer craftMatrix;
    public final ResultContainer craftResult = new ResultContainer();
    public final Level world;
    public final CraftingStationBlockEntity tileEntity;

    public Map<Direction,ItemStack> blocks = new EnumMap<>(Direction.class);
    public Map<Direction,BlockEntity> blockEntityMap = new EnumMap<>(Direction.class);

    public final Map<Direction,Component> containerNames = new EnumMap<>(Direction.class);
    private final Player player;
    private final BlockPos pos;
    public Recipe<CraftingContainer> lastRecipe;
    protected Recipe<CraftingContainer> lastLastRecipe;

    public CraftingStationMenu(int id, Inventory inv,BlockPos pos) {
        this(id, inv, new SimpleContainer(9),pos);
    }


    public CraftingStationMenu(int id, Inventory inv, SimpleContainer simpleContainer,BlockPos pos) {
        super(ModMenuTypes.crafting_station, id);
        this.player = inv.player;
        this.pos = pos;
        this.world = player.level();
        this.tileEntity = (CraftingStationBlockEntity) ModIntegration.getTileEntityAtPos(player.level(),pos);
        setCurrentContainer(tileEntity.getCurrentContainer());
        this.craftMatrix = new PersistantCraftingContainer(this, simpleContainer);


        addOwnSlots();

        if (Services.PLATFORM.getConfig().sideContainers()) {
            searchSideInventories();
        }

        addSideInventorySlots();
        addPlayerSlots(inv);
        slotsChanged(craftMatrix);
    }

    public static class SideContainerSlot extends Slot {


        private final CraftingStationMenu craftingStationMenu;

        public SideContainerSlot(int slot, int $$2, int $$3, CraftingStationMenu craftingStationMenu) {
            super(new SimpleContainer(0), slot, $$2, $$3);
            this.craftingStationMenu = craftingStationMenu;
        }


        @Override
        public ItemStack getItem() {
            return craftingStationMenu.getCurrentHandler().$getStack(getContainerSlot());
        }

        @Override
        public ItemStack remove(int pAmount) {
            return craftingStationMenu.getCurrentHandler().$removeStack(getContainerSlot(), pAmount);
        }

        @Override
        public boolean mayPlace(ItemStack $$0) {
            return craftingStationMenu.getCurrentHandler().$valid(getContainerSlot());
        }

        @Override
        public void set(ItemStack $$0) {
            craftingStationMenu.getCurrentHandler().$setStack(getContainerSlot(),$$0);
        }
    }

    public SideContainerWrapper getCurrentHandler() {
        return Services.PLATFORM.getWrapper(blockEntityMap.get(getSelectedContainer()));
    }

    protected void addSideInventorySlots() {
        int rows = 9;
        int cols = 6;
        for (int row = 0; row < rows;row++) {
            for (int col = 0;col < cols;col++) {
                int index = col + cols * row;
                int xPos = -117 + col * 18;
                int yPos = 17+row * 18;
                addSlot(new SideContainerSlot(index,xPos,yPos,this));
            }
        }
    }

    public boolean hasSideContainers() {
        return !blocks.isEmpty();
    }

    public int subContainerSize() {
        return getCurrentHandler().$getSlotCount();
    }


    public Direction getSelectedContainer() {
        return currentContainer;
    }

    //it goes crafting output slot | 0
    //crafting input slots | 1 to 9
    //side inventories (if any) | 10 to (9 + subContainerSize)
    //player inventory | (10 + subContainerSides)

    protected void searchSideInventories() {
            ;
            // detect te
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = pos.relative(dir);

                BlockEntity te = world.getBlockEntity(neighbor);
                if (te != null && !(te instanceof CraftingStationBlockEntity)) {
                    // if blacklisted, skip checks entirely
                    if (CommonTagUtil.isIn(CraftingStation.blacklisted,te.getType()))
                        continue;
                    if (te instanceof Container container && !container.stillValid(player)) {
                        continue;
                    }

                    // try internal access first
                    if (Services.PLATFORM.hasCapability(te)) {
                        blockEntityMap.put(dir, te);
                        blocks.put(dir,new ItemStack(world.getBlockState(neighbor).getBlock()));
                        containerNames.put(dir,te instanceof MenuProvider menuProvider? menuProvider.getDisplayName() : te.getBlockState().getBlock().getName());
                    }
                    // try sided access else
                    //      if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite())) {
                    //        if(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite()) instanceof IItemHandlerModifiable) {
                    //          inventoryTE = te;
                    //         accessDir = dir.getOpposite();
                    //         break;
                    //       }
                    //   }
                }
            }
    }


    private void addOwnSlots() {
        // crafting result
        this.addSlot(new SlotFastCraft(player, this.craftMatrix, craftResult, 0, 124, 35));

        // crafting grid
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                addSlot(new Slot(craftMatrix, x + 3 * y, 30 + 18 * x, 17 + 18 * y));
            }
        }
    }

    private void addPlayerSlots(Inventory playerInventory) {
        // inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(playerInventory, 9 + x + 9 * y, 8 + 18 * x, 84 + 18 * y));
            }
        }

        // hotbar
        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(playerInventory, x, 8 + 18 * x, 142));
        }
    }

    // update crafting
    //clientside only
    //@Override
    //public void setAll(List<ItemStack> p_190896_1_) {
    //    craftMatrix.setDoNotCallUpdates(true);
    //    super.setAll(p_190896_1_);
     //   craftMatrix.setDoNotCallUpdates(false);
     //   craftMatrix.onCraftMatrixChanged();
   // }

    @Override
    public void slotsChanged(Container inventory) {
        this.slotChangedCraftingGrid(world, player, craftMatrix, craftResult);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {

        if (hasSideContainers()) {

            return handleTransferWithSides(playerIn, index);
        } else {

            Slot slot = this.slots.get(index);

            if (slot == null || !slot.hasItem()) {
                return ItemStack.EMPTY;
            }

            ItemStack ret = slot.getItem().copy();
            ItemStack stack = slot.getItem().copy();

            boolean nothingDone;

            //is this the crafting output slot?
            if (index == 0) {

                // Try moving module -> player inventory
                nothingDone = !moveToPlayerInventory(stack);

                // Try moving module -> tile inventory
            }

            // Is the slot an input slot??
            else if (index < 10) {
                // Try moving module -> player inventory
                nothingDone = !moveToPlayerInventory(stack);

                // Try moving module -> tile inventory
            }
            // Is the slot from the tile?
            else {
                // try moving player -> modules
                nothingDone = !moveToCraftingStation(stack);

                // Try moving player -> tile inventory
            }

            if (nothingDone) {
                return ItemStack.EMPTY;
            }
            return notifySlotAfterTransfer(playerIn, stack, ret, slot);
        }
    }

    protected ItemStack handleTransferWithSides(Player player, int index) {
        Slot slot = this.slots.get(index);

        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack ret = slot.getItem().copy();
        ItemStack stack = ret.copy();

        boolean nothingDone;

        //is this the crafting output slot?
        if (index == 0) {

            nothingDone = !refillSideInventory(stack);
            // Try moving module -> player inventory
            nothingDone &= !moveToPlayerInventory(stack);

            // Try moving module -> tile inventory
            nothingDone &= !mergeItemStackMove(stack, 10, 10 + subContainerSize());
        }

        // Is the slot an input slot??
        else if (index < 10) {
            //try to refill side inventory
            nothingDone = !refillSideInventory(stack);
            // Try moving crafting station -> player inventory
            nothingDone &= !moveToPlayerInventory(stack);

            // Try moving crafting station -> side inventory
            nothingDone &= !moveToSideInventory(stack);
        }
        // Is the slot from the side inventories?
        else if (index < 10 + subContainerSize()) {
            // Try moving crafting station -> preferred modules
            nothingDone = !moveToCraftingStation(stack);

            // Try moving module -> player inventory
            nothingDone &= !moveToPlayerInventory(stack);
        }
        // Slot is from the player inventory
        else if (index >= 10 + subContainerSize()) {
            // try moving player -> modules
            nothingDone = !moveToCraftingStation(stack);

            // Try moving player -> crafting station inventory
            nothingDone &= !moveToSideInventory(stack);
        }
        // you violated some assumption or something. Shame on you.
        else {
            return ItemStack.EMPTY;
        }

        if (nothingDone) {
            return ItemStack.EMPTY;
        }
        return notifySlotAfterTransfer(player, stack, ret, slot);
    }

    protected void slotChangedCraftingGrid(Level world, Player player, CraftingContainer inv, ResultContainer result) {
        ItemStack itemstack = ItemStack.EMPTY;

        //If polymorph is installed we have to check earlier; the original caching doesn't detect the changed selection. Else, continue as previous.
        if (Services.PLATFORM.isModLoaded(ModIntegration.POLYMORPH)) {
            lastRecipe = ModIntegration.findRecipe(this, inv, world, player);
        } else {
            // if the recipe is no longer valid, update it
            if (lastRecipe == null || !lastRecipe.matches(inv, world)) {
                lastRecipe = ModIntegration.findRecipe(this, inv, world, player);
            }
        }

        // if the recipe is no longer valid, update it
        if (lastRecipe == null || !lastRecipe.matches(inv, world)) {
            lastRecipe = ModIntegration.findRecipe(this, inv, world, player);
        }

        // if we have a recipe, fetch its result
        if (lastRecipe != null) {
            itemstack = lastRecipe.assemble(inv,world.registryAccess());
        }
        // set the slot on both sides, client is for display/so the client knows about the recipe
        result.setItem(0, itemstack);

        // update recipe on server
        if (!world.isClientSide) {
            ServerPlayer entityplayermp = (ServerPlayer) player;

            // we need to sync to all players currently in the inventory
            List<ServerPlayer> relevantPlayers = getAllPlayersWithThisContainerOpen(this, entityplayermp.serverLevel());

            // sync result to all serverside inventories to prevent duplications/recipes being blocked
            // need to do this every time as otherwise taking items of the result causes desync
            syncResultToAllOpenWindows(itemstack, relevantPlayers);

            // if the recipe changed, update clients last recipe
            // this also updates the client side display when the recipe is added
            if (lastLastRecipe != lastRecipe) {
                syncRecipeToAllOpenWindows(lastRecipe, relevantPlayers);
                lastLastRecipe = lastRecipe;
            }
        }
    }

    private void syncResultToAllOpenWindows(final ItemStack stack, List<ServerPlayer> players) {
        players.forEach(otherPlayer -> {
            otherPlayer.containerMenu.setItem(0,this.getStateId(), stack);
            //otherPlayer.connection.sendPacket(new SPacketSetSlot(otherPlayer.openContainer.windowId, SLOT_RESULT, stack));
        });
    }

    private void syncRecipeToAllOpenWindows(final Recipe<CraftingContainer> lastRecipe, List<ServerPlayer> players) {
        players.forEach(otherPlayer -> {
            // safe cast since hasSameContainerOpen does class checks
            ((CraftingStationMenu) otherPlayer.containerMenu).lastRecipe = lastRecipe;
            Services.PLATFORM.sendToClient(new S2CLastRecipePacket(lastRecipe), otherPlayer);
        });
    }

    private List<ServerPlayer> getAllPlayersWithThisContainerOpen(CraftingStationMenu container, ServerLevel server) {
        return server.players().stream()
                .filter(player -> hasSameContainerOpen(container, player))
                .collect(Collectors.toList());
    }

    private boolean hasSameContainerOpen(CraftingStationMenu container, Player playerToCheck) {
        return playerToCheck instanceof ServerPlayer &&
                playerToCheck.containerMenu.getClass().isAssignableFrom(container.getClass()) &&
                this.sameGui((CraftingStationMenu) playerToCheck.containerMenu);
    }

    public boolean sameGui(CraftingStationMenu otherContainer) {
        return this.tileEntity == otherContainer.tileEntity;
    }

    @Nonnull
    protected ItemStack notifySlotAfterTransfer(Player player, @Nonnull ItemStack stack, @Nonnull ItemStack original, Slot slot) {
        // notify slot
        slot.onQuickCraft(stack, original);

        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        // update slot we pulled from
        slot.set(stack);
        slot.onTake(player, stack);

        if (slot.hasItem() && slot.getItem().isEmpty()) {
            slot.set(ItemStack.EMPTY);
        }

        return original;
    }

    //return true if anything happened
    protected boolean moveToSideInventory(@Nonnull ItemStack itemstack) {
        return hasSideContainers() && this.mergeItemStackMove(itemstack, 10, 10 + subContainerSize());
    }

    protected boolean moveToPlayerInventory(@Nonnull ItemStack itemstack) {
        return this.moveItemStackTo(itemstack, 10 + subContainerSize(), this.slots.size(), false);
    }

    protected boolean refillSideInventory(@Nonnull ItemStack itemStack) {
        return this.mergeItemStackRefill(itemStack, 10, 10 + subContainerSize());
    }

    protected boolean moveToCraftingStation(@Nonnull ItemStack itemstack) {
        return this.moveItemStackTo(itemstack, 1, 10, false);
    }

    // Fix for a vanilla bug: doesn't take Slot.getMaxStackSize into account
    @Override
    protected boolean moveItemStackTo(@Nonnull ItemStack stack, int startIndex, int endIndex, boolean useEndIndex) {
        boolean didSomething = mergeItemStackRefill(stack, startIndex, endIndex);
        if (!stack.isEmpty()) didSomething |= mergeItemStackMove(stack, startIndex, endIndex);
        return didSomething;
    }

    // only refills items that are already present
    //return true if successful
    protected boolean mergeItemStackRefill(@Nonnull ItemStack stack, int startIndex, int endIndex) {
        if (stack.isEmpty()) return false;

        boolean didSomething = false;

        Slot targetSlot;
        ItemStack slotStack;

        if (stack.isStackable()) {

            for (int k = startIndex; k < endIndex; k++) {
                if (stack.isEmpty()) break;
                targetSlot = this.slots.get(k);
                slotStack = targetSlot.getItem();

                if (!slotStack.isEmpty()
                        && ItemStack.isSameItemSameTags(stack, slotStack)
                        && this.canTakeItemForPickAll(stack, targetSlot)) {
                    int l = slotStack.getCount() + stack.getCount();
                    int limit = targetSlot.getMaxStackSize(stack);

                    if (l <= limit) {
                        stack.setCount(0);
                        slotStack.setCount(l);
                        targetSlot.setChanged();
                        didSomething = true;
                    } else if (slotStack.getCount() < limit) {
                        stack.shrink(limit - slotStack.getCount());
                        slotStack.setCount(limit);
                        targetSlot.setChanged();
                        didSomething = true;
                    }
                }
            }
        }
        return didSomething;
    }

    // only moves items into empty slots
    protected boolean mergeItemStackMove(@Nonnull ItemStack stack, int startIndex, int endIndex) {
        if (stack.isEmpty()) return false;

        boolean didSomething = false;

        for (int k = startIndex; k < endIndex; k++) {
            Slot targetSlot = this.slots.get(k);
            ItemStack slotStack = targetSlot.getItem();

            if (slotStack.isEmpty() && targetSlot.mayPlace(stack) && this.canTakeItemForPickAll(stack, targetSlot)) // Forge: Make sure to respect isItemValid in the slot.
            {
                int limit = targetSlot.getMaxStackSize(stack);
                ItemStack stack2 = stack.copy();
                if (stack2.getCount() > limit) {
                    stack2.setCount(limit);
                    stack.shrink(limit);
                } else {
                    stack.setCount(0);
                }
                targetSlot.set(stack2);
                targetSlot.setChanged();
                didSomething = true;

                if (stack.isEmpty()) {
                    break;
                }
            }
        }
        return didSomething;
    }


    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != craftResult && super.canTakeItemForPickAll(stack, slot);
    }

    public void updateLastRecipeFromServer(Recipe<CraftingContainer> recipe) {
        lastRecipe = recipe;
        // if no recipe, set to empty to prevent ghost outputs when another player grabs the result
        this.craftResult.setItem(0, recipe != null ? recipe.assemble(craftMatrix,world.registryAccess()) : ItemStack.EMPTY);
    }

    public boolean needsScroll() {
        return false;
    }

    protected Direction currentContainer;

    void setCurrentContainer(Direction currentContainer) {
        this.currentContainer = currentContainer;
    }


    public NonNullList<ItemStack> getRemainingItems() {
        return lastRecipe != null && lastRecipe.matches(craftMatrix, world) ? lastRecipe.getRemainingItems(craftMatrix) : craftMatrix.getStackList();
    }


    public enum ButtonAction {
        CLEAR,TAB_0,TAB_1,TAB_2,TAB_3,TAB_4,TAB_5;
        static final ButtonAction[] VALUES = values();
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int id) {
        if (id < 0 || id >= ButtonAction.VALUES.length) return false;
        ButtonAction buttonAction = ButtonAction.VALUES[id];
        if (pPlayer instanceof ServerPlayer) {
            switch (buttonAction) {
                case CLEAR -> {
                    for (int i = 1; i < 10; i++) quickMoveStack(player, i);
                }
                case TAB_0, TAB_1, TAB_2, TAB_3, TAB_4, TAB_5 -> {
                    Direction direction = Direction.values()[id - 1];
                    if (blockEntityMap.get(direction) != null)
                        setCurrentContainer(direction);
                }
            }
        }
        return true;
    }

    @Override
    public void removed(Player $$0) {
        super.removed($$0);
        if (!$$0.level().isClientSide) {
            tileEntity.setCurrentContainer(currentContainer);
        }
    }

    public  void setClientData(Map<Direction,ItemStack> icons) {
        this.blocks = icons;
    }
}