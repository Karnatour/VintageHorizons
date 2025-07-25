/*
 *    This file is part of the Distant Horizons mod
 *    licensed under the GNU LGPL v3 License.
 *
 *    Copyright (C) 2020 James Seibel
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, version 3.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.seibel.distanthorizons.forge;

import com.seibel.distanthorizons.common.AbstractModInitializer;
import com.seibel.distanthorizons.core.api.internal.ServerApi;
import com.seibel.distanthorizons.core.dependencyInjection.SingletonInjector;
import com.seibel.distanthorizons.core.wrapperInterfaces.misc.IPluginPacketSender;
import com.seibel.distanthorizons.core.wrapperInterfaces.modAccessor.IModChecker;
import com.seibel.distanthorizons.forge.wrappers.modAccessor.ModChecker;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import java.util.function.Consumer;

/**
 * Initialize and setup the Mod. <br>
 * If you are looking for the real start of the mod
 * check out the ClientProxy.
 */
@Mod(modid = "distanthorizons", name = "DistantHorizons", version = "1.2.2")
public class ForgeMain extends AbstractModInitializer
{
	
	public static final boolean IS_QUARK_LOADED = Loader.isModLoaded("quark");
	public static final boolean IS_FURENIKUSROADS_LOADED = Loader.isModLoaded("furenikusroads");
	public static final boolean IS_IMMERSIVERAILRAODING_LOADED = Loader.isModLoaded("immersiverailroading");
	public static final boolean IS_BOTANIA_LOADED = Loader.isModLoaded("botania");
	
	@Mod.Instance
	public static ForgeMain instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkManagerCallback());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			this.onInitializeClient();
		}
		else
		{
			this.onInitializeServer();
		}
	}
	
	// ServerWorldLoadEvent
	@Mod.EventHandler
	public void dedicatedWorldLoadEvent(FMLServerAboutToStartEvent event)
	{
		ServerApi.INSTANCE.serverLoadEvent(event.getServer().isDedicatedServer());
	}
	
	// ServerWorldUnloadEvent
	@Mod.EventHandler
	public void serverWorldUnloadEvent(FMLServerStoppingEvent event)
	{
		ServerApi.INSTANCE.serverUnloadEvent();
	}
	
	@Override
	protected void createInitialBindings()
	{
		SingletonInjector.INSTANCE.bind(IModChecker.class, ModChecker.INSTANCE);
		SingletonInjector.INSTANCE.bind(IPluginPacketSender.class, new ForgePluginPacketSender());
	}
	
	@Override
	protected IEventProxy createClientProxy() { return new ForgeClientProxy(); }
	
	@Override
	protected IEventProxy createServerProxy(boolean isDedicated) { return new ForgeServerProxy(isDedicated); }
	
	@Override
	protected void initializeModCompat()
	{
        /* TODO
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
				() -> (client, parent) -> GetConfigScreen.getScreen(parent));
		*/
	}
	
	@Override
	protected void subscribeClientStartedEvent(Runnable eventHandler)
	{
		// FIXME What event is this?
	}
	
	@Mod.EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent event)
	{
		if (eventHandlerStartServer != null)
			eventHandlerStartServer.accept(event.getServer());
	}
	
	Consumer<MinecraftServer> eventHandlerStartServer;
	
	@Override
	protected void subscribeServerStartingEvent(Consumer<MinecraftServer> eventHandler)
	{
		eventHandlerStartServer = eventHandler;
	}
	
	@Override
	protected void runDelayedSetup() { SingletonInjector.INSTANCE.runDelayedSetup(); }
	
}
