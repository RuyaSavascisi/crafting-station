package tfar.craftingstation.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tfar.craftingstation.CraftingStation;

import java.security.Provider;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModDatagen {

    public static void gather(GatherDataEvent e) {
        DataGenerator dataGenerator = e.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        ExistingFileHelper existingFileHelper = e.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = e.getLookupProvider();
        dataGenerator.addProvider(e.includeServer(),new ModBlockTagsProvider(packOutput,lookup,existingFileHelper));
        dataGenerator.addProvider(e.includeServer(),new ModDataMapsProvider(packOutput,lookup));
        dataGenerator.addProvider(e.includeServer(),ModLootTableProvider.create(packOutput,lookup));
    }

    public static Stream<Block> getKnownBlocks() {
        return getKnown(BuiltInRegistries.BLOCK);
    }
    public static Stream<Item> getKnownItems() {
        return getKnown(BuiltInRegistries.ITEM);
    }

    public static Stream<EntityType<?>> getKnownEntityTypes() {
        return getKnown(BuiltInRegistries.ENTITY_TYPE);
    }

    public static <V> Stream<V> getKnown(Registry<V> registry) {
        return registry.stream().filter(o -> registry.getKey(o).getNamespace().equals(CraftingStation.MOD_ID));
    }

}
