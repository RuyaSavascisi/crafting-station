package com.tfar.examplemod;

import com.tfar.examplemod.client.CraftingStationTileSpecialRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod.EventBusSubscriber
@Mod(modid = CraftingStation.MODID, name = CraftingStation.NAME, version = CraftingStation.VERSION)
public class CraftingStation {
  public static final String MODID = "craftingstation";
  public static final String NAME = "Crafting Station";
  public static final String VERSION = "@VERSION@";

  private static final Logger LOGGER = LogManager.getLogger();

  @Mod.Instance
  public static CraftingStation INSTANCE;

  public CraftingStation() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Mod.EventHandler
  public void preInit(final FMLPreInitializationEvent event) {
    NetworkRegistry.INSTANCE.registerGuiHandler(this,new GuiHandler());
}

@SubscribeEvent
  public static void doClientStuff(final ModelRegistryEvent event) {
    ClientRegistry.bindTileEntitySpecialRenderer(CraftingStationTile.class, new CraftingStationTileSpecialRenderer());
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Objects.crafting_station),0,new ModelResourceLocation(Item.getItemFromBlock(Objects.crafting_station).getRegistryName(), "inventory"));
  }

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber
  public static class RegistryEvents {
    @SubscribeEvent
    public static void block(final RegistryEvent.Register<Block> event) {
      // register a new block here
      event.getRegistry().register(new CraftingStationBlock(Material.WOOD).setRegistryName("crafting_station"));
    }

    @SubscribeEvent
    public static void item(final RegistryEvent.Register<Item> event) {
      // register a new block here
      event.getRegistry().register(new ItemBlock(Objects.crafting_station).setRegistryName(Objects.crafting_station.getRegistryName()).setCreativeTab(CreativeTabs.DECORATIONS));
      GameRegistry.registerTileEntity(CraftingStationTile.class, new ResourceLocation(MODID, "crafting_station_tile"));
    }
  }

  @GameRegistry.ObjectHolder(MODID)
  public static class Objects {
    public static final Block crafting_station = null;
  }
}
