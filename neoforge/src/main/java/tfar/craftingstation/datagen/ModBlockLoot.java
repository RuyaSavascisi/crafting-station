package tfar.craftingstation.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import tfar.craftingstation.init.ModBlocks;

public class ModBlockLoot extends VanillaBlockLoot {

    public ModBlockLoot(HolderLookup.Provider pRegistries) {
        super(pRegistries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.crafting_station);
        this.add(ModBlocks.crafting_station_slab, this::createSlabItemTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModDatagen.getKnownBlocks().toList();
    }
}
