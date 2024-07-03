package tfar.craftingstation;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.craftingstation.client.ModClientNeoForge;
import tfar.craftingstation.datagen.ModDatagen;
import tfar.craftingstation.init.ModBlockEntityTypes;
import tfar.craftingstation.init.ModBlocks;
import tfar.craftingstation.init.ModMenuTypes;
import tfar.craftingstation.menu.CraftingStationMenu;
import tfar.craftingstation.network.PacketHandlerNeoForge;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CraftingStation.MOD_ID)
public class CraftingStationNeoForge {
    // Directly reference a log4j logger.



    public static final Logger LOGGER = LogManager.getLogger();

    public CraftingStationNeoForge(IEventBus bus, Dist dist, ModContainer modContainer) {
        // Register the setup method for modloading
        modContainer.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        bus.addListener(this::setup);
        bus.addListener(this::enqueueIMC);
        bus.addListener(ModDatagen::gather);
        bus.addListener(RegistryEvents::block);
        bus.addListener(this::addCreative);
        bus.addListener(PacketHandlerNeoForge::register);
        if (dist.isClient()) {
            ModClientNeoForge.setup(bus);
        }
        CraftingStation.init();
    }

    public static final Configs.Server SERVER;
    public static final ModConfigSpec SERVER_SPEC;
    public static final Configs.Client CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;

    static {
        final Pair<Configs.Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Configs.Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
        final Pair<Configs.Server, ModConfigSpec> specPair2 = new ModConfigSpec.Builder().configure(Configs.Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.crafting_station);
            event.accept(ModBlocks.crafting_station_slab);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("craftingtweaks", "RegisterProvider", () -> {
            CompoundTag tagCompound = new CompoundTag();
            tagCompound.putString("ContainerClass", CraftingStationMenu.class.getName());
            tagCompound.putString("AlignToGrid", "left");
            return tagCompound;
        });
    }

    public static class RegistryEvents {
        @SubscribeEvent
        public static void block(final RegisterEvent event) {
            // register a new block here
            event.register(Registries.BLOCK, CraftingStation.id("crafting_station"), () -> ModBlocks.crafting_station);
            event.register(Registries.BLOCK, CraftingStation.id("crafting_station_slab"), () -> ModBlocks.crafting_station_slab);
            // register a new item here
            Item.Properties properties = new Item.Properties();
            event.register(Registries.ITEM, CraftingStation.id("crafting_station"), () -> new BlockItem(ModBlocks.crafting_station, properties));
            event.register(Registries.ITEM, CraftingStation.id("crafting_station_slab"), () -> new BlockItem(ModBlocks.crafting_station_slab, properties));
            event.register(Registries.MENU, CraftingStation.id("crafting_station"), () -> ModMenuTypes.crafting_station);
            event.register(Registries.BLOCK_ENTITY_TYPE, CraftingStation.id("crafting_station"), () -> ModBlockEntityTypes.crafting_station);
        }

    }
}
