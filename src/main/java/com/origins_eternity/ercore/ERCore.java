package com.origins_eternity.ercore;

import com.origins_eternity.ercore.utils.proxy.CommonProxy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = ERCore.MOD_ID, name = ERCore.MOD_NAME, version = ERCore.VERSION, dependencies = "after:tconstruct@[1.12-2.7.2.15,);after:firstaid@[1.6.13,);")
public class ERCore {
	public static final String MOD_ID = "ercore";
	public static final String MOD_NAME = "ER Core";
	public static final String VERSION = "1.3.0";
	
	@SidedProxy(clientSide = "com.origins_eternity.ercore.utils.proxy.ClientProxy", serverSide = "com.origins_eternity.ercore.utils.proxy.CommonProxy")
	public static CommonProxy proxy;

	static { FluidRegistry.enableUniversalBucket(); }

	public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel("ercore");

	@EventHandler
	public void construction(FMLConstructionEvent event) { proxy.construction(event); }

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) { proxy.preInit(event); }

	@EventHandler
	public void init(FMLInitializationEvent event) { proxy.init(event); }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) { proxy.postInit(event); }

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) { proxy.serverStart(event); }
}