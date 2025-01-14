package net.mcft.copy.betterstorage;

import net.mcft.copy.betterstorage.misc.handlers.CraftingHandler;
import net.minecraftforge.common.MinecraftForge;

import net.mcft.copy.betterstorage.addon.Addon;
import net.mcft.copy.betterstorage.config.Config;
import net.mcft.copy.betterstorage.config.GlobalConfig;
import net.mcft.copy.betterstorage.content.BetterStorageEntities;
import net.mcft.copy.betterstorage.content.BetterStorageItems;
import net.mcft.copy.betterstorage.content.BetterStorageTileEntities;
import net.mcft.copy.betterstorage.content.BetterStorageTiles;
import net.mcft.copy.betterstorage.item.EnchantmentBetterStorage;
import net.mcft.copy.betterstorage.misc.Constants;
import net.mcft.copy.betterstorage.misc.CreativeTabBetterStorage;
import net.mcft.copy.betterstorage.misc.DungeonLoot;
import net.mcft.copy.betterstorage.misc.Recipes;
import net.mcft.copy.betterstorage.network.ChannelHandler;
import net.mcft.copy.betterstorage.proxy.CommonProxy;
import net.mcft.copy.betterstorage.misc.handlers.ChunkEventHandler;
import net.mcft.copy.betterstorage.misc.handlers.network.InitClientWorldPacket;
import net.minecraft.creativetab.CreativeTabs;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import com.bioxx.tfc.TerraFirmaCraft;

@Mod(modid = Constants.modId,
     name = Constants.modName,
     dependencies = "required-after:Forge; after:terrafirmacraft; after:Thaumcraft; after:NotEnoughItems;",
     guiFactory = "net.mcft.copy.betterstorage.client.gui.BetterStorageGuiFactory")
public class BetterStorage {
	
	@Instance(Constants.modId)
	public static BetterStorage instance;
	
	@SidedProxy(serverSide = Constants.commonProxy,
	            clientSide = Constants.clientProxy)
	public static CommonProxy proxy;
	
	public static ChannelHandler networkChannel;
	
	public static Logger log;
	
	public static CreativeTabs creativeTab;
	
	public static Config globalConfig;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		networkChannel = new ChannelHandler();
		log = event.getModLog();
		creativeTab = new CreativeTabBetterStorage();
		
		Addon.initialize();
		
		globalConfig = new GlobalConfig(event.getSuggestedConfigurationFile());
		Addon.setupConfigsAll();
		globalConfig.load();
		globalConfig.save();
		
		BetterStorageTiles.initialize();
		BetterStorageItems.initialize();
		
		EnchantmentBetterStorage.initialize();
		
		BetterStorageTileEntities.register();
		BetterStorageEntities.register();
		DungeonLoot.add();
		
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		TerraFirmaCraft.PACKET_PIPELINE.registerPacket(InitClientWorldPacket.class);

		Recipes.add();
		proxy.initialize();

		// Register the Chunk Load/Save Handler
		MinecraftForge.EVENT_BUS.register(new ChunkEventHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Addon.postInitializeAll();
	}
	
}
